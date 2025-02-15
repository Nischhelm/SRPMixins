package srpmixins;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import srpmixins.capability.CapabilityEvoPointsHandler;
import srpmixins.handlers.NexusSpawnSounds;
import srpmixins.handlers.ParasiteDropChance;
import srpmixins.util.CompatUtil;
import srpmixins.config.SRPConfigProvider;
import srpmixins.config.SRPMixinsConfigProvider;

@Mod(modid = SRPMixins.MODID, version = SRPMixins.VERSION, name = SRPMixins.NAME, dependencies = "required-after:fermiumbooter", acceptableRemoteVersions = "*")
public class SRPMixins {
    public static final String MODID = "srpmixins";
    public static final String VERSION = "2.4.2";
    public static final String NAME = "SRPMixins";
    public static final Logger LOGGER = LogManager.getLogger();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(NexusSpawnSounds.class);
        MinecraftForge.EVENT_BUS.register(ParasiteDropChance.class);
        SRPMixinsConfigProvider.init();
        CapabilityEvoPointsHandler.registerCapability();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        SRPConfigProvider.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if(CompatUtil.isLycanitesMobsLoaded())
            CompatUtil.reloadLycaniteSpawnerManager();
    }
}