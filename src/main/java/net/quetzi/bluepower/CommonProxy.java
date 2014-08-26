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

package net.quetzi.bluepower;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.quetzi.bluepower.references.Refs;

public class CommonProxy {
    
	public static EntityPlayer fakePlayer;
	
    public void init() {
    
    }
    
    public void initRenderers() {
    
    }
    
    public EntityPlayer getFakePlayer(World world) {
    	if(fakePlayer == null) {
    		fakePlayer = FakePlayerFactory.get((WorldServer) world, new GameProfile(Refs.MODID, "Bluepower"));
    	}
    	
    	return fakePlayer;
    }
    
    public EntityPlayer getPlayer() {
    
        return null;
    }
    
    public boolean isSneakingInGui() {
    
        return false;
    }
}
