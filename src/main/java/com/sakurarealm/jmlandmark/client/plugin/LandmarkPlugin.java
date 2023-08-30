package com.sakurarealm.jmlandmark.client.plugin;

import com.sakurarealm.jmlandmark.JMLandmarkMod;
import com.sakurarealm.jmlandmark.client.ClientLandmarkManager;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.display.Context;
import journeymap.client.api.event.ClientEvent;
import journeymap.client.api.event.DisplayUpdateEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;

@journeymap.client.api.ClientPlugin
@SideOnly(Side.CLIENT)
public class LandmarkPlugin implements IClientPlugin {

    private static LandmarkPlugin INSTANCE;
    private IClientAPI clientAPI;
    private Context.UI previousUI;

    public LandmarkPlugin() {
        INSTANCE = this;
    }

    public static LandmarkPlugin getInstance() {
        return INSTANCE;
    }

    public IClientAPI getClientAPI() {
        return clientAPI;
    }

    @Override
    public void initialize(IClientAPI clientAPI) {
        this.clientAPI = clientAPI;
        this.clientAPI.subscribe(getModId(), EnumSet.of(ClientEvent.Type.DISPLAY_UPDATE));

        JMLandmarkMod.getLogger().info("Initialized journey map landmark plugin.");

    }

    @Override
    public String getModId() {
        return JMLandmarkMod.MODID;
    }

    @Override
    public void onEvent(ClientEvent event) {
        if (event.type == ClientEvent.Type.DISPLAY_UPDATE) {
            DisplayUpdateEvent displayUpdateEvent = (DisplayUpdateEvent) event;
            JMLandmarkMod.getLogger().info(displayUpdateEvent.uiState);

            if (displayUpdateEvent.uiState.active && previousUI != displayUpdateEvent.uiState.ui) {
                ((ClientLandmarkManager) JMLandmarkMod.getProxy().getLandMarkManager()).updateFullscreenDisplay();
                previousUI = displayUpdateEvent.uiState.ui;
            } else if (!displayUpdateEvent.uiState.active) {
                ((ClientLandmarkManager) JMLandmarkMod.getProxy().getLandMarkManager()).updateMinimapDisplay();
                previousUI = null;
            }
        }
    }

}
