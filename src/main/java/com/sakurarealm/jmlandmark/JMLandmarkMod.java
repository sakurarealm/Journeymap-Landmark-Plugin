package com.sakurarealm.jmlandmark;

import com.sakurarealm.jmlandmark.common.CommonProxy;
import com.sakurarealm.jmlandmark.common.network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

@Mod(modid = JMLandmarkMod.MODID, name = JMLandmarkMod.NAME, version = JMLandmarkMod.VERSION)
@Mod.EventBusSubscriber(modid = JMLandmarkMod.MODID)
public class JMLandmarkMod {
    public static final String MODID = "jm-landmark";
    public static final String NAME = "Journey Map Landmark Plugin";
    public static final String VERSION = "1.0";

    private static Logger LOGGER;

    @Mod.Instance(JMLandmarkMod.MODID)
    private static JMLandmarkMod INSTANCE;

    @SidedProxy(clientSide = "com.sakurarealm.jmlandmark.client.ClientProxy",
            serverSide = "com.sakurarealm.jmlandmark.server.ServerProxy")
    private static CommonProxy PROXY;

    public JMLandmarkMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static CommonProxy getProxy() {
        return PROXY;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        PROXY.preInit(event);

        PacketHandler.getInstance().init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        PROXY.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PROXY.postInit(event);
        LOGGER.info("Journey map plugin postInit done!");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        PROXY.serverStarting(event);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        PROXY.serverStopping(event);
    }

}
