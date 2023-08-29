package com.sakurarealm.jmlandmark.server;

import com.sakurarealm.jmlandmark.common.CommonProxy;
import com.sakurarealm.jmlandmark.common.landmark.Landmark;
import com.sakurarealm.jmlandmark.common.network.LandmarkPacket;
import com.sakurarealm.jmlandmark.common.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.SERVER)
public class ServerProxy extends CommonProxy {

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        super.serverStarting(event);
        landMarkManager = new ServerLandmarkManager();
        landMarkManager.init();
    }

    @Override
    public void serverStopping(FMLServerStoppingEvent event) {
        super.serverStopping(event);

    }

    public void reload() {
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
