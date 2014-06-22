package net.quetzi.bluepower.tileentities.tier2;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.tileentities.TileBase;

public class TileAssembler extends TileBase implements ISidedInventory {

	public ItemStack[] inventory = new ItemStack[34];
	public int currentSlot = 0;
	
	@Override
	protected void redstoneChanged(boolean newValue) {
		super.redstoneChanged(newValue);
		if(newValue) {
			//TODO : Change State
			this.useItem();
		}
	}
	
	private void useItem() {
		ForgeDirection orientation = ForgeDirection.getOrientation(this.getBlockMetadata());
		if(this.worldObj.isAirBlock(xCoord + orientation.offsetX, yCoord + orientation.offsetY, zCoord + orientation.offsetZ)) {
			if(inventory[currentSlot] != null) {
				ItemStack target = inventory[currentSlot];
				int itemTargetSlot = 0;
				for(int i = 16;i < this.inventory.length;i++) {
					if(this.inventory[i] != null) {
						if(this.inventory[i].getItem() == target.getItem() && this.inventory[i].getItemDamage() == target.getItemDamage()) {
							itemTargetSlot = i;
							break;
						}
					}
				}
				
				if(itemTargetSlot == 0) {
					return;
				}
				
				if(target.getItem() instanceof ItemBlock) {
					if(!worldObj.isRemote) {
						((ItemBlock) this.inventory[itemTargetSlot].getItem()).placeBlockAt(this.inventory[itemTargetSlot], BluePower.proxy.getFakePlayer(worldObj), worldObj, xCoord + orientation.offsetX, yCoord + orientation.offsetY, zCoord + orientation.offsetZ, 0, 0, 0, 0, this.inventory[itemTargetSlot].getItemDamage());
					}
					
					//TODO : Use Items
					
					
					this.decrStackSize(itemTargetSlot, 1);
					if(this.currentSlot >= 15) {
						this.currentSlot = 0;
					} else {
						this.currentSlot++;
					}
					
				}
			} else {
				if(this.currentSlot >= 15) {
					this.currentSlot = 0;
				} else {
					this.currentSlot++;
				}
			}
		}
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return this.inventory[var1];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		ItemStack stack = getStackInSlot(i);
	    
	    if (stack != null) {
	    	if (stack.stackSize <= j) {
	    		setInventorySlotContents(i, null);
	    	} 
	    	else {
	    		stack = stack.splitStack(j);
	        
	    		if (stack.stackSize == 0) {
	    			setInventorySlotContents(i, null);
	    		}
	    	}
	    }
	    
	    return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return this.getStackInSlot(var1);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.inventory[i] = itemstack;
	    
	    if ((itemstack != null) && (itemstack.stackSize > getInventoryStackLimit())) {
	    	itemstack.stackSize = getInventoryStackLimit();
	    }
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tCompound) {
		super.readFromNBT(tCompound);
		this.currentSlot = tCompound.getInteger("currentSlot");
		for (int i = 0; i < this.inventory.length; i++) {
			NBTTagCompound tc = tCompound.getCompoundTag("inventory" + i);
			inventory[i] = ItemStack.loadItemStackFromNBT(tc);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tCompound) {
		super.writeToNBT(tCompound);
		tCompound.setInteger("currentSlot", this.currentSlot);
		for (int i = 0; i < this.inventory.length; i++) {
			if (inventory[i] != null) {
				NBTTagCompound tc = new NBTTagCompound();
				inventory[i].writeToNBT(tc);
	            tCompound.setTag("inventory" + i, tc);
			}
		}
	}

	@Override
	public String getInventoryName() {
		return "tile.assembler.name";
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
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {
		
	}
	
	@Override
	public List<ItemStack> getDrops() {
		List<ItemStack> stacks = super.getDrops();
		
		for(int i = 16;i < this.inventory.length;i++) {
			if(this.inventory[i] != null) {
				stacks.add(this.inventory[i]);
			}
		}
		
		return stacks;
	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return new int[] {16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStack, int side) {

		for (int i : this.getAccessibleSlotsFromSide(side)) {
			if (slot == i) { 
				return true; 
			}
	    }
	    return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemStack, int side) {

		for (int i : this.getAccessibleSlotsFromSide(side)) {
			if (slot == i) { 
				return true; 
			}
	    }
	    return false;
	}

}
