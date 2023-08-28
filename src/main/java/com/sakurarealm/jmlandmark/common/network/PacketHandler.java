package com.sakurarealm.jmlandmark.common.network;

import com.sakurarealm.jmlandmark.JMLandmarkMod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

    private static final PacketHandler INSTANCE = new PacketHandler();

    public static PacketHandler getInstance() {
        return INSTANCE;
    }

    private PacketHandler() {

    }

    private static final SimpleNetworkWrapper HANDLER = new SimpleNetworkWrapper(JMLandmarkMod.MODID);

    public void init() {
        int id = 0;
        HANDLER.registerMessage(ImageSendPacket.ImageSendHandler.class, ImageSendPacket.class, id++, Side.CLIENT);
        HANDLER.registerMessage(ImageRequestPacket.ImageRequestHandler.class, ImageRequestPacket.class, id++, Side.SERVER);
        HANDLER.registerMessage(LandmarkPacket.LandmarkHandler.class, LandmarkPacket.class, id++, Side.CLIENT);
        HANDLER.registerMessage(ImageNotExistPacket.ImageNotExistHandler.class, ImageNotExistPacket.class, id++, Side.CLIENT);

    }

    public void sendTo(EntityPlayerMP player, IMessage toSend) {
        HANDLER.sendTo(toSend, player);
    }

    public void sendToServer(IMessage toSend) {
        HANDLER.sendToServer(toSend);
    }


}
