package com.sakurarealm.jmlandmark.common;

import com.sakurarealm.jmlandmark.common.landmark.LandmarkManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.*;

import java.io.File;

public abstract class CommonProxy {

    protected LandmarkManager landMarkManager;

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public void serverStarting(FMLServerStartingEvent event) {

    }

    public void serverStopping(FMLServerStoppingEvent event) {

    }

    public boolean isPlayerOp(EntityPlayerMP player) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

        if (server != null) {

            boolean isOp = server.getPlayerList().canSendCommands(player.getGameProfile());
            boolean cheatsAllowed = server.getWorld(0).getGameRules().getBoolean("commandBlockOutput");

            return isOp || cheatsAllowed;
        }
        return false;
    }

    public File parseImageSource(String image) {
        if (landMarkManager.getImageDir() != null) {
            return new File(landMarkManager.getImageDir(), image);
        }
        return null;
    }

    public LandmarkManager getLandMarkManager() {
        return landMarkManager;
    }
}
