package srpmixins;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfSquid;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityLum;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
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
import srpmixins.handlers.*;
import srpmixins.util.compat.CompatUtil;
import srpmixins.util.compat.LycanitesMobsCompat;

@Mod(
        modid = SRPMixins.MODID,
        version = SRPMixins.VERSION,
        name = SRPMixins.NAME,
        dependencies = "required-after:fermiumbooter@[1.2.0,);required-after:srparasites",
        acceptableRemoteVersions = "*"
)
public class SRPMixins {
    public static final String MODID = "srpmixins";
    public static final String VERSION = "2.8.0";
    public static final String NAME = "SRPMixins";
    public static final Logger LOGGER = LogManager.getLogger();
    public static Configuration CONFIG;
    public static boolean completedLoading = false;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CONFIG = new Configuration(event.getSuggestedConfigurationFile());
        CONFIG.load();

        SRPMixinsConfigProvider.init();

        if(SRPMixinsConfigHandler.chunkphases.enabled && SRPConfigSystems.useEvolution) {
            CapabilityEvoPointsHandler.registerCapability();
            MinecraftForge.EVENT_BUS.register(CapabilityEvoPointsHandler.AttachCapabilityHandler.class);
        }

        if(SRPMixinsConfigHandler.adaptation.overhaulAdaptation) {
            CapabilityAdaptationHandler.registerCapability();
            MinecraftForge.EVENT_BUS.register(CapabilityAdaptationHandler.EventHandler.class);
        }

        registerEventSubscriberIf(NexusSpawnSounds.class, SRPMixinsConfigHandler.deterrents.playsounds);
        registerEventSubscriberIf(ParasiteDropChance.class, SRPMixinsConfigHandler.dimension.doMultipliers);
        registerEventSubscriberIf(SRPArmorBowEvolutionHandler.class, SRPMixinsConfigHandler.weapons.addArmorBowEvolution);
        registerEventSubscriberIf(WriteConversionPathways.class, SRPMixinsConfigHandler.spawns.autoFillConversionRules);
        //registerEventSubscriberIf(SpawnPotentialsHandler.class, SRPMixinsConfigHandler.spawns.fixSpawningEntirely); //TODO
    }

    private static void registerEventSubscriberIf(Object subscriber, boolean condition){
        if(condition) MinecraftForge.EVENT_BUS.register(subscriber);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        SRPConfigProvider.init();

        if(SRPMixinsConfigHandler.morephases.enableMorePhases && SRPMixinsConfigHandler.morephases.phaseKills.length == 0)
            SRPMixinsConfigProvider.initMorePhasesConfig();
        if (SRPMixinsConfigHandler.mobConfig.enableMobConfig && SRPMixinsConfigHandler.mobConfig.mobConfig.length == 0)
            SRPMixinsConfigProvider.initMobConfigs();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if(CompatUtil.isLycanitesMobsLoaded())
            LycanitesMobsCompat.reloadLycaniteSpawnerManager();

        SRPConfigProvider.postInit();
        SRPMixinsConfigProvider.postInit();

        completedLoading = true;

        if(SRPMixinsConfigHandler.waterparas.enableWaterSpawns) {
            EntitySpawnPlacementRegistry.setPlacementType(EntityInfSquid.class, EntityLiving.SpawnPlacementType.IN_WATER);
            EntitySpawnPlacementRegistry.setPlacementType(EntityLum.class, EntityLiving.SpawnPlacementType.IN_WATER);
        }
    }
}