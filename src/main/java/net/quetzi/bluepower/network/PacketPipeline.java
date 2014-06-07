package net.quetzi.bluepower.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.quetzi.bluepower.network.packets.PacketUpdateAlloyFurnaceGUI;
import net.quetzi.bluepower.references.Refs;

import java.util.*;

/**
 * MineChess
 *
 * @author MineMaarten
 *         www.minemaarten.com
 * @author sirgingalot
 *         some code from: cpw
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * <p/>
 * Packet pipeline class. Directs all registered packet data to be handled by the packets themselves.
 */
@ChannelHandler.Sharable
public class PacketPipeline extends MessageToMessageCodec<FMLProxyPacket, AbstractPacket>
{

    public static PacketPipeline instance;
    private final LinkedList<Class<? extends AbstractPacket>> packets = new LinkedList<Class<? extends AbstractPacket>>();
    private EnumMap<Side, FMLEmbeddedChannel> channels;

    public PacketPipeline()
    {
        registerPacket(PacketUpdateAlloyFurnaceGUI.class);

        channels = NetworkRegistry.INSTANCE.newChannel(Refs.MODID, this);

        // Ensures that packet discriminators are common between server and client by using logical sorting
        Collections.sort(packets, new Comparator<Class<? extends AbstractPacket>>()
        {

            @Override
            public int compare(Class<? extends AbstractPacket> clazz1, Class<? extends AbstractPacket> clazz2)
            {
                int com = String.CASE_INSENSITIVE_ORDER.compare(clazz1.getCanonicalName(), clazz2.getCanonicalName());
                if (com == 0) {
                    com = clazz1.getCanonicalName().compareTo(clazz2.getCanonicalName());
                }

                return com;
            }
        });
    }

    // Method to call from FMLInitializationEvent
    public static void init()
    {
        instance = new PacketPipeline();
    }

    /**
     * Register your packet with the pipeline. Discriminators are automatically set.
     *
     * @param clazz the class to register
     * @return whether registration was successful. Failure may occur if 256 packets have been registered or if the registry already contains this packet
     */
    public boolean registerPacket(Class<? extends AbstractPacket> clazz)
    {
        if (packets.size() >= 256) {
            throw new IllegalArgumentException("Registered more than 256 packets!");
        }

        if (packets.contains(clazz)) {
            throw new IllegalArgumentException("Tried to register a packet that already is registered!");
        }

        packets.add(clazz);
        return true;
    }

    // In line encoding of the packet, including discriminator setting
    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractPacket msg, List<Object> out) throws Exception
    {
        ByteBuf buffer = Unpooled.buffer();
        Class<? extends AbstractPacket> clazz = msg.getClass();
        if (!packets.contains(msg.getClass())) {
            throw new NullPointerException("No Packet Registered for: " + msg.getClass().getCanonicalName());
        }

        byte discriminator = (byte) packets.indexOf(clazz);
        buffer.writeByte(discriminator);
        msg.encodeInto(ctx, buffer);
        FMLProxyPacket proxyPacket = new FMLProxyPacket(buffer.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
        out.add(proxyPacket);
    }

    // In line decoding and handling of the packet
    @Override
    protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception
    {
        ByteBuf payload = msg.payload();
        byte discriminator = payload.readByte();
        Class<? extends AbstractPacket> clazz = packets.get(discriminator);
        if (clazz == null) {
            throw new NullPointerException("No packet registered for discriminator: " + discriminator);
        }

        AbstractPacket pkt = clazz.newInstance();
        pkt.decodeInto(ctx, payload.slice());

        EntityPlayer player;
        switch (FMLCommonHandler.instance().getEffectiveSide()) {
            case CLIENT:
                player = getClientPlayer();
                pkt.handleClientSide(player);
                break;

            case SERVER:
                INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
                player = ((NetHandlerPlayServer) netHandler).playerEntity;
                pkt.handleServerSide(player);
                break;

            default:
        }

        out.add(pkt);
    }

    @SideOnly(Side.CLIENT)
    private EntityPlayer getClientPlayer()
    {
        return Minecraft.getMinecraft().thePlayer;
    }

    /**
     * Send this message to everyone.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     */
    public void sendToAll(AbstractPacket message)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channels.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to the specified player.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     * @param player  The player to send it to
     */
    public void sendTo(AbstractPacket message, EntityPlayerMP player)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channels.get(Side.SERVER).writeAndFlush(message);
    }

    public void sendToAllAround(LocationIntPacket message, World world, double distance)
    {
        sendToAllAround(message, message.getTargetPoint(world, distance));
    }

    public void sendToAllAround(LocationIntPacket message, World world)
    {
        sendToAllAround(message, message.getTargetPoint(world));
    }

    public void sendToAllAround(LocationDoublePacket message, World world)
    {
        sendToAllAround(message, message.getTargetPoint(world));
    }

    /**
     * Send this message to everyone within a certain range of a point.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     * @param point   The {@link cpw.mods.fml.common.network.NetworkRegistry.TargetPoint} around which to send
     */
    public void sendToAllAround(AbstractPacket message, NetworkRegistry.TargetPoint point)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        channels.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to everyone within the supplied dimension.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message     The message to send
     * @param dimensionId The dimension id to target
     */
    public void sendToDimension(AbstractPacket message, int dimensionId)
    {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        channels.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to the server.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     */
    public void sendToServer(AbstractPacket message)
    {
        channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channels.get(Side.CLIENT).writeAndFlush(message);
    }
}