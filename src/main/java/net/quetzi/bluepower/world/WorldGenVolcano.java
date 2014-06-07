package net.quetzi.bluepower.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.init.Config;

public class WorldGenVolcano{
    public class Pos{
        public final int x, z;

        public Pos(int x, int z){
            this.x = x;
            this.z = z;
        }
    }

    private final Map<Pos, Integer> vulcanoMap = new HashMap<Pos, Integer>();
    private static final int MAX_VULCANO_RADIUS = 200;//absulute max radius a vulcano can have, this should be a magnitude bigger than an average vulcano radius.

    public void generate(World world, Random rand, int middleX, int vulcanoHeight, int middleZ){
        List<Pos>[] distMap = calculateDistMap();
        boolean first = true;
        for(int dist = 0; dist < distMap.length; dist++) {//Loop through every XZ position of the vulcano, in order of how close the positions are from the center. The vulcano will be generated from the center to the edge.
            List<Pos> distList = distMap[dist];
            boolean isFinished = true;//Will stay true as long as there were still blocks being generated at this distance from the vulcano.
            for(Pos p : distList) {
                int worldHeight = world.getHeightValue(p.x + middleX, p.z + middleZ) - 1;
                int posHeight = first ? vulcanoHeight : getNewVulcanoHeight(worldHeight, p, rand, dist);
                if(posHeight >= 0 && (posHeight > worldHeight || canReplace(world, p.x + middleX, posHeight, p.z + middleZ))) {// If the calculated desired vulcano height is higher than the world height, generate.
                    vulcanoMap.put(p, posHeight);
                    if(!first){
                        for(int i = posHeight; i > 0 && (i > worldHeight || canReplace(world, p.x + middleX, i, p.z + middleZ)); i--) {
                            world.setBlock(p.x + middleX, i, p.z + middleZ, BPBlocks.basalt, 0, 0);
                        }
                        for(int i = posHeight + 1; i < vulcanoHeight; i++){
                            if(canReplace(world, p.x + middleX, i, p.z + middleZ)) world.setBlock(p.x + middleX, i, p.z + middleZ, Blocks.air, 0, 0);
                        }
                    }
                    isFinished = false;
                }
                first = false;
            }
            if(isFinished) break;
        }
        generateLavaColumn(world, middleX, vulcanoHeight, middleZ, rand);
    }
    
    private boolean canReplace(World world, int x, int y, int z){
        if(world.isAirBlock(x, y, z)) return true;
        Block block = world.getBlock(x, y, z);
        Material material = block.getMaterial();
        return material == Material.wood || material == Material.cactus || material == Material.leaves || material == Material.plants || material == Material.vine || block == Blocks.water || block == Blocks.flowing_water;
    }
    
    private void generateLavaColumn(World world, int x, int topY, int z, Random rand){
       // world.setBlock(x, topY, z, Blocks.lava);
        if(rand.nextDouble() < Config.volcanoActiveToInactiveRatio){
            world.setBlock(x, topY, z, BPBlocks.cracked_basalt);
        }else{
            world.setBlock(x, topY + 1, z,Blocks.lava);
            world.setBlock(x, topY, z,Blocks.lava);//This block set, which does update neighbors, will make the lava above update.
        }
        for(int y = 10; y < topY; y++){
            world.setBlock(x, y, z, Blocks.lava, 0, 0);
            world.setBlock(x+1, y, z, BPBlocks.basalt, 0, 0);
            world.setBlock(x-1, y, z, BPBlocks.basalt, 0, 0);
            world.setBlock(x, y, z+1, BPBlocks.basalt, 0, 0);
            world.setBlock(x, y, z-1, BPBlocks.basalt, 0, 0);
        }
    }

  /**
   * Saves an array of relative Positions with distance to origin. The index is the distance, the element the positions with that distance to the origin.
   */
    @SuppressWarnings("unchecked")
    private  List<Pos>[] calculateDistMap(){
        List<Pos>[] distMap = new List[MAX_VULCANO_RADIUS];
        for(int x = -MAX_VULCANO_RADIUS; x <= MAX_VULCANO_RADIUS; x++) {
            for(int z = -MAX_VULCANO_RADIUS; z <= MAX_VULCANO_RADIUS; z++) {
                int dist = (int)Math.sqrt(x * x + z * z);
                if(dist < MAX_VULCANO_RADIUS) {
                    List<Pos> distList = distMap[dist];
                    if(distList == null) {
                        distList = new ArrayList<Pos>();
                        distMap[dist] = distList;
                    }
                    distList.add(new Pos(x, z));
                }
            }
        }
        return distMap;
    }

    /**
     * Calculates a height for the requested position. It looks at the adjacent (already generated) vulcano heights, takes the average,
     * and blends in a bit of randomness. If there are no neighbors this is the first vulcano block generated, meaning it's the center,
     * meaning it should get the max height.
     * @param requestedPos
     * @param maxHeight
     * @return
     */
    private int getNewVulcanoHeight(int worldHeight, Pos requestedPos, Random rand, int distFromCenter){
        int neighborCount = 0;
        int totalHeight = 0;
        for(int x = requestedPos.x - 1; x <= requestedPos.x + 1; x++) {
            for(int z = requestedPos.z - 1; z <= requestedPos.z + 1; z++) {
                int neighborHeight = getNeighborHeight(x, z);
                if(neighborHeight != -1) {
                    neighborCount++;
                    totalHeight += neighborHeight;
                }
            }
        }
        if(neighborCount != 0){
            double avgHeight = (double)totalHeight / neighborCount;
            if((int)avgHeight < worldHeight + 2 && rand.nextInt(5) != 0) return (int)avgHeight - 2;
            //Formula that defines how fast the volcano descends. Using a square function to make it steeper at the top, and added randomness.
            int blocksDown;
            if(distFromCenter < 2){
                blocksDown = 0;
            }else if(distFromCenter == 2){
                blocksDown = rand.nextInt(2);
            }else{
                blocksDown = (int)(Math.pow(avgHeight - worldHeight + 1, 1.2) * 0.02D + ((rand.nextDouble() - 0.5) * 3) + 0.4D);
            }
            if(blocksDown < 0) blocksDown = 0;
            int newHeight = (int)avgHeight - blocksDown;
            return newHeight;
        }else{
            return -1;
        }
    }

    /**
     * This helper method is created so we don't have to create an object just to do a vulcanoMap.get(new Pos(x,z)).
     * @param x
     * @param z
     * @return
     */
    private int getNeighborHeight(int x, int z){
        for(Map.Entry<Pos, Integer> entry : vulcanoMap.entrySet()) {
            if(entry.getKey().x == x && entry.getKey().z == z) return entry.getValue();
        }
        return -1;
    }
}
