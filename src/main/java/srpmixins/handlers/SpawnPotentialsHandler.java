package srpmixins.handlers;

import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigWorld;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import com.dhanantry.scapeandrunparasites.world.biome.BiomeParasite;
import com.google.common.base.Functions;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;
import srpmixins.util.ParasiteCreatureType;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

import java.util.*;
import java.util.stream.Collectors;

public class SpawnPotentialsHandler {
    public static final Map<Byte, Map<Biome.SpawnListEntry, Integer>> phaseIdSpawnsCustom = new HashMap<>(); //don't reset cause it doesnt get rewritten
    public static final Map<Byte, Map<Biome.SpawnListEntry, Integer>> phaseIdSpawns = new HashMap<>();
    public static final Map<Biome.SpawnListEntry, Integer> allPhaseSpawns = new HashMap<>();
    public static final Map<Biome.SpawnListEntry, Integer> biomeSpawns = new HashMap<>();

    private static Map<Integer, Integer> paraIdToColoPointLock = null;

    public static void resetCaches(){
        phaseIdSpawnsCustom.clear();
        phaseIdSpawns.clear();
        allPhaseSpawns.clear();
        biomeSpawns.clear();
        paraIdToColoPointLock = null;
    }

    public static Map<Biome.SpawnListEntry, Integer> getPhaseSpawnListCustom(byte phaseChecked) {
        return phaseIdSpawnsCustom.computeIfAbsent(phaseChecked,
                phase -> phase >= 0 ? SRPSpawning.getSpawns(phase).stream()
                        .collect(Collectors.toMap(
                            Functions.identity(), //Key is just the SpawnListEntry itself
                            entry -> {            //Value is the para id assigned to the mob
                                ResourceLocation loc = EntityList.getKey(entry.entityClass);
                                if (loc == null) return -1;
                                String mobid = loc.getPath();
                                return SRPMixinsConfigProvider.mobNameToParaIdMap.getOrDefault(mobid, -1);
                            })
                        ) : Collections.emptyMap()
        );
    }

    @SubscribeEvent
    public static void onSpawnPotentials(WorldEvent.PotentialSpawns event) {
        if (event.getType() != ParasiteCreatureType.PARASITE) return;
        World world = event.getWorld();
        int currDim = world.provider.getDimension();

        //Blacklisted Dimensions if Evo is off -> don't add parasite spawn entries
        if (!SRPConfigSystems.useEvolution) {
            boolean isInList = Arrays.stream(SRPConfig.blackListedDimensions).anyMatch(dim -> dim == currDim);
            if(isInList != SRPConfig.blackListedDimensionsWhite) return;
        }

        SRPSaveData data = SRPSaveDataInterface.get(world, null, event.getPos());
        SRPWorldData worlddata = SRPWorldData.get(world);

        //Para Biome has its own handling independent on phase
        //Auto includes SRPMixins "Fix Parasite Biome Spawns"
        if (world.getBiome(event.getPos()) instanceof BiomeParasite){
            event.getList().addAll(filterSpawnEntries(biomeSpawns, data, worlddata, true));
        } else {
            byte evophase = data.getEvolutionPhase(currDim);

            if (SRPConfigSystems.useEvolution && SRPConfigSystems.phaseCustomSpawner) //Default: phases + custom spawner
                event.getList().addAll(filterSpawnEntries(getPhaseSpawnListCustom(evophase), data, worlddata, false));
            else if (SRPConfigSystems.useEvolution /*&& !SRPConfigSystems.phaseCustomSpawner*/) //Phases + no custom spawner
                event.getList().addAll(filterSpawnEntries(phaseIdSpawns.get(evophase), data, worlddata, false));
            else /*if (!SRPConfigSystems.useEvolution)*/ //Phases off
                event.getList().addAll(filterSpawnEntries(allPhaseSpawns, data, worlddata, false));
        }
    }

    private static List<Biome.SpawnListEntry> filterSpawnEntries(Map<Biome.SpawnListEntry, Integer> original, SRPSaveData data, SRPWorldData worldData, boolean isParaBiome){
        return original.entrySet().stream()
                .filter(entry -> entry.getValue() != -1)
                .filter(entry -> !data.checkParasiteID(entry.getValue())) //is not in evolution phase lock
                .filter(entry -> !isColonyLocked(entry.getValue(), worldData, isParaBiome)) //is not in evolution phase lock
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static boolean isColonyLocked(int paraIdToCheck, SRPWorldData data, boolean isParaBiome) {
        //ColonyLockFix: don't check for colony points if colonies are not enabled
        if(SRPMixinsConfigHandler.spawns.fixColonyLock && !SRPConfigWorld.coloniesActivated) return false;

        //This already includes the ColonyLockFix_ParaBiome

        if (isParaBiome && !SRPConfigWorld.preeValuesBiome)
            return false; //No colony lock in parasite biomes if srp config "Colony Parasite Values Biome" is on false

        if (paraIdToColoPointLock == null) paraIdToColoPointLock = Arrays.stream(SRPConfigWorld.preeValues)
                .map(s -> s.split(";"))
                .collect(Collectors.toMap(
                        split -> Integer.parseInt(split[0].trim()), //Key
                        split -> Integer.parseInt(split[1].trim())  //Value
                ));

        int minColoPointsRequired = paraIdToColoPointLock.getOrDefault(paraIdToCheck, 0);
        if (minColoPointsRequired == 0) return false; //not locked

        int currColoPoints = data.totalColonyPoints(0);
        return currColoPoints < minColoPointsRequired;
    }
}
