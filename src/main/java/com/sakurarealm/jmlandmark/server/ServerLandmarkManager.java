package com.sakurarealm.jmlandmark.server;

import com.sakurarealm.jmlandmark.JMLandmarkMod;
import com.sakurarealm.jmlandmark.common.landmark.Landmark;
import com.sakurarealm.jmlandmark.common.landmark.LandmarkManager;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServerLandmarkManager extends LandmarkManager {
    @Override
    public void init() {
        super.init();
        loadOrCreate();
    }

    protected void saveLandmark() {

    }

    @Override
    public void loadOrCreate() {
        createExampleLandmark();
        // Add all landmarks to the manager
        landmarkMap.clear();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(landmarksDir.toPath(), "*.json")) {
            for (Path entry : stream) {
                String content = new String(Files.readAllBytes(entry));
                JSONObject landmarkObject = new JSONObject(content);
                Landmark landmark = new Landmark();
                landmark.fromJson(landmarkObject);
                addLandmark(landmark);
            }
        } catch (IOException | JSONException e) {
            JMLandmarkMod.getLogger().error("Exception when loading the landmarks:\n " + e);
        }
    }

    @Override
    public void updateAllLandmarks() {
        loadOrCreate();
    }

    private void createExampleLandmark() {
        File exampleFile = new File(landmarksDir, "examplelandmark.json");
        if (!exampleFile.exists() || !exampleFile.isFile()) {
            JMLandmarkMod.getLogger().info("Creating the example landmark.");

            // Copy the default image file from the package
            File exampleImageFile = JMLandmarkMod.getProxy().parseImageSource("sprites.png");
            if (!exampleImageFile.exists() || !exampleImageFile.isFile()) {
                try (InputStream is = ServerLandmarkManager.class.getResourceAsStream("/assets/jmlandmark/images/sprites.png")) {
                    if (is == null) {
                        JMLandmarkMod.getLogger().error("Resource image doesn't exist.");
                        return;
                    }
                    try (OutputStream os = Files.newOutputStream(exampleImageFile.toPath())) {
                        IOUtils.copy(is, os);
                    }
                } catch (IOException e) {
                    JMLandmarkMod.getLogger().error("Failed to create the example landmark: " + e);
                    return;
                }
            }

            // Copy the example landmark to the landmarks file
            try (InputStream is = ServerLandmarkManager.class.getResourceAsStream("/assets/jmlandmark/landmarks/examplelandmark.json")) {
                if (is == null) {
                    JMLandmarkMod.getLogger().error("Resource landmark doesn't exist.");
                    return;
                }
                try (OutputStream os = Files.newOutputStream(exampleFile.toPath())) {
                    IOUtils.copy(is, os);
                }
            } catch (IOException e) {
                JMLandmarkMod.getLogger().error("Failed to create the example landmark: " + e);
            }
        }
    }

}
