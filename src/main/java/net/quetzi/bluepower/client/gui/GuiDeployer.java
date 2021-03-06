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
 *     
 *     @author Quetzi
 */

package net.quetzi.bluepower.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.quetzi.bluepower.containers.ContainerDeployer;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier1.TileDeployer;

public class GuiDeployer extends GuiBase {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/deployer.png");
    private final TileDeployer deployer;

    public GuiDeployer(InventoryPlayer invPlayer, TileDeployer deployer) {

        super(new ContainerDeployer(invPlayer, deployer), resLoc);
        this.deployer = deployer;
        this.ySize = 166;
    }
}
