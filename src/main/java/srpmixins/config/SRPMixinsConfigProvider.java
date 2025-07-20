package srpmixins.config;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import srpmixins.SRPMixins;
import srpmixins.config.providers.SRPMobConfigProvider;

import java.util.*;
import java.util.stream.Collectors;

public class SRPMixinsConfigProvider {
    // ---- these belong to DimensionMultiConfigProvider but SRPCotesia already expects them here woops ----
    public static final Map<Integer,Float> dimensionHealthMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionDmgMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionArmorMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionKBResMultipliers = new HashMap<>();
    // -----------------------------------------------------------------------------------------------------


    public static final Map<Integer, Set<String>> biomeSpawningBlacklists = new HashMap<>();
    public static float playerNeedlerMulti = 0.4F;
    public static List<Integer> whiteListedDeterrents;
    public static final Map<Integer, Integer> minFeralisations = new HashMap<>();
    public static final Map<String, Integer> foodBlacklist = new HashMap<>();
    public static final Map<String, Set<Integer>> blockBreakBlacklist = new HashMap<>();
    public static final Set<String> fireMultiDmgTypes = new HashSet<>();

    public static void init(){
        setupBiomeBlacklistMap();
        setupPlayerNeedlerMulti();
        setupWhitelistedDeterrents();
        setupMinFeralisations();
        setupFoodBlacklist();
        setupBlockBreakBlacklist();
        setupFireMultiDmgTypes();
    }

    public static void reset(){
        biomeSpawningBlacklists.clear();

        whiteListedDeterrents.clear();
        minFeralisations.clear();
        foodBlacklist.clear();
        blockBreakBlacklist.clear();
        fireMultiDmgTypes.clear();

        init();
    }

    private static void setupFireMultiDmgTypes() {
        fireMultiDmgTypes.addAll(Arrays.asList(SRPMixinsConfigHandler.adaptation.fireMultiDmgTypes));
    }

    private static void setupBlockBreakBlacklist() {
        for(String s : SRPMixinsConfigHandler.various.blockBreakBlacklist) {
            String[] split = s.split(",");
            if(split.length < 2) SRPMixins.LOGGER.warn("SRPMixins unable to parse block break blacklist entry {}. Expected pattern: modid:blockname, comma separated parasite mobname/group list", s);
            else {
                Set<Integer> listedParaIds = new HashSet<>();
                for(int i = 1; i < split.length; i++){
                    String entry = split[i].trim();
                    if(SRPMobConfigProvider.mobNameToParaIdMap.containsKey(entry)) listedParaIds.add(SRPMobConfigProvider.mobNameToParaIdMap.get(entry));
                    else if(SRPMobConfigProvider.mobGroupToParaIdMap.containsKey(entry)) listedParaIds.addAll(SRPMobConfigProvider.mobGroupToParaIdMap.get(entry));
                }
                blockBreakBlacklist.put(split[0].trim(), listedParaIds);
            }
        }
    }

    private static void setupFoodBlacklist() {
        for(String s : SRPMixinsConfigHandler.various.foodBlacklist){
            String[] split = s.split(",");
            if(split.length == 1) foodBlacklist.put(split[0].trim(), -1);
            else if(split.length == 2)
                try { foodBlacklist.put(split[0].trim(), Integer.parseInt(split[1].trim())); }
                catch (Exception e) { SRPMixins.LOGGER.warn("SRPMixins unable to parse food blacklist entry {}. Metadata not a number", s); }
            else SRPMixins.LOGGER.warn("SRPMixins unable to parse food blacklist entry {}. Expected pattern: modid:itemname, optional metadata", s);
        }
    }

    private static void setupMinFeralisations() {
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
    }

    private static void setupWhitelistedDeterrents() {
        whiteListedDeterrents = Arrays.stream(SRPMixinsConfigHandler.deterrents.whiteListedDeterrents)
                .map(s -> SRPMobConfigProvider.mobNameToParaIdMap.get(s.replace("srparasites:","")))
                .collect(Collectors.toList());
    }

    private static void setupPlayerNeedlerMulti() {
        playerNeedlerMulti = SRPMixinsConfigHandler.potions.playerNeedlerMulti;
        if(playerNeedlerMulti < 0) playerNeedlerMulti = SRPConfigSystems.needlerDamage; //negative = use SRP default value
    }

    public static void setupBiomeBlacklistMap() {
        for (String line : SRPMixinsConfigHandler.spawns.biomeBlacklist) {
            String[] split = line.split(",");
            if (split.length >= 1) {
                try {
                    int dim = Integer.parseInt(split[0].trim());
                    if (!biomeSpawningBlacklists.containsKey(dim))
                        biomeSpawningBlacklists.put(dim, new HashSet<>());
                    if(split.length>=2) {
                        String biome = split[1].trim();
                        biomeSpawningBlacklists.get(dim).add(biome);
                    }
                } catch (NumberFormatException e) {
                    SRPMixins.LOGGER.warn(SRPMixins.NAME + " config could not parse biome blacklist line {}", line);
                }
            }
        }
    }

    public static int getLurePhaseMultiplier(byte phase){
        if(!SRPMixinsConfigHandler.lures.variableCarcassValues) return 1;
        if(SRPMixinsConfigHandler.lures.carcassPhaseMultis.length != SRPConfigProvider.getMaxPhase()+1) return 1;
        if(phase < 0 || phase > SRPConfigProvider.getMaxPhase()) return 1;
        return SRPMixinsConfigHandler.lures.carcassPhaseMultis[phase];
    }
}
