package net.quetzi.bluepower.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.quetzi.bluepower.containers.ContainerAssembler;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier2.TileEntityAssembler;

public class GuiAssembler extends GuiBase {

	private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/assembler.png");
	
	public TileEntityAssembler assembler;
	
	public GuiAssembler(InventoryPlayer invP, TileEntityAssembler tile) {
		super(new ContainerAssembler(invP, tile), resLoc);
		this.ySize = 195;
		this.assembler = tile;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		super.drawGuiContainerBackgroundLayer(f, i, j);
		
		int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
		
		this.drawTexturedModalRect(x + 6 + this.addXAmount(), y + 16 + this.addYAmount(), 176, 0, 20, 20);
	}

	private int addYAmount() {
		if(this.assembler.currentSlot <= 7) {
			return 0;
		} else {
			return 18;
		}
	}

	private int addXAmount() {
		if(this.assembler.currentSlot <= 7) {
			return 18 * this.assembler.currentSlot;
		} else {
			int i = this.assembler.currentSlot - 8;
			return 18 * i;
		}
	}

}
