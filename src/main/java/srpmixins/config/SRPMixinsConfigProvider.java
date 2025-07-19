package srpmixins.config;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import srpmixins.SRPMixins;
import srpmixins.config.providers.SRPMobConfigProvider;

import java.util.*;
import java.util.stream.Collectors;

public class SRPMixinsConfigProvider {
    public static final Map<Integer,Float> dimensionHealthMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionDmgMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionArmorMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionKBResMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionDropMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionMobCapMultipliers = new HashMap<>();
    public static final Map<Integer, List<String>> biomeSpawningBlacklists = new HashMap<>();

    public static float playerNeedlerMulti = 0.4F;

    public static List<Integer> whiteListedDeterrents;

    //Deliberately copied/initialised here to stop ppl from changing it in game
    public static int chunkPhasesSpacing = SRPMixinsConfigHandler.chunkphases.chunkSpacing;
    public static int chunkPhasesHalfSpacing = chunkPhasesSpacing >> 1; //Spacing divided by two and truncated (so it's different for odd vs even spacing)
    public static boolean chunkPhasesSpacingIsOdd = (chunkPhasesSpacing & 1) == 1;

    public static final Map<Integer, Integer> minFeralisations = new HashMap<>();
    public static final Map<String, Integer> foodBlacklist = new HashMap<>();
    public static final Map<String, List<Integer>> blockBreakBlacklist = new HashMap<>();

    public static final List<String> fireMultiDmgTypes = new ArrayList<>();

    public static void init(){
        setupDimensionMultiplierMap(dimensionHealthMultipliers, SRPMixinsConfigHandler.dimension.dimensionHealthMultipliers);
        setupDimensionMultiplierMap(dimensionDmgMultipliers, SRPMixinsConfigHandler.dimension.dimensionDmgMultipliers);
        setupDimensionMultiplierMap(dimensionArmorMultipliers, SRPMixinsConfigHandler.dimension.dimensionArmorMultipliers);
        setupDimensionMultiplierMap(dimensionKBResMultipliers, SRPMixinsConfigHandler.dimension.dimensionKBResMultipliers);
        setupDimensionMultiplierMap(dimensionDropMultipliers, SRPMixinsConfigHandler.dimension.dimensionDropMultipliers);
        setupDimensionMultiplierMap(dimensionMobCapMultipliers, SRPMixinsConfigHandler.dimension.dimensionMobCapMultipliers);
        setupBiomeBlacklistMap(biomeSpawningBlacklists, SRPMixinsConfigHandler.spawns.biomeBlacklist);

        playerNeedlerMulti = SRPMixinsConfigHandler.potions.playerNeedlerMulti;
        if(playerNeedlerMulti < 0) playerNeedlerMulti = SRPConfigSystems.needlerDamage; //negative = use SRP default value

        whiteListedDeterrents = Arrays.stream(SRPMixinsConfigHandler.deterrents.whiteListedDeterrents)
                .map(s -> SRPMobConfigProvider.mobNameToParaIdMap.get(s.replace("srparasites:","")))
                .collect(Collectors.toList());

        for(String s : SRPMixinsConfigHandler.coth.minFeralisations){
            String[] split = s.split(",");
            if(split.length == 2) {
                String firstEntry = split[0].trim();
                int paraId = SRPMobConfigProvider.mobNameToParaIdMap.getOrDefault(firstEntry, -1);
                if(paraId == -1) {
                    try {
                        paraId = Integer.parseInt(firstEntry); //Compat for old config
                    } catch (Exception e) {
                        SRPMixins.LOGGER.warn("SRPMixins unable to parse Min Feralisation line {}. Mob name doesn't exist",s);
                        continue;
                    }
                }
                if(!SRPMobConfigProvider.mobGroupToParaIdMap.get("FERAL").contains(paraId)){ SRPMixins.LOGGER.warn("SRPMixins unable to parse Min Feralisation line {}. Not a feral parasite",s); continue; }
                try {
                    int count = Integer.parseInt(split[1].trim());
                    minFeralisations.put(paraId, count);
                }
                catch (Exception e){ SRPMixins.LOGGER.warn("SRPMixins unable to parse Min Feralisation line {}. Count is not a number",s); }
            } else SRPMixins.LOGGER.warn("SRPMixins unable to parse Min Feralisation line. Expected pattern: int, int, found {}",s);
        }

        for(String s : SRPMixinsConfigHandler.various.foodBlacklist){
            String[] split = s.split(",");
            if(split.length == 1) foodBlacklist.put(split[0].trim(), -1);
            else if(split.length == 2)
                try { foodBlacklist.put(split[0].trim(), Integer.parseInt(split[1].trim())); }
                catch (Exception e) { SRPMixins.LOGGER.warn("SRPMixins unable to parse food blacklist entry {}. Metadata not a number", s); }
            else SRPMixins.LOGGER.warn("SRPMixins unable to parse food blacklist entry {}. Expected pattern: modid:itemname, optional metadata", s);
        }

        for(String s : SRPMixinsConfigHandler.various.blockBreakBlacklist) {
            String[] split = s.split(",");
            if(split.length < 2) SRPMixins.LOGGER.warn("SRPMixins unable to parse block break blacklist entry {}. Expected pattern: modid:blockname, comma separated parasite mobname/group list", s);
            else {
                List<Integer> listedParaIds = new ArrayList<>();
                for(int i = 1; i < split.length; i++){
                    String entry = split[i].trim();
                    if(SRPMobConfigProvider.mobNameToParaIdMap.containsKey(entry)) listedParaIds.add(SRPMobConfigProvider.mobNameToParaIdMap.get(entry));
                    else if(SRPMobConfigProvider.mobGroupToParaIdMap.containsKey(entry)) listedParaIds.addAll(SRPMobConfigProvider.mobGroupToParaIdMap.get(entry));
                }
                blockBreakBlacklist.put(split[0].trim(), listedParaIds);
            }
        }

        fireMultiDmgTypes.addAll(Arrays.asList(SRPMixinsConfigHandler.adaptation.fireMultiDmgTypes));
    }

