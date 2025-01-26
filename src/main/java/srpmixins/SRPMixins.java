package srpmixins;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import srpmixins.handlers.NexusSpawnSounds;
import srpmixins.handlers.ParasiteDropChance;
import srpmixins.handlers.SRPMixinsConfigHandler;
import srpmixins.util.CompatUtil;

import java.util.ArrayList;
import java.util.HashMap;

@Mod(modid = SRPMixins.MODID, version = SRPMixins.VERSION, name = SRPMixins.NAME, dependencies = "required-after:fermiumbooter", acceptableRemoteVersions = "*")
public class SRPMixins {
    public static final String MODID = "srpmixins";
    public static final String VERSION = "2.1.5";
    public static final String NAME = "SRPMixins";
    public static final Logger LOGGER = LogManager.getLogger();

    public static HashMap<Integer,Float> dimensionHealthMultipliers = new HashMap<>();
    public static HashMap<Integer,Float> dimensionDmgMultipliers = new HashMap<>();
    public static HashMap<Integer,Float> dimensionArmorMultipliers = new HashMap<>();
    public static HashMap<Integer,Float> dimensionKBResMultipliers = new HashMap<>();
    public static HashMap<Integer,Float> dimensionDropMultipliers = new HashMap<>();
    public static HashMap<Integer,Float> dimensionMobCapMultipliers = new HashMap<>();
    public static HashMap<Integer, ArrayList<String>> biomeSpawningBlacklists = new HashMap<>();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(NexusSpawnSounds.class);
        MinecraftForge.EVENT_BUS.register(ParasiteDropChance.class);
        SRPMixinsConfigHandler.setupDimensionMultiplierMap(dimensionHealthMultipliers, SRPMixinsConfigHandler.dimension.dimensionHealthMultipliers);
        SRPMixinsConfigHandler.setupDimensionMultiplierMap(dimensionDmgMultipliers, SRPMixinsConfigHandler.dimension.dimensionDmgMultipliers);
        SRPMixinsConfigHandler.setupDimensionMultiplierMap(dimensionArmorMultipliers, SRPMixinsConfigHandler.dimension.dimensionArmorMultipliers);
        SRPMixinsConfigHandler.setupDimensionMultiplierMap(dimensionKBResMultipliers, SRPMixinsConfigHandler.dimension.dimensionKBResMultipliers);
        SRPMixinsConfigHandler.setupDimensionMultiplierMap(dimensionDropMultipliers, SRPMixinsConfigHandler.dimension.dimensionDropMultipliers);
        SRPMixinsConfigHandler.setupDimensionMultiplierMap(dimensionMobCapMultipliers, SRPMixinsConfigHandler.dimension.dimensionMobCapMultipliers);
        SRPMixinsConfigHandler.setupBiomeBlacklistMap(biomeSpawningBlacklists, SRPMixinsConfigHandler.various.biomeBlacklist);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if(CompatUtil.isLycanitesMobsLoaded())
            CompatUtil.reloadLycaniteSpawnerManager();
    }
}