package com.sakurarealm.jmlandmark.common.landmark;

import com.sakurarealm.jmlandmark.JMLandmarkMod;
import com.sakurarealm.jmlandmark.client.plugin.LandmarkPlugin;
import com.sakurarealm.jmlandmark.common.utils.MarkerOverlayFactory;
import io.netty.buffer.ByteBuf;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.display.MarkerOverlay;
import journeymap.client.api.model.MapImage;
import net.minecraft.util.math.BlockPos;
import org.json.JSONException;
import org.json.JSONObject;

public class Landmark {

    private String name, hoverText;
    private ImageSource imageSource;
    private BlockPos pos;
    private MarkerOverlay overlay;

    public Landmark() {

    }

    public Landmark(String name, BlockPos pos, ImageSource imageSource, String hoverText) {
        this.name = name;
        this.imageSource = imageSource;
        this.pos = pos;
        this.hoverText = hoverText;
    }

    public boolean show() {
        if (imageSource == null) {
            JMLandmarkMod.getLogger().error("Landmark image source haven't been initialized!");
            return false;
        }

        IClientAPI clientAPI = LandmarkPlugin.getInstance().getClientAPI();
        MapImage mapImage = imageSource.getMapImage();

        if (mapImage == null) { // If the map image is not validate I.e. missing image
            return false;
        }

        if (overlay != null) {
            clientAPI.remove(overlay);
        }

        overlay = MarkerOverlayFactory.create(clientAPI, name, hoverText, pos, mapImage);

        try {
            clientAPI.show(overlay);
            return true;
        } catch (Exception e) {
            JMLandmarkMod.getLogger().warn(e.toString());
            return false;
        }
    }

    public void hide() {
        IClientAPI clientAPI = LandmarkPlugin.getInstance().getClientAPI();

        if (overlay != null) {
            clientAPI.remove(overlay);
        }
    }

    public String getName() {
        return name;
    }

    public BlockPos getPos() {
        return pos;
    }

    public String getSourceImageName() {
        return imageSource.getFileName();
    }

    public void setOnClickListener(Runnable runnable) {
        if (overlay.getOverlayListener() instanceof MarkerOverlayFactory.MarkerListener) {
            ((MarkerOverlayFactory.MarkerListener) overlay.getOverlayListener()).setOnClickListener(runnable);
        } else {
            JMLandmarkMod.getLogger().error("Overlay listener is not of type MarkerListener!");
        }

    }

    public void toBytes(ByteBuf buf) {
        byte[] bytesName = name.getBytes();
        buf.writeInt(bytesName.length);
        buf.writeBytes(bytesName);

        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());

        byte[] bytesText = hoverText.getBytes();
        buf.writeInt(bytesText.length);
        buf.writeBytes(bytesText);

        imageSource.toBytes(buf);
    }

    public void fromBytes(ByteBuf buf) {
        int nameLength = buf.readInt();
        name = new String(buf.readBytes(nameLength).array());

        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());

        int textLength = buf.readInt();
        hoverText = new String(buf.readBytes(textLength).array());

        imageSource = new ImageSource();
        imageSource.fromBytes(buf);
    }

    public void toJson(JSONObject obj) throws JSONException {
        obj.put("name", name);

        obj.put("x", pos.getX());
        obj.put("y", pos.getY());
        obj.put("z", pos.getZ());

        obj.put("hoverText", hoverText);

        JSONObject imageSourceObject = new JSONObject();
        imageSource.toJson(imageSourceObject);
        obj.put("imageSource", imageSourceObject);
    }

    public void fromJson(JSONObject obj) throws JSONException {
        name = obj.getString("name");

        int x = obj.getInt("x");
        int y = obj.getInt("y");
        int z = obj.getInt("z");
        pos = new BlockPos(x, y, z);

        hoverText = obj.getString("hoverText");

        imageSource = new ImageSource();
        imageSource.fromJson((JSONObject) obj.get("imageSource"));
    }

}