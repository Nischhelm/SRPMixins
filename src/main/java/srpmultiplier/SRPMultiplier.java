package srpmultiplier;

import com.lycanitesmobs.core.spawner.SpawnerManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import srpmultiplier.handlers.NexusSpawnSounds;
import srpmultiplier.handlers.ParasiteDropChance;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

import java.util.ArrayList;
import java.util.HashMap;

@Mod(modid = SRPMultiplier.MODID, version = SRPMultiplier.VERSION, name = SRPMultiplier.NAME, dependencies = "required-after:fermiumbooter", acceptableRemoteVersions = "*")
public class SRPMultiplier {
    public static final String MODID = "srpmultiplier";
    public static final String VERSION = "2.0.7";
    public static final String NAME = "SRPMultiplier";
    public static final Logger LOGGER = LogManager.getLogger();

    public static HashMap<Integer,Float> dimensionHealthMultipliers = new HashMap<>();
    public static HashMap<Integer,Float> dimensionDmgMultipliers = new HashMap<>();
    public static HashMap<Integer,Float> dimensionArmorMultipliers = new HashMap<>();
    public static HashMap<Integer,Float> dimensionKBResMultipliers = new HashMap<>();
    public static HashMap<Integer,Float> dimensionDropMultipliers = new HashMap<>();
    public static HashMap<Integer, ArrayList<String>> biomeSpawningBlacklists = new HashMap<>();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(NexusSpawnSounds.class);
        MinecraftForge.EVENT_BUS.register(ParasiteDropChance.class);
        SRPMultiplierConfigHandler.setupDimensionMultiplierMap(dimensionHealthMultipliers, SRPMultiplierConfigHandler.server.dimensionHealthMultipliers);
        SRPMultiplierConfigHandler.setupDimensionMultiplierMap(dimensionDmgMultipliers, SRPMultiplierConfigHandler.server.dimensionDmgMultipliers);
        SRPMultiplierConfigHandler.setupDimensionMultiplierMap(dimensionArmorMultipliers, SRPMultiplierConfigHandler.server.dimensionArmorMultipliers);
        SRPMultiplierConfigHandler.setupDimensionMultiplierMap(dimensionKBResMultipliers, SRPMultiplierConfigHandler.server.dimensionKBResMultipliers);
        SRPMultiplierConfigHandler.setupDimensionMultiplierMap(dimensionDropMultipliers, SRPMultiplierConfigHandler.server.dimensionDropMultipliers);
        SRPMultiplierConfigHandler.setupBiomeBlacklistMap(biomeSpawningBlacklists, SRPMultiplierConfigHandler.server.biomeBlacklist);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        SpawnerManager.getInstance().reload();
    }
}