package net.quetzi.bluepower.tileentities.tier1;

import java.util.List;

import scala.runtime.StringFormat;
import scala.util.Random;
import scala.util.automata.WordBerrySethi;

import com.mojang.authlib.GameProfile;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.org.apache.bcel.internal.generic.AALOAD;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.entity.BPFakePlayer;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.TileBase;

/**
 * 
 * @author TheFjong
 * 
 */
public class TileDeployer extends TileBase implements ISidedInventory {
    
    private final ItemStack[]     allInventories = new ItemStack[9];
    public boolean                isActive       = false;
    private boolean               redstoneSignal;
    protected static BPFakePlayer fakeplayer     = null;
    
    @Override
    public void updateEntity() {
    
        isActive = worldObj.isDaytime();
        
    }
    
    public void setRedstoneSignal(boolean newValue) {
    
        if (newValue != redstoneSignal) {
            redstoneSignal = newValue;
            if (redstoneSignal) {
                if (!worldObj.isRemote) {
                    placeItem();
                }
            }
        }
    }
    
    private static final int[] ROTATION_SIDE_MAPPING = { 0, 0, 0, 2, 3, 1 };
    
    private void placeItem() {
    
        ForgeDirection direction = this.getFacingDirection();
        FakePlayer player = new FakePlayer((WorldServer) worldObj, new GameProfile(Refs.MODID + "." + xCoord + "." + yCoord + "." + zCoord,".bluepower.fakeplayer.deployer"));
        ForgeDirection side = ForgeDirection.VALID_DIRECTIONS[getBlockMetadata() % ForgeDirection.VALID_DIRECTIONS.length];
        
        player.eyeHeight = .5F;
        player.theItemInWorldManager.setBlockReachDistance(1);
        System.out.println(player);
        
       
        
        player.prevRotationPitch = player.rotationYaw = 90;
        player.prevRotationYawHead = 50;
        player.posX = xCoord;
        player.posY = yCoord;
        player.posZ = zCoord;
        
        float hitX = xCoord;
        float hitY = xCoord;
        float hitZ = xCoord;
        
        for (int i = 0; i < allInventories.length; i++) {
            
            if (allInventories != null) {
                if (!(allInventories[i] == null)) {
                    
                    if (allInventories[i].getItem() instanceof ItemBlock) {
                        Block block = Block.getBlockFromItem(allInventories[i].getItem());
                        Material material = worldObj.getBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ).getMaterial();
                        if (worldObj.isAirBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ)|| material.isLiquid() || material.isReplaceable()) {
                            worldObj.setBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, block);
                              
                            decrStackSize(i, 1);
                            break;
                        }
                    } else {
                        
                        player.inventory.currentItem = 0;
                        player.inventory.setInventorySlotContents(0, allInventories[i]);
                        
                        if (allInventories[i].stackSize > 1) {
                            
                            ItemStack result = allInventories[i].useItemRightClick(worldObj, player);
                            allInventories[getAvailableSlot()] = player.inventory.getStackInSlot(1);
                            if (result == null || !result.isItemEqual(allInventories[i])|| !ItemStack.areItemStackTagsEqual(result, allInventories[i])) {
                            } else {
                                
                                player.theItemInWorldManager.activateBlockOrUseItem(player, worldObj, allInventories[i], xCoord + direction.offsetX,yCoord + direction.offsetY, zCoord + direction.offsetZ, direction.ordinal(), hitX, hitY, hitZ);
                                ItemStack playerItem = player.inventory.getStackInSlot(0);
                                
                                if (playerItem == null || !playerItem.isItemEqual(allInventories[i])|| !ItemStack.areItemStackTagsEqual(playerItem, allInventories[i])) {
                                    allInventories[i] = null;
                                }
                                break;
                                
                            }
                            
                        } else {
                            
                            ItemStack result = allInventories[i].useItemRightClick(worldObj, player);
                            
                            if (result == null || !result.isItemEqual(allInventories[i])
                                    || !ItemStack.areItemStackTagsEqual(result, allInventories[i])) {
                                allInventories[i] = result;
                                break;
                            } else {
                                
                                player.theItemInWorldManager.activateBlockOrUseItem(player, worldObj, allInventories[i], xCoord + direction.offsetX,
                                        yCoord + direction.offsetY, zCoord + direction.offsetZ, getFacingDirection().ordinal(), hitX, hitY, hitZ);
                                
                                ItemStack playerItem = player.inventory.getStackInSlot(0);
                                if (playerItem == null || !playerItem.isItemEqual(allInventories[i])|| !ItemStack.areItemStackTagsEqual(playerItem, allInventories[i])) {
                                    allInventories[i] = null;
                                }
                                decrStackSize(i, 1);
                                break;
                            }
                        }
                    }
                }
            }
            
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            for (int k = 0; k < player.inventory.getSizeInventory(); k++) {
                player.inventory.setInventorySlotContents(k, null);
            }
            player.setDead();
            worldObj.removeEntity(player);
        }
    }
    
    public int getAvailableSlot() {
    
        for (int i = 0; i < allInventories.length; i++) {
            
            if (!(allInventories == null)) {
                if (allInventories[i] == null) { return i; }
            }
        }
        return 0;
    }
    
    @Override
    public void onBlockNeighbourChanged() {
    
        super.onBlockNeighbourChanged();
        if (!worldObj.isRemote) {
            setRedstoneSignal(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord));
        }
        
    }
    
    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void readFromNBT(NBTTagCompound tCompound) {
    
        super.readFromNBT(tCompound);
        
        for (int i = 0; i < 9; i++) {
            NBTTagCompound tc = tCompound.getCompoundTag("inventory" + i);
            allInventories[i] = ItemStack.loadItemStackFromNBT(tc);
        }
    }
    
    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public void writeToNBT(NBTTagCompound tCompound) {
    
        super.writeToNBT(tCompound);
        
        for (int i = 0; i < 9; i++) {
            if (allInventories[i] != null) {
                NBTTagCompound tc = new NBTTagCompound();
                allInventories[i].writeToNBT(tc);
                tCompound.setTag("inventory" + i, tc);
            }
        }
    }
    
    @Override
    public int getSizeInventory() {
    
        return allInventories.length;
    }
    
    @Override
    public ItemStack getStackInSlot(int i) {
    
        return this.allInventories[i];
    }
    
    @Override
    public ItemStack decrStackSize(int slot, int amount) {
    
        // this needs to be side aware as well
        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null) {
            if (itemStack.stackSize <= amount) {
                setInventorySlotContents(slot, null);
            } else {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        
        return itemStack;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
    
        return getStackInSlot(i);
    }
    
    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
    
        this.allInventories[i] = itemStack;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    @Override
    public String getInventoryName() {
    
        return "tile.deployer.name";
    }
    
    @Override
    public boolean hasCustomInventoryName() {
    
        return true;
    }
    
    @Override
    public int getInventoryStackLimit() {
    
        return 64;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
    
        return true;
    }
    
    @Override
    public void openInventory() {
    
    }
    
    @Override
    public void closeInventory() {
    
    }
    
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
    
        return true;
    }
    
    @Override
    public List<ItemStack> getDrops() {
    
        List<ItemStack> drops = super.getDrops();
        for (ItemStack stack : allInventories)
            if (stack != null) drops.add(stack);
        return drops;
    }
    
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
    
        ForgeDirection direction = ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
        
        if (var1 == direction.ordinal()) { return new int[] {}; }
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    }
    
    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
    
        for (int i : this.getAccessibleSlotsFromSide(side)) {
            if (slot == i) { return true; }
        }
        return false;
    }
    
    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
    
        for (int i : this.getAccessibleSlotsFromSide(side)) {
            if (slot == i) { return true; }
        }
        return false;
    }
    
}
