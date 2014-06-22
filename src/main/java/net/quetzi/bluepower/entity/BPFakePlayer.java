package net.quetzi.bluepower.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.quetzi.bluepower.references.Refs;

import com.mojang.authlib.GameProfile;

/**
 * 
 * @author modmuss <3
 *
 * @Credit Buildcraft 
 */
public class BPFakePlayer{



	

	

	public static EntityPlayer fakePlayer;

	public static String playerName() {
		return "";
	}

	public static EntityPlayer createNewPlayer(World world) {
		EntityPlayer player = new EntityPlayer(world, new GameProfile(null,
				"[FakePlayerName]")) {
			@Override
			public void addChatMessage(IChatComponent var1) {
			}

			@Override
			public boolean canCommandSenderUseCommand(int var1, String var2) {
				return false;
			}

			@Override
			public ChunkCoordinates getPlayerCoordinates() {
				return null;
			}
		};
		return (EntityPlayer) player;
	}

	public static EntityPlayer createNewPlayer(World world, int x, int y, int z) {
		EntityPlayer player = new EntityPlayer(world, new GameProfile(null,
				"[FakePlayerName]")) {
			@Override
			public void addChatMessage(IChatComponent var1) {
			}

			@Override
			public boolean canCommandSenderUseCommand(int var1, String var2) {
				return false;
			}

			@Override
			public ChunkCoordinates getPlayerCoordinates() {
				return null;
			}
		};
		player.posX = x;
		player.posY = y;
		player.posZ = z;
		return (EntityPlayer) player;
	}

	public static EntityPlayer getfakePlayer(World world) {
		if (fakePlayer == null) {
			fakePlayer = createNewPlayer(world);
		} else {
			fakePlayer.worldObj = world;
		}

		return fakePlayer;
	}

	public static EntityPlayer getfakePlayer(World world, int x, int y,
			int z) {
		if (fakePlayer == null) {
			fakePlayer = createNewPlayer(world, x, y, z);
		} else {
			fakePlayer.worldObj = world;
			fakePlayer.posX = x;
			fakePlayer.posY = y;
			fakePlayer.posZ = z;
		}

		return fakePlayer;
	}


}
