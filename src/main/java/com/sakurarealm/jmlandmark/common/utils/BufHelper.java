package com.sakurarealm.jmlandmark.common.utils;

import io.netty.buffer.ByteBuf;

public class BufHelper {

    public static String readStringFromBuffer(ByteBuf buf) {
        int length = buf.readInt();
        byte[] tmp = new byte[length];
        buf.readBytes(tmp);

        return new String(tmp);
    }

    public static void writeStringToBuffer(ByteBuf buf, String string) {
        byte[] bytes = string.getBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    public static byte[] readImageFromBuffer(ByteBuf buf) {
        int dataLength = buf.readInt();
        byte[] imageData = new byte[dataLength];
        buf.readBytes(imageData);
        return imageData;
    }

    public static void writeImageToBuffer(ByteBuf buf, byte[] image) {
        buf.writeInt(image.length);
        buf.writeBytes(image);
    }
}
