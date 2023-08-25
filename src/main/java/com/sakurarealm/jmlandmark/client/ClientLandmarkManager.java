package com.sakurarealm.jmlandmark.client;

import com.sakurarealm.jmlandmark.JMLandmarkMod;
import com.sakurarealm.jmlandmark.common.landmark.Landmark;
import com.sakurarealm.jmlandmark.common.landmark.LandmarkManager;
import com.sakurarealm.jmlandmark.common.network.ImageRequestPacket;
import com.sakurarealm.jmlandmark.common.network.PacketHandler;

import java.util.ArrayList;
import java.util.List;

public class ClientLandmarkManager extends LandmarkManager {

    private List<String> requestingImages = new ArrayList<>();

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loadOrCreate() {

    }

    @Override
    public void addLandmark(Landmark landmark) {
        Landmark landmark1 = landmarkMap.get(landmark.getName());
        if (landmark1 != null) {
            landmark1.hide();
        }
        super.addLandmark(landmark);

        updateLandmarkWithRequest(landmark.getName());
    }

    @Override
    public void removeAllLandMarks() {
        for (Landmark landmark : landmarkMap.values()) {
            landmark.hide();
        }

        super.removeAllLandMarks();
    }

    @Override
    public void removeLandmark(String name) {
        Landmark landmark = landmarkMap.get(name);
        if (landmark != null) {
            landmark.hide();
        }

        super.removeLandmark(name);
    }

    public void requestImageSource(String fileName) {
        if (!requestingImages.contains(fileName)) {
            requestingImages.add(fileName);
            List<String> requestingList = new ArrayList<>();
            requestingList.add(fileName);
            PacketHandler.getInstance().sendToServer(new ImageRequestPacket(requestingList));
        } else {
            JMLandmarkMod.getLogger().info(String.format("Existing ongoing request for image file %s", fileName));
        }
    }

    public void onReceiveImageSource(String fileName) {
        requestingImages.remove(fileName);

        for (Landmark landmark : landmarkMap.values()) {
            if (landmark.getSourceImageName().equals(fileName)) {
                updateLandmark(landmark);
            }
        }
    }

    public void updateLandmark(String name) {
        Landmark landmark = landmarkMap.get(name);
        if (landmark != null) {
            updateLandmark(landmark);
        }
    }

    protected void updateLandmark(Landmark landmark) {
        if (!landmark.show()) {
            JMLandmarkMod.getLogger().warn(
                    String.format("Image source %s not found while updating landmark %s",
                            landmark.getSourceImageName(),
                            landmark.getName())
            );
        }
    }

    public void updateLandmarkWithRequest(String name) {
        Landmark landmark = landmarkMap.get(name);
        if (landmark != null) {
            if (!landmark.show()) {
                requestImageSource(landmark.getSourceImageName());
            }
        }
    }

    @Override
    public void updateAllLandmarks() {
        for (Landmark landmark : landmarkMap.values()) {
            updateLandmark(landmark);
        }
    }
}
