package com.sakurarealm.jmlandmark.common.network;

import com.sakurarealm.jmlandmark.JMLandmarkMod;
import com.sakurarealm.jmlandmark.client.ClientLandmarkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ImageNotExistPacket implements IMessage {

    String fileName;

    public ImageNotExistPacket() {

    }

    public ImageNotExistPacket(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int length = buf.readInt();
        fileName = new String(buf.readBytes(length).array());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        byte[] bytesName = fileName.getBytes();
        buf.writeInt(bytesName.length);
        buf.writeBytes(bytesName);
    }

    public String getFileName() {
        return fileName;
    }

    public static class ImageNotExistHandler implements IMessageHandler<ImageNotExistPacket, IMessage> {

        @Override
        public IMessage onMessage(ImageNotExistPacket message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    ((ClientLandmarkManager) (JMLandmarkMod.getProxy().getLandMarkManager())).withdrawRequest(
                            message.getFileName()
                    );
                });
            }
            return null;
        }
    }
}
