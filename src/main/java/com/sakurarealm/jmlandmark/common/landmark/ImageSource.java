package com.sakurarealm.jmlandmark.common.landmark;

import com.sakurarealm.jmlandmark.JMLandmarkMod;
import com.sakurarealm.jmlandmark.common.utils.BufHelper;
import com.sakurarealm.jmlandmark.common.utils.ImageHelper;
import io.netty.buffer.ByteBuf;
import journeymap.client.api.model.MapImage;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageSource {

    public static final String CLIENT_IMAGES_CACHE_DIR = "journeymap/plugin/landmark_caches/images/";

    private String name;
    private File image;
    private String fileName;
    private int offsetX, offsetY, width, height, color;
    private double opacity;

    public ImageSource() {

    }

    public ImageSource(String name, String fileName, int offsetX, int offsetY, int width, int height) {
        this.name = name;
        this.image = JMLandmarkMod.getProxy().parseImageSource(fileName);
        this.fileName = fileName;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.color = 0xffffff;
        this.opacity = 1.f;
    }

    public ImageSource(String name, String fileName, int offsetX, int offsetY, int width, int height, int color, double opacity) {
        this(name, fileName, offsetX, offsetY, width, height);
        this.color = color;
        this.opacity = opacity;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isValidate() {
        return fileName != null && image != null && image.exists() && image.isFile();
    }

    public BufferedImage getBufferedImage() {
        if (!isValidate()) {
            return null;
        }
        try {
            return ImageHelper.imageToBufferedImage(image.getAbsolutePath());
        } catch (IOException e) {
            return null;
        }

    }

    public MapImage getMapImage() {
        BufferedImage img = getBufferedImage();
        if (img == null)
            return null;

        return new MapImage(img, offsetX, offsetY, width, height, color, (float) opacity);
    }

    public void toBytes(ByteBuf buf) {
        // Write file name
        BufHelper.writeStringToBuffer(buf, fileName);

        // Write offsets and scales
        buf.writeInt(offsetX);
        buf.writeInt(offsetY);
        buf.writeInt(width);
        buf.writeInt(height);

        // Write color and opacity
        buf.writeInt(color);
        buf.writeDouble(opacity);
    }

    public void fromBytes(ByteBuf buf) {
        fileName = BufHelper.readStringFromBuffer(buf);
        image = JMLandmarkMod.getProxy().parseImageSource(fileName);

        offsetX = buf.readInt();
        offsetX = buf.readInt();
        width = buf.readInt();
        height = buf.readInt();
        color = buf.readInt();
        opacity = buf.readInt();
    }

    public void toJson(JSONObject obj) throws JSONException {
        obj.put("fileName", fileName);
        obj.put("offsetX", offsetX);
        obj.put("offsetY", offsetY);
        obj.put("width", width);
        obj.put("height", height);
        obj.put("color", color);
        obj.put("opacity", opacity);
    }

    public void fromJson(JSONObject obj) throws JSONException {
        fileName = obj.getString("fileName");
        offsetX = obj.getInt("offsetX");
        offsetY = obj.getInt("offsetY");
        width = obj.getInt("width");
        height = obj.getInt("height");
        color = obj.getInt("color");
        opacity = obj.getDouble("opacity");
    }

}
