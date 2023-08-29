package com.sakurarealm.jmlandmark.common.network;

import com.sakurarealm.jmlandmark.JMLandmarkMod;
import com.sakurarealm.jmlandmark.common.utils.ImageHelper;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ImageRequestPacket implements IMessage {
    private List<String> imageNames;

    public ImageRequestPacket() {
    }

    public ImageRequestPacket(List<String> imageNames) {
        this.imageNames = imageNames;
    }

    /**
     * Read all requesting images from buffer
     *
     * @param buf Bytes buffer
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        int numImages = buf.readInt();
        imageNames = new ArrayList<>();

        for (int i = 0; i < numImages; i++) {
            int length = buf.readInt();
            String imageName = new String(buf.readBytes(length).array());
            imageNames.add(imageName);
        }
    }

    /**
     * Write all image names to the byte buffer
     *
     * @param buf Byte buffer
     */
    @Override
    public void toBytes(ByteBuf buf) {
        // Write the number of requesting images
        buf.writeInt(imageNames.size());
        // Write all names
        for (String imageName : imageNames) {
            byte[] nameBytes = imageName.getBytes();
            buf.writeInt(nameBytes.length);
            buf.writeBytes(nameBytes);
        }
    }

    public List<String> getImageNames() {
        return imageNames;
    }

    public static class ImageRequestHandler implements IMessageHandler<ImageRequestPacket, IMessage> {

        @Override
        public IMessage onMessage(ImageRequestPacket message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                // Load and serialize all the images to send
                ConcurrentHashMap<String, byte[]> images = new ConcurrentHashMap<>();
                for (String imageName : message.getImageNames()) {
                    File imageFile = JMLandmarkMod.getProxy().parseImageSource(imageName);
                    if (imageFile.exists() && imageFile.isFile()) {
                        byte[] imageData = ImageHelper.imageToBytes(imageFile.getAbsolutePath());
                        images.put(imageName, imageData);
                    } else {
                        // Send image not found to the player
                        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                            PacketHandler.getInstance().sendTo(ctx.getServerHandler().player, new ImageNotExistPacket(imageName));
                        });
                    }
                }

                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    // Send the packet to the requesting player
                    ImageSendPacket packet = new ImageSendPacket(images);

                    PacketHandler.getInstance().sendTo(ctx.getServerHandler().player, packet);
                });

            }

            return null;
        }
    }
}
