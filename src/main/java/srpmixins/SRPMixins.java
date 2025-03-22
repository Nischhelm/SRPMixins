package srpmixins;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import srpmixins.capability.adaptation.CapabilityAdaptationHandler;
import srpmixins.capability.chunkphases.CapabilityEvoPointsHandler;
import srpmixins.config.SRPConfigProvider;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;
import srpmixins.handlers.NexusSpawnSounds;
import srpmixins.handlers.ParasiteDropChance;
import srpmixins.handlers.SRPArmorBowEvolutionHandler;
import srpmixins.util.CompatUtil;

@Mod(modid = SRPMixins.MODID, version = SRPMixins.VERSION, name = SRPMixins.NAME, dependencies = "required-after:fermiumbooter", acceptableRemoteVersions = "*")
public class SRPMixins {
    public static final String MODID = "srpmixins";
    public static final String VERSION = "2.5.5";
    public static final String NAME = "SRPMixins";
    public static final Logger LOGGER = LogManager.getLogger();
    public static boolean completedLoading = false;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        SRPMixinsConfigProvider.init();

        if(SRPMixinsConfigHandler.deterrents.playsounds) MinecraftForge.EVENT_BUS.register(NexusSpawnSounds.class);
        if(SRPMixinsConfigHandler.dimension.doMultipliers) MinecraftForge.EVENT_BUS.register(ParasiteDropChance.class);

        if(SRPMixinsConfigHandler.chunkphases.enabled && SRPConfigSystems.useEvolution) {
            CapabilityEvoPointsHandler.registerCapability();
            MinecraftForge.EVENT_BUS.register(CapabilityEvoPointsHandler.AttachCapabilityHandler.class);
        }

        if(SRPMixinsConfigHandler.adaptation.overhaulAdaptation) {
            CapabilityAdaptationHandler.registerCapability();
            MinecraftForge.EVENT_BUS.register(CapabilityAdaptationHandler.EventHandler.class);
        }

        if(SRPMixinsConfigHandler.weapons.addArmorBowEvolution)
            MinecraftForge.EVENT_BUS.register(SRPArmorBowEvolutionHandler.class);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        SRPConfigProvider.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if(CompatUtil.isLycanitesMobsLoaded())
            CompatUtil.reloadLycaniteSpawnerManager();

        SRPConfigProvider.postInit();

        completedLoading = true;
    }


}