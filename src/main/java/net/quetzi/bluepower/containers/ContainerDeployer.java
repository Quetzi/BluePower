/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.quetzi.bluepower.tileentities.tier1.TileDeployer;

public class ContainerDeployer extends Container {
    
    private final TileDeployer tileDeployer;
    
    public ContainerDeployer(InventoryPlayer invPlayer, TileDeployer deployer) {
    
        bindPlayerInventory(invPlayer);
        this.tileDeployer = deployer;
        
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlotToContainer(new Slot(deployer, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }
    }
    
    protected void bindPlayerInventory(InventoryPlayer invPlayer) {
    
        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18, 142));
        }
        
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer player) {
    
        return tileDeployer.isUseableByPlayer(player);
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
    
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);
        
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if (par2 < 9) {
                if (!this.mergeItemStack(itemstack1, 9, 45, true)) { return null; }
            } else if (!this.mergeItemStack(itemstack1, 0, 9, false)) { return null; }
            
            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }
            
            if (itemstack1.stackSize == itemstack.stackSize) { return null; }
            
            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }
        
        return itemstack;
    }
    
}
