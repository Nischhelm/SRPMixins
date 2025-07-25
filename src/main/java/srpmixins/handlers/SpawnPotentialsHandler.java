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
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.compat.CompatUtil;
import srpmixins.compat.SRPExtraCompat;
import srpmixins.config.SRPConfigProvider;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.util.ParasiteCreatureType;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

import java.util.*;
import java.util.stream.Collectors;

public class SpawnPotentialsHandler {
    public static final Map<Byte, List<BiomeSpawnListEntryWrapper>> phaseIdSpawnsCustom = new HashMap<>(); //don't reset cause it doesn't get rewritten
    public static final Map<Byte, List<BiomeSpawnListEntryWrapper>> phaseIdSpawns = new HashMap<>();
    public static final List<BiomeSpawnListEntryWrapper> allPhaseSpawns = new ArrayList<>();
    public static final List<BiomeSpawnListEntryWrapper> biomeSpawns = new ArrayList<>();

    private static Map<Integer, Integer> paraIdToColoPointLock = null;

    public static void resetCaches(){
        phaseIdSpawns.clear();
        allPhaseSpawns.clear();
        biomeSpawns.clear();
        paraIdToColoPointLock = null;
    }

    public static List<BiomeSpawnListEntryWrapper> getPhaseSpawnListCustom(byte phaseChecked) {
        if(phaseIdSpawnsCustom.containsKey(phaseChecked)) return phaseIdSpawnsCustom.get(phaseChecked);

        List<BiomeSpawnListEntryWrapper> freshList;
        if(phaseChecked > SRPConfigProvider.getMaxPhase())
            freshList = Collections.emptyList();
        else {
            List<Biome.SpawnListEntry> srpList = SRPSpawning.getSpawns(phaseChecked);
            if (srpList == null || srpList.isEmpty()) freshList = Collections.emptyList();
            else {
                freshList = srpList.stream().map(entry -> {
                    ResourceLocation loc = EntityList.getKey(entry.entityClass);
                    if (loc == null) return new BiomeSpawnListEntryWrapper(entry, Integer.MIN_VALUE);
                    int paraId = SRPMobConfigProvider.mobNameToParaIdMap.getOrDefault(loc.getPath(), Integer.MIN_VALUE);
                    return new BiomeSpawnListEntryWrapper(entry, paraId);
                }).collect(Collectors.toList());
            }
        }

        phaseIdSpawnsCustom.put(phaseChecked, freshList);
        return freshList;
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

        Set<String> biomeBlacklist = SRPMixinsConfigProvider.biomeSpawningBlacklists.get(currDim);
        if(biomeBlacklist != null && isDimensionBlacklisted(biomeBlacklist)) return;

        Biome biome = world.getBiome(event.getPos());
        if(biomeBlacklist != null && isBiomeBlacklisted(biomeBlacklist, biome)) return;

        SRPSaveData data = SRPSaveDataInterface.get(world, null, event.getPos());
        SRPWorldData worlddata = SRPWorldData.get(world);

        //Para Biome has its own handling independent on phase
        //Auto includes SRPMixins "Fix Parasite Biome Spawns"
        if (biome instanceof BiomeParasite)
            event.getList().addAll(filterSpawnEntries(biomeSpawns, data, worlddata, true, currDim));
        else {
            byte evophase = data.getEvolutionPhase(currDim);
            if(SRPConfigSystems.useEvolution && evophase < 0) return; //faster than checking each one

            //Default: phases + custom spawner
            if (SRPConfigSystems.useEvolution && SRPConfigSystems.phaseCustomSpawner)
                event.getList().addAll(filterSpawnEntries(getPhaseSpawnListCustom(evophase), data, worlddata, false, currDim));

            //Phases + no custom spawner
            else if (SRPConfigSystems.useEvolution) //&& !SRPConfigSystems.phaseCustomSpawner
                event.getList().addAll(filterSpawnEntries(phaseIdSpawns.getOrDefault(evophase, Collections.emptyList()), data, worlddata, false, currDim));

            //Phases off
            else /*if (!SRPConfigSystems.useEvolution)*/
                event.getList().addAll(filterSpawnEntries(allPhaseSpawns, data, worlddata, false, currDim));
        }
    }

    private static List<Biome.SpawnListEntry> filterSpawnEntries(List<BiomeSpawnListEntryWrapper> original, SRPSaveData data, SRPWorldData worldData, boolean isParaBiome, int dimId){
        List<Biome.SpawnListEntry> returnList = new ArrayList<>();
        for(BiomeSpawnListEntryWrapper wrapper : original){
            if(wrapper.paraId == Integer.MIN_VALUE) continue;
            if(data.checkParasiteID(wrapper.paraId)) continue;
            if(isColonyLocked(wrapper.paraId, worldData, isParaBiome)) continue;
            if(isSubCapLocked(wrapper.entry.entityClass, dimId)) continue;
            returnList.add(wrapper.entry);
        }
        return returnList;
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

    private static boolean isDimensionBlacklisted(Set<String> biomeBlacklist){
        if(biomeBlacklist.isEmpty()) //early return to not do string manipulation if whole dimension is disabled
            return !SRPMixinsConfigHandler.spawns.biomeBlacklistIsWhitelist;
        else return false;
    }

    private static boolean isBiomeBlacklisted(Set<String> biomeBlacklist, Biome biome){
        ResourceLocation biomeLoc = biome.getRegistryName();
        if(biomeLoc == null) return false;
        String biomeId = biomeLoc.toString();
        String biomeModId = biomeLoc.getNamespace();
        boolean isInList = biomeBlacklist.contains(biomeId) || biomeBlacklist.contains(biomeModId);

        return isInList != SRPMixinsConfigHandler.spawns.biomeBlacklistIsWhitelist;
    }

    public static class BiomeSpawnListEntryWrapper {
        final Biome.SpawnListEntry entry;
        final int paraId;
        public BiomeSpawnListEntryWrapper(Biome.SpawnListEntry entry, int paraId){
            this.entry = entry;
            this.paraId = paraId;
        }
    }
}
