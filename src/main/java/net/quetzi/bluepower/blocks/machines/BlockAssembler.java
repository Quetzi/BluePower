package net.quetzi.bluepower.blocks.machines;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.blocks.BlockContainerBase;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier2.TileEntityAssembler;

public class BlockAssembler extends BlockContainerBase {

	public IIcon front;
	public IIcon back;
	public IIcon active;
	
	public BlockAssembler() {
		super(Material.iron);
		this.setBlockName(Refs.ASSEMBLER_NAME);
		this.setCreativeTab(CustomTabs.tabBluePowerMachines);
	}

	@Override
	protected Class<? extends TileEntity> getTileEntity() {
		return TileEntityAssembler.class;
	}

	@Override
	public GuiIDs getGuiID() {
		return GuiIDs.ASSEMBLER_ID;
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		ForgeDirection orientation = ForgeDirection.getOrientation(meta);
		
		if(side == orientation.ordinal()) {
			return this.front;
		} else if(side == orientation.getOpposite().ordinal()) {
			return this.back;
		}
		
		return this.blockIcon;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) {
		
		//TODO : Add the animated textures
		
		String name = getUnlocalizedName().substring(5);
		
		this.blockIcon = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + name + "_side_0");
		this.active = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + name + "_front_active");
		this.front = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + name + "_front");
		this.back = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + name + "_bottom");
	}
}
