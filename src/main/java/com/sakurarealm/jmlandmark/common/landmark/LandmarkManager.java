package com.sakurarealm.jmlandmark.common.landmark;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import scala.collection.parallel.ParIterableLike;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class LandmarkManager {

    protected Map<String, Landmark> landmarkMap = new ConcurrentHashMap<>();

    protected File mcDir, dataDir, imageDir, landmarksDir;

    private static final String LANDMARK_DIR = "maplandmarks/data/";
    private static final String LANDMARK_IMAGE_DIR = "images";
    private static final String LANDMARKS_DIR = "landmarks";

    public LandmarkManager() {

    }

    public void init() {
        String worldFolderName, landmarksDirName;
        if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
            mcDir = FMLCommonHandler.instance().getMinecraftServerInstance().getDataDirectory();
            worldFolderName = "";
            landmarksDirName = LANDMARKS_DIR;
        } else {
            mcDir = Minecraft.getMinecraft().mcDataDir;
            worldFolderName = Minecraft.getMinecraft().getIntegratedServer().getFolderName();
            landmarksDirName = "";
        }

        dataDir = new File(mcDir, LANDMARK_DIR);
        File worldDir = new File(dataDir, worldFolderName);
        imageDir = new File(worldDir, LANDMARK_IMAGE_DIR);
        landmarksDir = new File(worldDir, landmarksDirName);

        imageDir.mkdirs();
        landmarksDir.mkdirs();
    }

    abstract public void loadOrCreate();

    public List<Landmark> getAllLandmarks() {
        return new ArrayList<>(landmarkMap.values());
    }

    public void addLandmark(Landmark landmark) {
        landmarkMap.put(landmark.getName(), landmark);
    }

    public void removeLandmark(String name) {
        landmarkMap.remove(name);
    }

    public void removeAllLandMarks() {
        landmarkMap.clear();
    }

    public File getMcDir() {
        return mcDir;
    }

    public File getDataDir() {
        return dataDir;
    }

    public File getImageDir() {
        return imageDir;
    }

    public File getLandmarksDir() {
        return landmarksDir;
    }

    public abstract void updateAllLandmarks();
}
