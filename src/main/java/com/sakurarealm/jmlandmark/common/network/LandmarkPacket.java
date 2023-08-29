package com.sakurarealm.jmlandmark.common.network;

import com.sakurarealm.jmlandmark.JMLandmarkMod;
import com.sakurarealm.jmlandmark.client.ClientLandmarkManager;
import com.sakurarealm.jmlandmark.client.ClientProxy;
import com.sakurarealm.jmlandmark.common.landmark.Landmark;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class LandmarkPacket implements IMessage {

    private Landmark landmark;
    private boolean adding;

    public LandmarkPacket() {
        landmark = new Landmark();
    }

    public LandmarkPacket(Landmark landmark, boolean adding) {
        this.landmark = landmark;
        this.adding = adding;
    }

    public Landmark getLandmark() {
        return landmark;
    }

    public boolean isAdding() {
        return adding;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        landmark.fromBytes(buf);
        adding = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        landmark.toBytes(buf);
        buf.writeBoolean(adding);
    }

    public static class LandmarkHandler implements IMessageHandler<LandmarkPacket, IMessage> {
        @Override
        public IMessage onMessage(LandmarkPacket message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    ClientProxy proxy = (ClientProxy) JMLandmarkMod.getProxy();
                    ClientLandmarkManager manager = (ClientLandmarkManager) proxy.getLandMarkManager();
                    if (message.isAdding()) {
                        manager.addLandmark(message.getLandmark());
                    } else {
                        manager.removeLandmark(message.getLandmark().getName());
                    }
                });
            }

            return null;
        }
    }
}
