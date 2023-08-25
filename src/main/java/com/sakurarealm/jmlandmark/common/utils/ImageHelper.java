package com.sakurarealm.jmlandmark.common.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageHelper {

    public static byte[] imageToBytes(String imagePath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] bytes = baos.toByteArray();
            baos.close();
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage imageFromBytes(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static BufferedImage imageToBufferedImage(String imagePath) throws IOException {
        return ImageIO.read(new File(imagePath));
    }

    public static void saveImage(File imageFile, BufferedImage image) throws IOException {
        ImageIO.write(image, "png", imageFile);
    }

}
