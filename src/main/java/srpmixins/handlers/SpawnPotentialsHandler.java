package srpmixins.handlers;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityAta;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfSquid;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityLum;
import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigWorld;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import com.dhanantry.scapeandrunparasites.world.biome.BiomeParasite;
import com.google.common.base.Functions;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.SRPMixins;
import srpmixins.compat.CompatUtil;
import srpmixins.compat.SRPExtraCompat;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.util.ParasiteCreatureType;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

import java.util.*;
import java.util.stream.Collectors;

public class SpawnPotentialsHandler {
    //This is basically just Lists but since getting parasite id is not a static method, we cache those too
    public static final Map<Byte, Map<Biome.SpawnListEntry, Integer>> phaseIdSpawnsCustom = new HashMap<>(); //don't reset cause it doesn't get rewritten
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
                                if (loc == null) return Integer.MIN_VALUE;
                                String mobid = loc.getPath();
                                return SRPMobConfigProvider.mobNameToParaIdMap.getOrDefault(mobid, Integer.MIN_VALUE);
                            })
                        ) : Collections.emptyMap()
        );
    }

    public static Map<Integer, Integer> getParaIdToColoPointLockMap(){
        if (paraIdToColoPointLock == null) paraIdToColoPointLock = Arrays.stream(SRPConfigWorld.preeValues)
                    .map(s -> s.split(";"))
                    .collect(Collectors.toMap(
                            split -> Integer.parseInt(split[0].trim()), //Key
                            split -> Integer.parseInt(split[1].trim())  //Value
                    ));
        return paraIdToColoPointLock;
    }

    @SubscribeEvent
    public static void onSpawnPotentials(WorldEvent.PotentialSpawns event) {
        if (event.getType() != ParasiteCreatureType.PARASITE) return;
        World world = event.getWorld();
        int currDim = world.provider.getDimension();

        //Blacklisted Dimensions (if Evo is off) -> don't add parasite spawn entries
        //If evo is on, gotta use phase -1 or -2
        if (!SRPConfigSystems.useEvolution) {
            boolean isInList = Arrays.stream(SRPConfig.blackListedDimensions).anyMatch(dim -> dim == currDim);
            if(isInList != SRPConfig.blackListedDimensionsWhite) return;
        }

        if (isBiomeBlacklisted(world, event.getPos())) return;

        SRPSaveData data = SRPSaveDataInterface.get(world, null, event.getPos());
        SRPWorldData worlddata = SRPWorldData.get(world);

        //Para Biome has its own handling independent on phase
        //Auto includes SRPMixins "Fix Parasite Biome Spawns"
        if (world.getBiome(event.getPos()) instanceof BiomeParasite){
            event.getList().addAll(filterSpawnEntries(biomeSpawns, data, worlddata, true, currDim));
        } else {
            byte evophase = data.getEvolutionPhase(currDim);

            //No spawns in phase -1 and phase -2
            if(SRPConfigSystems.useEvolution && evophase < 0) return;

            //Default: phases + custom spawner
            if (SRPConfigSystems.useEvolution && SRPConfigSystems.phaseCustomSpawner)
                event.getList().addAll(filterSpawnEntries(getPhaseSpawnListCustom(evophase), data, worlddata, false, currDim));

            //Phases + no custom spawner
            else if (SRPConfigSystems.useEvolution) //&& !SRPConfigSystems.phaseCustomSpawner
                event.getList().addAll(filterSpawnEntries(phaseIdSpawns.get(evophase), data, worlddata, false, currDim));

            //Phases off
            else /*if (!SRPConfigSystems.useEvolution)*/
                event.getList().addAll(filterSpawnEntries(allPhaseSpawns, data, worlddata, false, currDim));
        }
    }

    private static List<Biome.SpawnListEntry> filterSpawnEntries(Map<Biome.SpawnListEntry, Integer> original, SRPSaveData data, SRPWorldData worldData, boolean isParaBiome, int dimId){
        return original.entrySet().stream()
                .filter(entry -> entry.getValue() != Integer.MIN_VALUE)
                .filter(entry -> !data.checkParasiteID(entry.getValue())) //is not in evolution phase lock
                .filter(entry -> !isColonyLocked(entry.getValue(), worldData, isParaBiome)) //is not in evolution phase lock
                .filter(entry -> !isSubCapLocked(entry.getKey().entityClass, dimId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static boolean isSubCapLocked(Class<? extends EntityLiving> entityClass, int dimId) {
        if(entityClass == EntityLum.class || entityClass == EntityInfSquid.class || CompatUtil.srpextra.isLoaded() && SRPExtraCompat.isWaterParasite(entityClass)){
            if(SRPMixinsConfigHandler.waterparas.waterParasiteCap < 0) return false;
            return WorldMobCapHandler.waterCount.getOrDefault(dimId, 0) >= SRPMixinsConfigHandler.waterparas.waterParasiteCap;
        }
        else if(EntityPStationaryArchitect.class.isAssignableFrom(entityClass)){
            if(SRPMixinsConfigHandler.deterrents.nexusCap < 0) return false;
            return WorldMobCapHandler.nexusCount.getOrDefault(dimId,0) >= SRPMixinsConfigHandler.deterrents.nexusCap;
        }
        else if(entityClass == EntityAta.class)
            return WorldMobCapHandler.gnatCount.getOrDefault(dimId, 0) >= SRPConfig.worldGnatCap;
        //SRP Incomplete cap is never used for spawn checks
        //SRPMixins end simmermen cap is also only for conversions
        return false;
    }

    private static boolean isColonyLocked(int paraIdToCheck, SRPWorldData data, boolean isParaBiome) {
        //Architects only if colonies are enabled
        if(paraIdToCheck == 90 && !SRPConfigWorld.coloniesActivated) return false;

        //ColonyLockFix: don't check for colony points if colonies are not enabled
        if(SRPMixinsConfigHandler.spawns.fixColonyLock && !SRPConfigWorld.coloniesActivated) return false;

        //This already includes the ColonyLockFix_ParaBiome
        if (isParaBiome && !SRPConfigWorld.preeValuesBiome) return false; //No colony lock in parasite biomes if srp config "Colony Parasite Values Biome" is on false

        int minColoPointsRequired = getParaIdToColoPointLockMap().getOrDefault(paraIdToCheck, 0);
        if (minColoPointsRequired <= 0) return false; //not locked

        int currColoPoints = data.totalColonyPoints(0);
        return currColoPoints < minColoPointsRequired;
    }

    private static boolean isBiomeBlacklisted(World world, BlockPos pos){
        int dim = world.provider.getDimension();
        Set<String> biomeBlacklist = SRPMixinsConfigProvider.biomeSpawningBlacklists.get(dim);
        if(biomeBlacklist == null) return false;

        ResourceLocation biome = world.getBiome(pos).getRegistryName();
        if(biome == null) return false;
        String currBiome = biome.toString();
        String currBiomeMod = biome.getNamespace();
        boolean isInList = biomeBlacklist.contains(currBiome) ||
                biomeBlacklist.contains(currBiomeMod) ||
                biomeBlacklist.isEmpty();

        return isInList != SRPMixinsConfigHandler.spawns.biomeBlacklistIsWhitelist;
    }
}
