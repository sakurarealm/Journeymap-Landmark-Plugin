package com.sakurarealm.jmlandmark.server;

import com.sakurarealm.jmlandmark.common.CommonProxy;
import com.sakurarealm.jmlandmark.common.landmark.Landmark;
import com.sakurarealm.jmlandmark.common.landmark.LandmarkManager;
import com.sakurarealm.jmlandmark.common.network.LandmarkPacket;
import com.sakurarealm.jmlandmark.common.network.PacketHandler;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SideOnly(Side.SERVER)
public class ServerProxy extends CommonProxy {
    public static final String SERVER_IMAGES_DIR = "journeymap/plugin/landmarks/images/";

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (landMarkManager == null)
            return;


    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning()) {
            super.serverStarting(event);
            landMarkManager = new ServerLandmarkManager();
            landMarkManager.init();
        }

    }

    @Override
    public void serverStopping(FMLServerStoppingEvent event) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning()) {
            super.serverStopping(event);
        }

    }

    public void refresh() {
        List<Landmark> oldLandmarks = landMarkManager.getAllLandmarks();
        landMarkManager.loadOrCreate();
        List<Landmark> newLandmarks = landMarkManager.getAllLandmarks();

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server == null) {
            return;
        }

        for (EntityPlayerMP playerMP : server.getPlayerList().getPlayers()) {
            for (Landmark landmark : oldLandmarks) {
                if (!landmark.getName().equals("examplelandmark")) {
                    PacketHandler.getInstance().sendTo(playerMP, new LandmarkPacket(landmark, false));
                }
            }
            for (Landmark landmark : newLandmarks) {
                if (!landmark.getName().equals("examplelandmark")) {
                    PacketHandler.getInstance().sendTo(playerMP, new LandmarkPacket(landmark, true));
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (landMarkManager == null) {
            return;
        }

        for (Landmark landmark : getLandMarkManager().getAllLandmarks()) {
            if (!landmark.getName().equals("examplelandmark"))
                PacketHandler.getInstance().sendTo((EntityPlayerMP) event.player, new LandmarkPacket(landmark, true));
        }
    }

}
