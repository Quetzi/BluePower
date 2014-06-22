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
package net.quetzi.bluepower.client.gui.widget;

import java.util.List;

import net.minecraft.client.gui.Gui;
import net.quetzi.bluepower.references.Refs;

import org.lwjgl.opengl.GL11;

/**
 * @author fabricator77
 */
public class WidgetDipSwitch extends BaseWidget {
    
    public WidgetDipSwitch(int id, int x, int y) {
    
        super(id, x, y, 8, 16, Refs.MODID+":textures/gui/widgets/redbus_dipswitch.png");
    }
    
    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
    
        if (button == 0) {
            if (++value > 1) value = 0;
        }
        super.onMouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public void render(int mouseX, int mouseY) {
    
        super.render(mouseX, mouseY);
    }
    
    @Override
    protected int getTextureU() {
   
        return 0;
    }
    @Override
    protected int getTextureV() {
    
        return value == 0 ? 0 : 8;
    }
    @Override
    public void addTooltip(List<String> curTooltip, boolean shiftPressed) {
    
        if (value == 1) {
            curTooltip.add("gui.widget.dipswitch.on");
        } else {
            curTooltip.add("gui.widget.dipswitch.off");
        }
    }
    
}
