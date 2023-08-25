package com.sakurarealm.jmlandmark.server;

import com.sakurarealm.jmlandmark.common.CommonProxy;
import com.sakurarealm.jmlandmark.common.landmark.Landmark;
import com.sakurarealm.jmlandmark.common.network.LandmarkPacket;
import com.sakurarealm.jmlandmark.common.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.beans.EventHandler;

@SideOnly(Side.SERVER)
public class ServerProxy extends CommonProxy {
    public static final String SERVER_IMAGES_DIR = "journeymap/plugin/landmarks/images/";

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (landMarkManager == null)
            return;


    }

    public void onServerTick(TickEvent.ServerTickEvent event) {

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

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (landMarkManager == null) {
            return;
        }

        for (Landmark landmark : getLandMarkManager().getAllLandmarks()) {
            if (!landmark.getName().equals("example"))
                PacketHandler.getInstance().sendTo((EntityPlayerMP) event.player, new LandmarkPacket(landmark, true));
        }
    }

}
