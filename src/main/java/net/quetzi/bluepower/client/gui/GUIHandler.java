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

package net.quetzi.bluepower.client.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.quetzi.bluepower.containers.*;
import net.quetzi.bluepower.containers.inventorys.InventoryItem;
import net.quetzi.bluepower.items.ItemCanvasBag;
import net.quetzi.bluepower.items.ItemFloppyDisk;
import net.quetzi.bluepower.items.ItemScrewdriver;
import net.quetzi.bluepower.items.ItemSeedBag;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.tileentities.tier1.*;
import net.quetzi.bluepower.tileentities.tier2.TileAssembler;
import net.quetzi.bluepower.tileentities.tier2.TileSortingMachine;
import net.quetzi.bluepower.tileentities.tier3.*;

public class GUIHandler implements IGuiHandler {
    
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    
        // This function creates a container
        TileEntity ent = world.getTileEntity(x, y, z);
        // ID is the GUI ID
        switch (GuiIDs.values()[ID]) {
            case ALLOY_FURNACE:
                return new ContainerAlloyFurnace(player.inventory, (TileAlloyFurnace) ent);
            case BUFFER:
                return new ContainerBuffer(player.inventory, (TileBuffer) ent);
            case SORTING_MACHINE:
                return new ContainerSortingMachine(player.inventory, (TileSortingMachine) ent);
            case SEEDBAG:
                if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemSeedBag) { return new ContainerSeedBag(
                        player.getCurrentEquippedItem(), player.inventory, InventoryItem.getItemInventory(player, player.getCurrentEquippedItem(),
                                "Seed Bag", 9)); }
                break;
            case CANVAS_BAG:
                if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemCanvasBag) { return new ContainerCanvasBag(
                        player.getCurrentEquippedItem(), player.inventory, InventoryItem.getItemInventory(player, player.getCurrentEquippedItem(),
                                "Canvas Bag", 27)); }
                break;
            case CPU:
            	return new ContainerCPU(player.inventory, (TileCPU) ent);
            case MONITOR:
            	if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemScrewdriver) {
            		return null;
            	}
            	return new ContainerMonitor(player.inventory, (TileMonitor) ent);
            case DISK_DRIVE: // FIXME: this conditional will always be false (for fabricator77)
            	if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemScrewdriver
            		&& player.getCurrentEquippedItem().getItem() instanceof ItemFloppyDisk) {
            		return null;
            	}
            	return new ContainerDiskDrive(player.inventory, (TileDiskDrive) ent);
            case IO_EXPANDER:
            	if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemScrewdriver) {
            		return null;
            	}
            	return new ContainerIOExpander(player.inventory, (TileIOExpander) ent);
            	
            case REDBUS_ID:
            	return new ContainerRedbusID(player.inventory, (IRedBusWindow) ent);
            	
            case KINETICGENERATOR_ID:
            	return new ContainerKinect(player.inventory, (TileKinectGenerator) ent);
            case DEPLOYER_ID:
            	return new ContainerDeployer(player.inventory, (TileDeployer) ent);
            case RELAY_ID:
                return new ContainerRelay(player.inventory, (TileRelay) ent);
            case EJECTOR_ID:
                return new ContainerEjector(player.inventory, (TileEjector) ent);
            case ASSEMBLER_ID: 
            	return new ContainerAssembler(player.inventory, (TileAssembler) ent);
            default:
                break;
        }
        return null;
    }
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    
        TileEntity ent = world.getTileEntity(x, y, z);
        // ID is the GUI ID
        switch (GuiIDs.values()[ID]) {
            case ALLOY_FURNACE:
                return new GuiAlloyFurnace(player.inventory, (TileAlloyFurnace) ent);
            case BUFFER:
                return new GuiBuffer(player.inventory, (TileBuffer) ent);
            case SORTING_MACHINE:
                return new GuiSortingMachine(player.inventory, (TileSortingMachine) ent);
            case SEEDBAG:
                if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemSeedBag) { return new GuiSeedBag(
                        player.getCurrentEquippedItem(), player.inventory, InventoryItem.getItemInventory(player, player.getCurrentEquippedItem(),
                                "Seed Bag", 9)); }
            case CANVAS_BAG:
                if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemCanvasBag) { return new GuiCanvasBag(
                        player.getCurrentEquippedItem(), player.inventory, InventoryItem.getItemInventory(player, player.getCurrentEquippedItem(),
                                "Canvas Bag", 27)); }
                break;
            case CPU:
            	return new GuiCPU(player.inventory, (TileCPU) ent);
            case MONITOR:
            	if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemScrewdriver) {
            		return null;
            	}
            	return new GuiMonitor(player.inventory, (TileMonitor) ent);
            case DISK_DRIVE: // FIXME: this conditional will always be false (for fabricator77)
            	if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemScrewdriver
            	&& player.getCurrentEquippedItem().getItem() instanceof ItemFloppyDisk) {
            		return null;
            	}
            	return new GuiDiskDrive(player.inventory, (TileDiskDrive) ent);
            case IO_EXPANDER:
            	if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemScrewdriver) {
            		return null;
            	}
            	return new GuiIOExpander(player.inventory, (TileIOExpander) ent);
            case REDBUS_ID:
            	return new GuiRedbusID(player.inventory, (IRedBusWindow) ent);
            case KINETICGENERATOR_ID:
            	return new GuiKinect(player.inventory, (TileKinectGenerator) ent);
            case DEPLOYER_ID:
            	return new GuiDeployer(player.inventory, (TileDeployer) ent);
            case RELAY_ID:
                return new GuiRelay(player.inventory, (TileRelay) ent);
            case EJECTOR_ID:
                return new GuiEjector(player.inventory, (TileEjector) ent);
            case ASSEMBLER_ID:
            	return new GuiAssembler(player.inventory, (TileAssembler) ent);
            default:
                break;
        }
        return null;
    }
}