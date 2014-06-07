package net.quetzi.bluepower.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier1.TileAlloyFurnace;

import java.util.Random;

public class BlockAlloyFurnace extends BlockContainerBase
{

    private IIcon textureTop;
    private IIcon textureBottom;
    private IIcon textureSide;
    private IIcon textureFrontOn;
    private IIcon textureFrontOff;

    public BlockAlloyFurnace()
    {
        super(Material.rock);
        this.setBlockName(Refs.ALLOYFURNACE_NAME);
        //This might not be needed actually.
        this.setBlockTextureName(Refs.ALLOYFURNACE_NAME + "_front");

    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        ForgeDirection s = ForgeDirection.getOrientation(side);
        //If is facing

        if ((meta & 7) == side) {
            //Do some bitmasking
            if ((meta & 8) != 0) {
                return this.textureFrontOn;
            } else {
                return this.textureFrontOff;
            }
        }
        switch (s) {
            case UP:
                return textureTop;
            case DOWN:
                return textureBottom;
            case EAST:
            case NORTH:
            case SOUTH:
            case WEST:
            case UNKNOWN:
                return textureSide;
            default:
                break;

        }
        return null;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return true;
    }

    @SuppressWarnings("cast")
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rnd)
    {
        int metadata = world.getBlockMetadata(x, y, z);
        if ((metadata & 8) != 0) {
            int l = world.getBlockMetadata(x, y, z) & 7;
            float f = (float) x + 0.5F;
            float f1 = (float) y + 0.0F + rnd.nextFloat() * 6.0F / 16.0F;
            float f2 = (float) z + 0.5F;
            float f3 = 0.52F;
            float f4 = rnd.nextFloat() * 0.6F - 0.3F;

            if (l == 4) {
                world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 5) {
                world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 2) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
            } else if (l == 3) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    //Not sure if you need this function.
    public Item getItemDropped(int p_149650_1_, Random random, int p_149650_3_)
    {
        return Item.getItemFromBlock(BPBlocks.alloy_furnace);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.textureTop = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_top");
        this.textureBottom = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_bottom");
        this.textureSide = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_side");
        this.textureFrontOn = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_front_on");
        this.textureFrontOff = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_front_off");
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        int metadata = world.getBlockMetadata(x, y, z);
        if ((metadata & 8) != 0) {
            return 13;
        }
        return 0;
    }

    @Override
    protected Class<? extends TileEntity> getTileEntity()
    {
        return TileAlloyFurnace.class;
    }

    @Override
    public GuiIDs getGuiID()
    {
        return GuiIDs.ALLOY_FURNACE;
    }
}