    public static void reset(){
        dimensionHealthMultipliers.clear();
        dimensionDmgMultipliers.clear();
        dimensionArmorMultipliers.clear();
        dimensionKBResMultipliers.clear();
        dimensionDropMultipliers.clear();
        dimensionMobCapMultipliers.clear();
        biomeSpawningBlacklists.clear();
        minFeralisations.clear();
        blockBreakBlacklist.clear();
        foodBlacklist.clear();
        fireMultiDmgTypes.clear();

        init();
    }

    public static int getLurePhaseMultiplier(byte phase){
        if(!SRPMixinsConfigHandler.lures.variableCarcassValues) return 1;
        if(SRPMixinsConfigHandler.lures.carcassPhaseMultis.length != SRPConfigProvider.getMaxPhase()+1) return 1;
        if(phase < 0 || phase > SRPConfigProvider.getMaxPhase()) return 1;
        return SRPMixinsConfigHandler.lures.carcassPhaseMultis[phase];
    }

    public static void setupBiomeBlacklistMap(Map<Integer, List<String>> map, String[] config) {
        for (String line : config) {
            String[] split = line.split(",");
            if (split.length >= 1) {
                try {
                    int dim = Integer.parseInt(split[0].trim());
                    if (!map.containsKey(dim))
                        map.put(dim, new ArrayList<>());
                    if(split.length>=2) {
                        String biome = split[1].trim();
                        map.get(dim).add(biome);
                    }
                } catch (NumberFormatException e) {
                    SRPMixins.LOGGER.warn(SRPMixins.NAME + " config could not parse biome blacklist line {}", line);
                }
            }
        }
    }

    public static void setupDimensionMultiplierMap(Map<Integer,Float> map, String[] config) {
        for (String line : config) {
            String[] split = line.split(",");
            if (split.length >= 2) {
                try {
                    int dim = Integer.parseInt(split[0].trim());
                    float multi = Float.parseFloat(split[1].trim());
                    if (!map.containsKey(dim))
                        map.put(dim, multi);
                } catch (NumberFormatException e) {
                    SRPMixins.LOGGER.warn(SRPMixins.NAME + " config could not parse dimension multiplier line {}", line);
                }
            }
        }
    }

}
