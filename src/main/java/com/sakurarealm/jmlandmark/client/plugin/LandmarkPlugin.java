package com.sakurarealm.jmlandmark.client.plugin;

import com.sakurarealm.jmlandmark.JMLandmarkMod;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.event.ClientEvent;

@journeymap.client.api.ClientPlugin
public class LandmarkPlugin implements IClientPlugin {

    private static LandmarkPlugin INSTANCE;
    private IClientAPI clientAPI;

    public LandmarkPlugin() {
        INSTANCE = this;
    }

    public IClientAPI getClientAPI() {
        return clientAPI;
    }

    public static LandmarkPlugin getInstance() {
        return INSTANCE;
    }

    @Override
    public void initialize(IClientAPI clientAPI) {
        this.clientAPI = clientAPI;
        // this.clientAPI.subscribe(getModId(), EnumSet.of(MAPPING_STARTED, MAPPING_STOPPED));

        JMLandmarkMod.getLogger().info("Initialized journey map landmark plugin.");

    }

    @Override
    public String getModId() {
        return JMLandmarkMod.MODID;
    }

    @Override
    public void onEvent(ClientEvent event) {

    }

}
