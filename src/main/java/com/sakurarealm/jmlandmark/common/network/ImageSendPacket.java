package com.sakurarealm.jmlandmark.common.network;

import com.sakurarealm.jmlandmark.JMLandmarkMod;
import com.sakurarealm.jmlandmark.client.ClientLandmarkManager;
import com.sakurarealm.jmlandmark.common.utils.BufHelper;
import com.sakurarealm.jmlandmark.common.utils.ImageHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImageSendPacket implements IMessage {

    private final Map<String, byte[]> images;


    public ImageSendPacket() {
        images = new ConcurrentHashMap<>();
    }

    public ImageSendPacket(Map<String, byte[]> images) {
        this.images = images;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int numImages = buf.readInt();

        for (int i = 0; i < numImages; i++) {
            String imageName = BufHelper.readStringFromBuffer(buf);
            images.put(imageName, BufHelper.readImageFromBuffer(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(images.size());

        for (Map.Entry<String, byte[]> entry : images.entrySet()) {
            // Write image name
            BufHelper.writeStringToBuffer(buf, entry.getKey());
            // Write RGBA image bytes
            BufHelper.writeImageToBuffer(buf, entry.getValue());
        }

    }

    public Map<String, byte[]> getImages() {
        return images;
    }

    public static class ImageSendHandler implements IMessageHandler<ImageSendPacket, IMessage> {

        @Override
        public IMessage onMessage(ImageSendPacket message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                try { // Write the image files
                    for (Map.Entry<String, byte[]> entry : message.getImages().entrySet()) {
                        File outputFile = JMLandmarkMod.getProxy().parseImageSource(entry.getKey());
                        BufferedImage image = ImageHelper.imageFromBytes(entry.getValue());
                        ImageHelper.saveImage(outputFile, image);
                    }
                } catch (IOException e) {
                    JMLandmarkMod.getLogger().error(e);
                }
                // Update the Journey map in the main thread.
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    for (String fileName : message.getImages().keySet()) {
                        ((ClientLandmarkManager) (JMLandmarkMod.getProxy().getLandMarkManager())).onReceiveImageSource(
                                fileName
                        );
                    }
                });
            }

            return null;
        }
    }
}
