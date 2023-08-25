package com.sakurarealm.jmlandmark.server;

import com.sakurarealm.jmlandmark.JMLandmarkMod;
import com.sakurarealm.jmlandmark.common.landmark.ImageSource;
import com.sakurarealm.jmlandmark.common.landmark.Landmark;
import com.sakurarealm.jmlandmark.common.landmark.LandmarkManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServerLandmarkManager extends LandmarkManager {
    @Override
    public void init() {
        loadOrCreate();
    }

    @Override
    public void loadOrCreate() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(landmarksDir.toPath(), "*.json")) {
            for (Path entry : stream) {
                String content = new String(Files.readAllBytes(entry));
                JSONObject landmarkObject = new JSONObject(content);
                Landmark landmark = new Landmark();
                landmark.fromJson(landmarkObject);
                addLandmark(landmark);

                createExampleLandmark();
            }
        } catch (IOException | JSONException e) {
            JMLandmarkMod.getLogger().error("Exception when loading the landmarks:\n " + e);
        }
    }

    @Override
    public void updateAllLandmarks() {

    }

    private void createExampleLandmark() {
        File exampleFile = new File(landmarksDir, "example.json");
        if (!exampleFile.exists()) {
            JMLandmarkMod.getLogger().info("Creating the example landmark.");

            ImageSource exampleSource()
        }
    }
    
}
