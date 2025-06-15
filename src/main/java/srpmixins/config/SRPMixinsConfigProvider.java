package srpmixins.config;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import srpmixins.SRPMixins;

import java.util.*;
import java.util.stream.Collectors;

public class SRPMixinsConfigProvider {
    public static final Map<String, Integer> mobNameToParaIdMap = new HashMap<>();
    public static final Map<Integer, String> paraIdToMobName;
    public static final Map<String, List<Integer>> mobGroupToParaIdMap = new HashMap<>();

    public static final Map<Integer,Float> dimensionHealthMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionDmgMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionArmorMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionKBResMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionDropMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionMobCapMultipliers = new HashMap<>();
    public static final Map<Integer, List<String>> biomeSpawningBlacklists = new HashMap<>();
    public static final Map<String, Byte> biomeStartPhases = new HashMap<>();
    public static final List<Integer> chunkPhasesDimensionBlacklist = new ArrayList<>();

    public static float playerNeedlerMulti = 0.4F;

    public static List<Integer> whiteListedDeterrents;

    //Deliberately copied/initialised here to stop ppl from changing it in game
    public static int chunkPhasesSpacing = SRPMixinsConfigHandler.chunkphases.chunkSpacing;
    public static int chunkPhasesHalfSpacing = chunkPhasesSpacing >> 1; //Spacing divided by two and truncated (so it's different for odd vs even spacing)
    public static boolean chunkPhasesSpacingIsOdd = (chunkPhasesSpacing & 1) == 1;

    public static final Map<Integer, Integer> minFeralisations = new HashMap<>();
    public static final Map<String, Integer> foodBlacklist = new HashMap<>();
    public static final Map<String, List<Integer>> blockBreakBlacklist = new HashMap<>();
    public static final List<List<Float>> nexusGrowStunChance = new ArrayList<>();

    private static final Map<String, SRPMobConfig> srpMobConfig = new HashMap<>();

    private static final Map<String, Map<String, Integer>> conversionPathwayLocks = new HashMap<>();

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
                .map(s -> mobNameToParaIdMap.get(s.replace("srparasites:","")))
                .collect(Collectors.toList());

        for (String s : SRPMixinsConfigHandler.chunkphases.biomeStartPhases) {
            String[] split = s.split(",");
            if(split.length < 2){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse biome start phase entry, expected pattern: modid:biomename; startPhase, provided was: {}", s);
                continue;
            }
            try {
                String biomeId = split[0].trim();
                byte startPhase = Byte.parseByte(split[1].trim());
                biomeStartPhases.put(biomeId, startPhase);
            } catch (Exception e){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse biome start phase entry, expected number after semicolon, provided was: {}", s);
            }
        }

        for(int dimId : SRPMixinsConfigHandler.chunkphases.dimensionBlacklist)
            chunkPhasesDimensionBlacklist.add(dimId);

        for(String s : SRPMixinsConfigHandler.coth.minFeralisations){
            String[] split = s.split(",");
            if(split.length == 2) {
                String firstEntry = split[0].trim();
                int paraId = mobNameToParaIdMap.getOrDefault(firstEntry, -1);
                if(paraId == -1) {
                    try {
                        paraId = Integer.parseInt(firstEntry); //Compat for old config
                    } catch (Exception e) {
                        SRPMixins.LOGGER.warn("SRPMixins unable to parse Min Feralisation line {}. Mob name doesn't exist",s);
                        continue;
                    }
                }
                if(!mobGroupToParaIdMap.get("FERAL").contains(paraId)){ SRPMixins.LOGGER.warn("SRPMixins unable to parse Min Feralisation line {}. Not a feral parasite",s); continue; }
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
                    if(mobNameToParaIdMap.containsKey(entry)) listedParaIds.add(mobNameToParaIdMap.get(entry));
                    else if(mobGroupToParaIdMap.containsKey(entry)) listedParaIds.addAll(mobGroupToParaIdMap.get(entry));
                }
                blockBreakBlacklist.put(split[0].trim(), listedParaIds);
            }
        }

        fireMultiDmgTypes.addAll(Arrays.asList(SRPMixinsConfigHandler.adaptation.fireMultiDmgTypes));

        readConversionLockConfig();
    }

    public static void postInit(){
        for(String s : SRPMixinsConfigHandler.morephases.nexusGrowPenalty){
            String[] split = s.split(";");
            if(split.length < 3){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse \"More Phases\" Nexus Grow Stunned entry, too few arguments (expected 3): {}", s);
                continue;
            }
            try {
                float penaltyStageI = Float.parseFloat(split[0].trim());
                float penaltyStageII = Float.parseFloat(split[1].trim());
                float penaltyStageIII = Float.parseFloat(split[2].trim());
                nexusGrowStunChance.add(Arrays.asList(penaltyStageI, penaltyStageII, penaltyStageIII));
            } catch (Exception e){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse \"More Phases\" Nexus Grow Stun Chance entry. Expected pattern: stunChanceStage1; stunChanceStage2; stunChanceStage3. Provided was {}", s);
            }
        }
    }

    public static void reset(){
        dimensionHealthMultipliers.clear();
        dimensionDmgMultipliers.clear();
        dimensionArmorMultipliers.clear();
        dimensionKBResMultipliers.clear();
        dimensionDropMultipliers.clear();
        dimensionMobCapMultipliers.clear();
        biomeSpawningBlacklists.clear();
        biomeStartPhases.clear();
        chunkPhasesDimensionBlacklist.clear();
        minFeralisations.clear();
        blockBreakBlacklist.clear();
        foodBlacklist.clear();
        srpMobConfig.clear();
        fireMultiDmgTypes.clear();

        init();
        postInit();
    }

    public static int getLurePhaseMultiplier(byte phase){
        if(!SRPMixinsConfigHandler.lures.variableCarcassValues) return 1;
        if(SRPMixinsConfigHandler.lures.carcassPhaseMultis.length != SRPMixinsConfigHandler.morephases.getMaxPhase()+1) return 1;
        if(phase < 0 || phase > SRPMixinsConfigHandler.morephases.getMaxPhase()) return 1;
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

    static {
        mobNameToParaIdMap.put("pri_longarms", 1);
        mobNameToParaIdMap.put("sim_bigspider", 2);
        mobNameToParaIdMap.put("carrier_heavy", 3);
        mobNameToParaIdMap.put("pri_yelloweye", 4);
        mobNameToParaIdMap.put("buglin", 5);
        mobNameToParaIdMap.put("sim_human", 6);
        mobNameToParaIdMap.put("hi_blaze", 9960);  //TODO: placeholder id
        mobNameToParaIdMap.put("hi_skeleton", 9961); //TODO: placeholder id
        mobNameToParaIdMap.put("pri_manducater", 7);
        mobNameToParaIdMap.put("pri_summoner", 8);
        mobNameToParaIdMap.put("overseer", 9);
        mobNameToParaIdMap.put("pri_reeker", 10);
        mobNameToParaIdMap.put("carrier_flying", 11);
        mobNameToParaIdMap.put("rupter", 12);
        mobNameToParaIdMap.put("sim_cow", 13);
        mobNameToParaIdMap.put("sim_sheep", 14);
        mobNameToParaIdMap.put("sim_wolf", 15);
        mobNameToParaIdMap.put("beckon_si", 16);
        mobNameToParaIdMap.put("pri_bolster", 17);
        mobNameToParaIdMap.put("beckon_sii", 18);
        mobNameToParaIdMap.put("beckon_siii", 19);
        mobNameToParaIdMap.put("anc_overlord", 20);
        mobNameToParaIdMap.put("sim_wolfhead", 21);
        mobNameToParaIdMap.put("sim_sheephead", 22);
        mobNameToParaIdMap.put("movingflesh", 23);
        mobNameToParaIdMap.put("anc_dreadnaut", 24);
        mobNameToParaIdMap.put("vigilante", 25);
        mobNameToParaIdMap.put("sim_pig", 26);
        mobNameToParaIdMap.put("sim_villager", 27);
        mobNameToParaIdMap.put("sim_cowhead", 28);
        mobNameToParaIdMap.put("kyphosis", 29);
        mobNameToParaIdMap.put("sentry", 30);
        mobNameToParaIdMap.put("sim_pighead", 31);
        mobNameToParaIdMap.put("sim_villagerhead", 32);
        mobNameToParaIdMap.put("warden", 33);
        mobNameToParaIdMap.put("anc_pod", 34);
        mobNameToParaIdMap.put("anc_dreadnaut_ten", 35);
        mobNameToParaIdMap.put("worker", 36);
        mobNameToParaIdMap.put("pri_tozoon", 37);
        mobNameToParaIdMap.put("pri_arachnida", 38);
        mobNameToParaIdMap.put("incompleteform_small", 39);
        mobNameToParaIdMap.put("sim_adventurer", 40);
        mobNameToParaIdMap.put("beckon_siv", 41);
        mobNameToParaIdMap.put("incompleteform_medium", 43);
        mobNameToParaIdMap.put("sim_horse", 44);
        mobNameToParaIdMap.put("sim_horsehead", 45);
        mobNameToParaIdMap.put("sim_humanhead", 46);
        mobNameToParaIdMap.put("bomber_light", 47);
        mobNameToParaIdMap.put("host", 48);
        mobNameToParaIdMap.put("sim_bear", 49);
        mobNameToParaIdMap.put("marauder", 50);
        mobNameToParaIdMap.put("ada_longarms", 51);
        mobNameToParaIdMap.put("ada_manducater", 52);
        mobNameToParaIdMap.put("ada_summoner", 53);
        mobNameToParaIdMap.put("ada_reeker", 54);
        mobNameToParaIdMap.put("ada_yelloweye", 55);
        mobNameToParaIdMap.put("ada_bolster", 56);
        mobNameToParaIdMap.put("ada_arachnida", 58);
        mobNameToParaIdMap.put("sim_enderman", 59);
        mobNameToParaIdMap.put("grunt", 60);
        mobNameToParaIdMap.put("crux", 62);
        mobNameToParaIdMap.put("heed", 63);
        mobNameToParaIdMap.put("sim_dragone", 64);
        mobNameToParaIdMap.put("bomber_heavy", 65);
        mobNameToParaIdMap.put("pri_devourer", 66);
        mobNameToParaIdMap.put("sim_endermanhead", 69);
        mobNameToParaIdMap.put("sim_dragonehead", 70);
        mobNameToParaIdMap.put("sim_adventurerhead", 71);
        mobNameToParaIdMap.put("seizer", 72);
        mobNameToParaIdMap.put("dispatcher_si", 73);
        mobNameToParaIdMap.put("dispatcherten", 74);
        mobNameToParaIdMap.put("hostii", 75);
        mobNameToParaIdMap.put("mangler", 76);
        mobNameToParaIdMap.put("dispatcher_sii", 77);
        mobNameToParaIdMap.put("dispatcher_siii", 78);
        mobNameToParaIdMap.put("dispatcher_siv", 79);
        mobNameToParaIdMap.put("thrall", 80);
        mobNameToParaIdMap.put("seeker", 82);
        mobNameToParaIdMap.put("monarch", 84);
        mobNameToParaIdMap.put("wraith", 85);
        mobNameToParaIdMap.put("bogle", 86);
        mobNameToParaIdMap.put("haunter", 87);
        mobNameToParaIdMap.put("carrier_colony", 88);
        mobNameToParaIdMap.put("succor", 89);
        mobNameToParaIdMap.put("architect", 90);
        mobNameToParaIdMap.put("gnat", 91);
        mobNameToParaIdMap.put("pri_vermin", 92);
        mobNameToParaIdMap.put("fer_cow", 93);
        mobNameToParaIdMap.put("fer_enderman", 94);
        mobNameToParaIdMap.put("fer_horse", 95);
        mobNameToParaIdMap.put("fer_human", 96);
        mobNameToParaIdMap.put("fer_pig", 97);
        mobNameToParaIdMap.put("fer_sheep", 98);
        mobNameToParaIdMap.put("fer_villager", 99);
        mobNameToParaIdMap.put("tendril", 202);
        mobNameToParaIdMap.put("biomass", 205);
        mobNameToParaIdMap.put("wave", 211);
        mobNameToParaIdMap.put("waveshock", 213);
        mobNameToParaIdMap.put("fer_wolf", 300);
        mobNameToParaIdMap.put("hi_golem", 301);
        mobNameToParaIdMap.put("carrier_light", 304);
        mobNameToParaIdMap.put("fer_bear", 306);
        mobNameToParaIdMap.put("sim_squid", 307);
        mobNameToParaIdMap.put("worm", 308);

        paraIdToMobName = mobNameToParaIdMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        mobGroupToParaIdMap.put("ADAPTED", Arrays.asList(51, 52, 53, 54, 55, 56, 58));
        mobGroupToParaIdMap.put("ANCIENT", Arrays.asList(20, 24, 35));
        mobGroupToParaIdMap.put("ASSIMILATED", Arrays.asList(2, 6, 13, 14, 15, 21, 22, 26, 27, 28, 31, 32, 40, 44, 45, 46, 49, 59, 64, 69, 70, 71, 307));
        mobGroupToParaIdMap.put("CRUDE", Arrays.asList(23, 39, 43, 48, 62, 63, 75, 80));
        mobGroupToParaIdMap.put("DETERRENT", Arrays.asList(29, 30, 72, 74, 308));
        mobGroupToParaIdMap.put("FERAL", Arrays.asList(93, 94, 95, 96, 97, 98, 99, 300, 306));
        mobGroupToParaIdMap.put("HIJACKED", Collections.singletonList(301));
        mobGroupToParaIdMap.put("INBORN", Arrays.asList(3, 5, 11, 12, 36, 76, 91, 304));
        mobGroupToParaIdMap.put("NEXUS", Arrays.asList(16, 18, 19, 41, 73, 77, 78, 79));
        mobGroupToParaIdMap.put("PREEMINENT", Arrays.asList(65, 82, 85, 86, 87, 88, 89, 90));
        mobGroupToParaIdMap.put("PRIMITIVE", Arrays.asList(1, 4, 7, 8, 10, 17, 37, 38, 66, 92));
        mobGroupToParaIdMap.put("PURE", Arrays.asList(9, 25, 33, 47, 50, 60, 84));
    }

    //This is only called once if "More Phases" is enabled but the config isn't filled with current SRP values yet
    public static void initMorePhasesConfig() {
        int[] phaseKills = {0, SRPConfigSystems.phaseKillsOne, SRPConfigSystems.phaseKillsTwo, SRPConfigSystems.phaseKillsThree, SRPConfigSystems.phaseKillsFour, SRPConfigSystems.phaseKillsFive, SRPConfigSystems.phaseKillsSix, SRPConfigSystems.phaseKillsSeven, SRPConfigSystems.phaseKillsEight, SRPConfigSystems.phaseKillsNine, SRPConfigSystems.phaseKillsTen};
        double[] phaseKillCountPlus = {0.0F, SRPConfigSystems.phaseKillCountPlusOne, SRPConfigSystems.phaseKillCountPlusTwo, SRPConfigSystems.phaseKillCountPlusThree, SRPConfigSystems.phaseKillCountPlusFour, SRPConfigSystems.phaseKillCountPlusFive, SRPConfigSystems.phaseKillCountPlusSix, SRPConfigSystems.phaseKillCountPlusSeven, SRPConfigSystems.phaseKillCountPlusEight, SRPConfigSystems.phaseKillCountPlusNine, SRPConfigSystems.phaseKillCountPlusTen};
        String[] phaseWarning = {"", SRPConfigSystems.phaseWarningOne, SRPConfigSystems.phaseWarningTwo, SRPConfigSystems.phaseWarningThree, SRPConfigSystems.phaseWarningFour, SRPConfigSystems.phaseWarningFive, SRPConfigSystems.phaseWarningSix, SRPConfigSystems.phaseWarningSeven, SRPConfigSystems.phaseWarningEight, SRPConfigSystems.phaseWarningNine, SRPConfigSystems.phaseWarningTen};
        int[] phaseDelayTicks = {0, SRPConfigSystems.phaseDelayTicksOne, SRPConfigSystems.phaseDelayTicksTwo, SRPConfigSystems.phaseDelayTicksThree, SRPConfigSystems.phaseDelayTicksFour, SRPConfigSystems.phaseDelayTicksFive, SRPConfigSystems.phaseDelayTicksSix, SRPConfigSystems.phaseDelayTicksSeven, SRPConfigSystems.phaseDelayTicksEight, SRPConfigSystems.phaseDelayTicksNine, SRPConfigSystems.phaseDelayTicksTen};
        int[] sleepPenalty = {SRPConfigSystems.sleepPenaltyZero, SRPConfigSystems.sleepPenaltyOne, SRPConfigSystems.sleepPenaltyTwo, SRPConfigSystems.sleepPenaltyThree, SRPConfigSystems.sleepPenaltyFour, SRPConfigSystems.sleepPenaltyFive, SRPConfigSystems.sleepPenaltySix, SRPConfigSystems.sleepPenaltySeven, SRPConfigSystems.sleepPenaltyEight, SRPConfigSystems.sleepPenaltyNine, SRPConfigSystems.sleepPenaltyTen};

        int[] phaseMinParasiteID = {SRPConfigSystems.phaseCancelParasiteIDZero, SRPConfigSystems.phaseCancelParasiteIDOne, SRPConfigSystems.phaseCancelParasiteIDTwo, SRPConfigSystems.phaseCancelParasiteIDThree, SRPConfigSystems.phaseCancelParasiteIDFour, SRPConfigSystems.phaseCancelParasiteIDFive, SRPConfigSystems.phaseCancelParasiteIDSix, SRPConfigSystems.phaseCancelParasiteIDSeven, SRPConfigSystems.phaseCancelParasiteIDEight, SRPConfigSystems.phaseCancelParasiteIDNine, SRPConfigSystems.phaseCancelParasiteIDTen};
        int[] phaseMaxParasiteID = {SRPConfigSystems.phaseMaxParasiteIDZero, SRPConfigSystems.phaseMaxParasiteIDOne, SRPConfigSystems.phaseMaxParasiteIDTwo, SRPConfigSystems.phaseMaxParasiteIDThree, SRPConfigSystems.phaseMaxParasiteIDFour, SRPConfigSystems.phaseMaxParasiteIDFive, SRPConfigSystems.phaseMaxParasiteIDSix, SRPConfigSystems.phaseMaxParasiteIDSeven, SRPConfigSystems.phaseMaxParasiteIDEight, SRPConfigSystems.phaseMaxParasiteIDNine, SRPConfigSystems.phaseMaxParasiteIDTen};

        List<List<String>> phaseSpawnListOriginal = Arrays.asList(Arrays.asList(SRPConfigSystems.phaseSpawnEntryZero), Arrays.asList(SRPConfigSystems.phaseSpawnEntryOne), Arrays.asList(SRPConfigSystems.phaseSpawnEntryTwo), Arrays.asList(SRPConfigSystems.phaseSpawnEntryThree), Arrays.asList(SRPConfigSystems.phaseSpawnEntryFour), Arrays.asList(SRPConfigSystems.phaseSpawnEntryFive), Arrays.asList(SRPConfigSystems.phaseSpawnEntrySix), Arrays.asList(SRPConfigSystems.phaseSpawnEntrySeven), Arrays.asList(SRPConfigSystems.phaseSpawnEntryEight), Arrays.asList(SRPConfigSystems.phaseSpawnEntryNine), Arrays.asList(SRPConfigSystems.phaseSpawnEntryTen));
        List<String> newSpawnList = new ArrayList<>();
        List<String> spawnEntriesAlreadyProcessed = new ArrayList<>();

        for(List<String> oldListToProcess : phaseSpawnListOriginal){
            for(String spawnEntry : oldListToProcess) {
                if(spawnEntriesAlreadyProcessed.contains(spawnEntry)) continue;
                spawnEntriesAlreadyProcessed.add(spawnEntry);
                newSpawnList.add("[" + getPhaseListsContainingEntryAsString(spawnEntry, phaseSpawnListOriginal) + "]; " + spawnEntry);
            }
        }
        String[] phaseSpawnList = newSpawnList.toArray(new String[0]);

        double[] reinforcementSystemChance = {0.0, SRPConfigSystems.reinforcementSystemChanceOne, SRPConfigSystems.reinforcementSystemChanceTwo, SRPConfigSystems.reinforcementSystemChanceThree, SRPConfigSystems.reinforcementSystemChanceFour, SRPConfigSystems.reinforcementSystemChanceFive, SRPConfigSystems.reinforcementSystemChanceSix, SRPConfigSystems.reinforcementSystemChanceSeven, SRPConfigSystems.reinforcementSystemChanceEight, SRPConfigSystems.reinforcementSystemChanceNine, SRPConfigSystems.reinforcementSystemChanceTen};
        int[] phaseResidue = {0, SRPConfigSystems.phaseResidueOne, SRPConfigSystems.phaseResidueTwo, SRPConfigSystems.phaseResidueThree, SRPConfigSystems.phaseResidueFour, SRPConfigSystems.phaseResidueFive, SRPConfigSystems.phaseResidueSix, SRPConfigSystems.phaseResidueSeven, SRPConfigSystems.phaseResidueEight, SRPConfigSystems.phaseResidueNine, SRPConfigSystems.phaseResidueTen};
        String[] nexusGrowPenalty = {
                "0; 0; 0",
                SRPConfigSystems.beckonStageIGrowPenaltyOne + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyOne + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyOne,
                SRPConfigSystems.beckonStageIGrowPenaltyTwo + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyTwo + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyTwo,
                SRPConfigSystems.beckonStageIGrowPenaltyThree + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyThree + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyThree,
                SRPConfigSystems.beckonStageIGrowPenaltyFour + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyFour + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyFour,
                SRPConfigSystems.beckonStageIGrowPenaltyFive + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyFive + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyFive,
                SRPConfigSystems.beckonStageIGrowPenaltySix + "; " +SRPConfigSystems.beckonStageIIGrowPenaltySix + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltySix,
                SRPConfigSystems.beckonStageIGrowPenaltySeven + "; " +SRPConfigSystems.beckonStageIIGrowPenaltySeven + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltySeven,
                SRPConfigSystems.beckonStageIGrowPenaltyEight + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyEight + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyEight,
                SRPConfigSystems.beckonStageIGrowPenaltyNine + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyNine + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyNine,
                SRPConfigSystems.beckonStageIGrowPenaltyTen + "; " +SRPConfigSystems.beckonStageIIGrowPenaltyTen + "; " +SRPConfigSystems.beckonStageIIIGrowPenaltyTen
        };

        int[] phaseScentBonus = {SRPConfigSystems.phaseScentBonusZero,  SRPConfigSystems.phaseScentBonusOne,  SRPConfigSystems.phaseScentBonusTwo,  SRPConfigSystems.phaseScentBonusThree,  SRPConfigSystems.phaseScentBonusFour,  SRPConfigSystems.phaseScentBonusFive,  SRPConfigSystems.phaseScentBonusSix,  SRPConfigSystems.phaseScentBonusSeven,  SRPConfigSystems.phaseScentBonusEight,  SRPConfigSystems.phaseScentBonusNine,  SRPConfigSystems.phaseScentBonusTen};
        int[] phaseScentReaction = {SRPConfigSystems.phaseScentReactionZero,  SRPConfigSystems.phaseScentReactionOne,  SRPConfigSystems.phaseScentReactionTwo,  SRPConfigSystems.phaseScentReactionThree,  SRPConfigSystems.phaseScentReactionFour,  SRPConfigSystems.phaseScentReactionFive,  SRPConfigSystems.phaseScentReactionSix,  SRPConfigSystems.phaseScentReactionSeven,  SRPConfigSystems.phaseScentReactionEight,  SRPConfigSystems.phaseScentReactionNine,  SRPConfigSystems.phaseScentReactionTen};

        double[] mobSpawningCOTHChance = {0.0, SRPConfigSystems.mobSpawningCOTHChanceOne, SRPConfigSystems.mobSpawningCOTHChanceTwo, SRPConfigSystems.mobSpawningCOTHChanceThree, SRPConfigSystems.mobSpawningCOTHChanceFour, SRPConfigSystems.mobSpawningCOTHChanceFive, SRPConfigSystems.mobSpawningCOTHChanceSix, SRPConfigSystems.mobSpawningCOTHChanceSeven, SRPConfigSystems.mobSpawningCOTHChanceEight, SRPConfigSystems.mobSpawningCOTHChanceNine, SRPConfigSystems.mobSpawningCOTHChanceTen};
        double[] cropGrowStunned = {0.0, SRPConfigSystems.cropGrowStunnedOne, SRPConfigSystems.cropGrowStunnedTwo, SRPConfigSystems.cropGrowStunnedThree, SRPConfigSystems.cropGrowStunnedFour, SRPConfigSystems.cropGrowStunnedFive, SRPConfigSystems.cropGrowStunnedSix, SRPConfigSystems.cropGrowStunnedSeven, SRPConfigSystems.cropGrowStunnedEight, SRPConfigSystems.cropGrowStunnedNine, SRPConfigSystems.cropGrowStunnedTen};

        SRPMixins.CONFIG.get("general.More Phases", "Phase Points", SRPMixinsConfigHandler.morephases.phaseKills).set(phaseKills);
        SRPMixins.CONFIG.get("general.More Phases", "Phase Killcount Plus", SRPMixinsConfigHandler.morephases.phaseKillCountPlus).set(phaseKillCountPlus);
        SRPMixins.CONFIG.get("general.More Phases", "Phase Warning Message", SRPMixinsConfigHandler.morephases.phaseWarning).set(phaseWarning);
        SRPMixins.CONFIG.get("general.More Phases", "Phase Delay", SRPMixinsConfigHandler.morephases.phaseDelayTicks).set(phaseDelayTicks);
        SRPMixins.CONFIG.get("general.More Phases", "Sleep Penalty", SRPMixinsConfigHandler.morephases.sleepPenalty).set(sleepPenalty);
        SRPMixins.CONFIG.get("general.More Phases", "Spawning - Minimum Parasite Type ID", SRPMixinsConfigHandler.morephases.phaseMinParasiteID).set(phaseMinParasiteID);
        SRPMixins.CONFIG.get("general.More Phases", "Spawning - Maximum Parasite Type ID", SRPMixinsConfigHandler.morephases.phaseMaxParasiteID).set(phaseMaxParasiteID);
        SRPMixins.CONFIG.get("general.More Phases", "Spawning - Phase Spawn List", SRPMixinsConfigHandler.morephases.phaseSpawnList).set(phaseSpawnList);
        SRPMixins.CONFIG.get("general.More Phases", "Reinforcement System Chance", SRPMixinsConfigHandler.morephases.reinforcementSystemChance).set(reinforcementSystemChance);
        SRPMixins.CONFIG.get("general.More Phases", "Nexus Grow Stun Chance", SRPMixinsConfigHandler.morephases.nexusGrowPenalty).set(nexusGrowPenalty);
        SRPMixins.CONFIG.get("general.More Phases", "Odds Residue Spawns Beckon", SRPMixinsConfigHandler.morephases.phaseResidue).set(phaseResidue);
        SRPMixins.CONFIG.get("general.More Phases", "Scent Death Bonus", SRPMixinsConfigHandler.morephases.phaseScentBonus).set(phaseScentBonus);
        SRPMixins.CONFIG.get("general.More Phases", "Scent Reaction Bonus", SRPMixinsConfigHandler.morephases.phaseScentReaction).set(phaseScentReaction);
        SRPMixins.CONFIG.get("general.More Phases", "Mobs Spawn With COTH Chance", SRPMixinsConfigHandler.morephases.mobSpawningCOTHChance).set(mobSpawningCOTHChance);
        SRPMixins.CONFIG.get("general.More Phases", "Crop Grow Stunned", SRPMixinsConfigHandler.morephases.cropGrowStunned).set(cropGrowStunned);

        SRPMixinsConfigHandler.morephases.phaseKills = phaseKills;
        SRPMixinsConfigHandler.morephases.phaseKillCountPlus = phaseKillCountPlus;
        SRPMixinsConfigHandler.morephases.phaseWarning = phaseWarning;
        SRPMixinsConfigHandler.morephases.phaseDelayTicks = phaseDelayTicks;
        SRPMixinsConfigHandler.morephases.sleepPenalty = sleepPenalty;
        SRPMixinsConfigHandler.morephases.phaseMinParasiteID = phaseMinParasiteID;
        SRPMixinsConfigHandler.morephases.phaseMaxParasiteID = phaseMaxParasiteID;
        SRPMixinsConfigHandler.morephases.phaseSpawnList = phaseSpawnList;
        SRPMixinsConfigHandler.morephases.reinforcementSystemChance = reinforcementSystemChance;
        SRPMixinsConfigHandler.morephases.nexusGrowPenalty = nexusGrowPenalty;
        SRPMixinsConfigHandler.morephases.phaseResidue = phaseResidue;
        SRPMixinsConfigHandler.morephases.phaseScentBonus = phaseScentBonus;
        SRPMixinsConfigHandler.morephases.phaseScentReaction = phaseScentReaction;
        SRPMixinsConfigHandler.morephases.mobSpawningCOTHChance = mobSpawningCOTHChance;
        SRPMixinsConfigHandler.morephases.cropGrowStunned = cropGrowStunned;

        SRPMixins.CONFIG.save();
    }

    private static String getPhaseListsContainingEntryAsString(String spawnEntry, List<List<String>> phaseSpawnListOriginal) {
        List<Integer> phases = new ArrayList<>();
        String phasesToSpawnIn = "";
        for(int i = 0; i <= phaseSpawnListOriginal.size(); i++) {
            //we go one index further to always clear the list at the end
            if(i < phaseSpawnListOriginal.size() && phaseSpawnListOriginal.get(i).contains(spawnEntry)) {
                phases.add(i);
            }
            //Should trigger on the first spawn list that doesn't contain the current entry
            else if(!phases.isEmpty()){
                //startPhase - endPhase
                if(phases.size() >= 2) phasesToSpawnIn += phases.get(0) + " - " + phases.get(phases.size()-1) + ", ";
                //single phases
                else phasesToSpawnIn += phases.get(0) + ", ";
                phases.clear();
            }
        }
        return phasesToSpawnIn.substring(0, phasesToSpawnIn.length()-2); //remove last ", "
    }

    //The bottom part only runs once to grab all the SRP mob configs and put them into a list
    public static boolean readMobConfigs() {
        String unused = "---";
        for (String s : SRPMixinsConfigHandler.mobConfig.mobConfig) {
            List<String> split = Arrays.stream(s.split("\t")).map(String::trim).collect(Collectors.toList());
            if (split.size() < 7)
                SRPMixins.LOGGER.warn("SRPMixins unable to parse SRPMixins Mob Config entry, too few entries, provided was {}", s);
            else {
                try {
                    Boolean enabled = split.get(0).equals(unused) ? null : Boolean.parseBoolean(split.get(0));
                    Float healthMulti = split.get(1).equals(unused) ? null : Float.parseFloat(split.get(1));
                    Float dmgMulti = split.get(2).equals(unused) ? null : Float.parseFloat(split.get(2));
                    Float armorMulti = split.get(3).equals(unused) ? null : Float.parseFloat(split.get(3));
                    Float kbresMulti = split.get(4).equals(unused) ? null : Float.parseFloat(split.get(4));
                    Integer spawnRate = split.get(5).equals(unused) ? null : Integer.parseInt(split.get(5));
                    String mobName = split.get(6);
                    srpMobConfig.put(mobName, new SRPMobConfig(enabled, dmgMulti, armorMulti, healthMulti, kbresMulti, spawnRate));
                } catch (Exception e) {
                    SRPMixins.LOGGER.warn("SRPMixins unable to parse SRPMixins Mob Config entry, expected numbers, provided was {}", s);
                }
            }
        }
        return !srpMobConfig.isEmpty();
    }

    public static void initMobConfigs() {
        srpMobConfig.put("anc_dreadnaut", new SRPMobConfig(SRPConfigMobs.oroncoEnabled, SRPConfigMobs.oroncoDamageMultiplier, SRPConfigMobs.oroncoArmorMultiplier, SRPConfigMobs.oroncoHealthMultiplier, SRPConfigMobs.oroncoKDResistanceMultiplier, SRPConfigMobs.oroncoSpawnRate));
        srpMobConfig.put("anc_overlord", new SRPMobConfig(SRPConfigMobs.terlaEnabled, SRPConfigMobs.terlaDamageMultiplier, SRPConfigMobs.terlaArmorMultiplier, SRPConfigMobs.terlaHealthMultiplier, SRPConfigMobs.terlaKDResistanceMultiplier, SRPConfigMobs.terlaSpawnRate));
        srpMobConfig.put("anc_pod", new SRPMobConfig(null, SRPConfigMobs.pod1DamageMultiplier, SRPConfigMobs.pod1ArmorMultiplier, SRPConfigMobs.pod1HealthMultiplier, null, null));
        srpMobConfig.put("pri_arachnida", new SRPMobConfig(SRPConfigMobs.arachnidaEnabled, SRPConfigMobs.arachnidaDamageMultiplier, SRPConfigMobs.arachnidaArmorMultiplier, SRPConfigMobs.arachnidaHealthMultiplier, SRPConfigMobs.arachnidaKDResistanceMultiplier, SRPConfigMobs.arachnidaSpawnRate));
        srpMobConfig.put("bogle", new SRPMobConfig(SRPConfigMobs.lenciaEnabled, SRPConfigMobs.lenciaDamageMultiplier, SRPConfigMobs.lenciaArmorMultiplier, SRPConfigMobs.lenciaHealthMultiplier, SRPConfigMobs.lenciaKDResistanceMultiplier, SRPConfigMobs.lenciaSpawnRate));
        srpMobConfig.put("bomber_heavy", new SRPMobConfig(SRPConfigMobs.jinjoEnabled, SRPConfigMobs.jinjoDamageMultiplier, SRPConfigMobs.jinjoArmorMultiplier, SRPConfigMobs.jinjoHealthMultiplier, SRPConfigMobs.jinjoKDResistanceMultiplier, SRPConfigMobs.jinjoSpawnRate));
        srpMobConfig.put("bomber_light", new SRPMobConfig(SRPConfigMobs.ombooEnabled, SRPConfigMobs.ombooDamageMultiplier, SRPConfigMobs.ombooArmorMultiplier, SRPConfigMobs.ombooHealthMultiplier, SRPConfigMobs.ombooKDResistanceMultiplier, SRPConfigMobs.ombooSpawnRate));
        srpMobConfig.put("buglin", new SRPMobConfig(SRPConfigMobs.lodoEnabled, SRPConfigMobs.lodoDamageMultiplier, SRPConfigMobs.lodoArmorMultiplier, SRPConfigMobs.lodoHealthMultiplier, SRPConfigMobs.lodoKDResistanceMultiplier, SRPConfigMobs.lodoSpawnRate));
        srpMobConfig.put("carrier_colony", new SRPMobConfig(SRPConfigMobs.vestaEnabled, SRPConfigMobs.vestaDamageMultiplier, SRPConfigMobs.vestaArmorMultiplier, SRPConfigMobs.vestaHealthMultiplier, SRPConfigMobs.vestaKDResistanceMultiplier, SRPConfigMobs.vestaSpawnRate));
        srpMobConfig.put("carrier_flying", new SRPMobConfig(SRPConfigMobs.butholEnabled, SRPConfigMobs.butholDamageMultiplier, SRPConfigMobs.butholArmorMultiplier, SRPConfigMobs.butholHealthMultiplier, SRPConfigMobs.butholKDResistanceMultiplier, SRPConfigMobs.butholSpawnRate));
        srpMobConfig.put("carrier_heavy", new SRPMobConfig(SRPConfigMobs.ratholEnabled, SRPConfigMobs.ratholDamageMultiplier, SRPConfigMobs.ratholArmorMultiplier, SRPConfigMobs.ratholHealthMultiplier, SRPConfigMobs.ratholKDResistanceMultiplier, SRPConfigMobs.ratholSpawnRate));
        srpMobConfig.put("carrier_light", new SRPMobConfig(SRPConfigMobs.gotholEnabled, SRPConfigMobs.gotholDamageMultiplier, SRPConfigMobs.gotholArmorMultiplier, SRPConfigMobs.gotholHealthMultiplier, SRPConfigMobs.gotholKDResistanceMultiplier, SRPConfigMobs.gotholSpawnRate));
        srpMobConfig.put("crux", new SRPMobConfig(SRPConfigMobs.cruxaEnabled, SRPConfigMobs.cruxaDamageMultiplier, SRPConfigMobs.cruxaArmorMultiplier, SRPConfigMobs.cruxaHealthMultiplier, SRPConfigMobs.cruxaKDResistanceMultiplier, SRPConfigMobs.cruxaSpawnRate));
        srpMobConfig.put("draconite", new SRPMobConfig(SRPConfigMobs.hebluEnabled, SRPConfigMobs.hebluDamageMultiplier, SRPConfigMobs.hebluArmorMultiplier, SRPConfigMobs.hebluHealthMultiplier, SRPConfigMobs.hebluKDResistanceMultiplier, SRPConfigMobs.hebluSpawnRate));
        srpMobConfig.put("fer_bear", new SRPMobConfig(SRPConfigMobs.ferbearEnabled, SRPConfigMobs.ferbearDamageMultiplier, SRPConfigMobs.ferbearArmorMultiplier, SRPConfigMobs.ferbearHealthMultiplier, SRPConfigMobs.ferbearKDResistanceMultiplier, SRPConfigMobs.ferbearSpawnRate));
        srpMobConfig.put("fer_cow", new SRPMobConfig(SRPConfigMobs.fercowEnabled, SRPConfigMobs.fercowDamageMultiplier, SRPConfigMobs.fercowArmorMultiplier, SRPConfigMobs.fercowHealthMultiplier, SRPConfigMobs.fercowKDResistanceMultiplier, SRPConfigMobs.fercowSpawnRate));
        srpMobConfig.put("fer_enderman", new SRPMobConfig(SRPConfigMobs.ferendermanEnabled, SRPConfigMobs.ferendermanDamageMultiplier, SRPConfigMobs.ferendermanArmorMultiplier, SRPConfigMobs.ferendermanHealthMultiplier, SRPConfigMobs.ferendermanKDResistanceMultiplier, SRPConfigMobs.ferendermanSpawnRate));
        srpMobConfig.put("fer_horse", new SRPMobConfig(SRPConfigMobs.ferhorseEnabled, SRPConfigMobs.ferhorseDamageMultiplier, SRPConfigMobs.ferhorseArmorMultiplier, SRPConfigMobs.ferhorseHealthMultiplier, SRPConfigMobs.ferhorseKDResistanceMultiplier, SRPConfigMobs.ferhorseSpawnRate));
        srpMobConfig.put("fer_human", new SRPMobConfig(SRPConfigMobs.ferhumanEnabled, SRPConfigMobs.ferhumanDamageMultiplier, SRPConfigMobs.ferhumanArmorMultiplier, SRPConfigMobs.ferhumanHealthMultiplier, SRPConfigMobs.ferhumanKDResistanceMultiplier, SRPConfigMobs.ferhumanSpawnRate));
        srpMobConfig.put("fer_pig", new SRPMobConfig(SRPConfigMobs.ferpigEnabled, SRPConfigMobs.ferpigDamageMultiplier, SRPConfigMobs.ferpigArmorMultiplier, SRPConfigMobs.ferpigHealthMultiplier, SRPConfigMobs.ferpigKDResistanceMultiplier, SRPConfigMobs.ferpigSpawnRate));
        srpMobConfig.put("fer_sheep", new SRPMobConfig(SRPConfigMobs.fersheepEnabled, SRPConfigMobs.fersheepDamageMultiplier, SRPConfigMobs.fersheepArmorMultiplier, SRPConfigMobs.fersheepHealthMultiplier, SRPConfigMobs.fersheepKDResistanceMultiplier, SRPConfigMobs.fersheepSpawnRate));
        srpMobConfig.put("fer_villager", new SRPMobConfig(SRPConfigMobs.fervillagerEnabled, SRPConfigMobs.fervillagerDamageMultiplier, SRPConfigMobs.fervillagerArmorMultiplier, SRPConfigMobs.fervillagerHealthMultiplier, SRPConfigMobs.fervillagerKDResistanceMultiplier, SRPConfigMobs.fervillagerSpawnRate));
        srpMobConfig.put("fer_wolf", new SRPMobConfig(SRPConfigMobs.ferwolfEnabled, SRPConfigMobs.ferwolfDamageMultiplier, SRPConfigMobs.ferwolfArmorMultiplier, SRPConfigMobs.ferwolfHealthMultiplier, SRPConfigMobs.ferwolfKDResistanceMultiplier, SRPConfigMobs.ferwolfSpawnRate));
        srpMobConfig.put("gnat", new SRPMobConfig(SRPConfigMobs.ataEnabled, SRPConfigMobs.ataDamageMultiplier, SRPConfigMobs.ataArmorMultiplier, SRPConfigMobs.ataHealthMultiplier, SRPConfigMobs.ataKDResistanceMultiplier, SRPConfigMobs.ataSpawnRate));
        srpMobConfig.put("grunt", new SRPMobConfig(SRPConfigMobs.flogEnabled, SRPConfigMobs.flogDamageMultiplier, SRPConfigMobs.flogArmorMultiplier, SRPConfigMobs.flogHealthMultiplier, SRPConfigMobs.flogKDResistanceMultiplier, SRPConfigMobs.flogSpawnRate));
        srpMobConfig.put("haunter", new SRPMobConfig(SRPConfigMobs.pheonEnabled, SRPConfigMobs.pheonDamageMultiplier, SRPConfigMobs.pheonArmorMultiplier, SRPConfigMobs.pheonHealthMultiplier, SRPConfigMobs.pheonKDResistanceMultiplier, SRPConfigMobs.pheonSpawnRate));
        srpMobConfig.put("heed", new SRPMobConfig(SRPConfigMobs.heedEnabled, SRPConfigMobs.heedDamageMultiplier, SRPConfigMobs.heedArmorMultiplier, SRPConfigMobs.heedHealthMultiplier, SRPConfigMobs.heedKDResistanceMultiplier, SRPConfigMobs.heedSpawnRate));
        srpMobConfig.put("hostii", new SRPMobConfig(SRPConfigMobs.herdEnabled, SRPConfigMobs.herdDamageMultiplier, SRPConfigMobs.herdArmorMultiplier, SRPConfigMobs.herdHealthMultiplier, SRPConfigMobs.herdKDResistanceMultiplier, SRPConfigMobs.herdSpawnRate));
        srpMobConfig.put("hi_blaze", new SRPMobConfig(SRPConfigMobs.hiblazeEnabled, SRPConfigMobs.hiblazeDamageMultiplier, SRPConfigMobs.hiblazeArmorMultiplier, SRPConfigMobs.hiblazeHealthMultiplier, SRPConfigMobs.hiblazeKDResistanceMultiplier, SRPConfigMobs.hiblazeSpawnRate));
        srpMobConfig.put("hi_golem", new SRPMobConfig(SRPConfigMobs.higolemEnabled, SRPConfigMobs.higolemDamageMultiplier, SRPConfigMobs.higolemArmorMultiplier, SRPConfigMobs.higolemHealthMultiplier, SRPConfigMobs.higolemKDResistanceMultiplier, SRPConfigMobs.higolemSpawnRate));
        srpMobConfig.put("hi_skeleton", new SRPMobConfig(SRPConfigMobs.hiskeletonEnabled, SRPConfigMobs.hiskeletonDamageMultiplier, SRPConfigMobs.hiskeletonArmorMultiplier, SRPConfigMobs.hiskeletonHealthMultiplier, SRPConfigMobs.hiskeletonKDResistanceMultiplier, SRPConfigMobs.hiskeletonSpawnRate));
        srpMobConfig.put("host", new SRPMobConfig(SRPConfigMobs.hostEnabled, SRPConfigMobs.hostDamageMultiplier, SRPConfigMobs.hostArmorMultiplier, SRPConfigMobs.hostHealthMultiplier, SRPConfigMobs.hostKDResistanceMultiplier, SRPConfigMobs.hostSpawnRate));
        srpMobConfig.put("incompleteform_medium", new SRPMobConfig(SRPConfigMobs.inhooMEnabled, SRPConfigMobs.inhooMDamageMultiplier, SRPConfigMobs.inhooMArmorMultiplier, SRPConfigMobs.inhooMHealthMultiplier, SRPConfigMobs.inhooMKDResistanceMultiplier, SRPConfigMobs.inhooMSpawnRate));
        srpMobConfig.put("incompleteform_small", new SRPMobConfig(SRPConfigMobs.inhooSEnabled, SRPConfigMobs.inhooSDamageMultiplier, SRPConfigMobs.inhooSArmorMultiplier, SRPConfigMobs.inhooSHealthMultiplier, SRPConfigMobs.inhooSKDResistanceMultiplier, SRPConfigMobs.inhooSSpawnRate));
        srpMobConfig.put("kyphosis", new SRPMobConfig(SRPConfigMobs.tonroEnabled, SRPConfigMobs.tonroDamageMultiplier, SRPConfigMobs.tonroArmorMultiplier, SRPConfigMobs.tonroHealthMultiplier, null, null));
        srpMobConfig.put("mangler", new SRPMobConfig(SRPConfigMobs.nuuhEnabled, SRPConfigMobs.nuuhDamageMultiplier, SRPConfigMobs.nuuhArmorMultiplier, SRPConfigMobs.nuuhHealthMultiplier, SRPConfigMobs.nuuhKDResistanceMultiplier, SRPConfigMobs.nuuhSpawnRate));
        srpMobConfig.put("marauder", new SRPMobConfig(SRPConfigMobs.esorEnabled, SRPConfigMobs.esorDamageMultiplier, SRPConfigMobs.esorArmorMultiplier, SRPConfigMobs.esorHealthMultiplier, SRPConfigMobs.esorKDResistanceMultiplier, SRPConfigMobs.esorSpawnRate));
        srpMobConfig.put("monarch", new SRPMobConfig(SRPConfigMobs.orchEnabled, SRPConfigMobs.orchDamageMultiplier, SRPConfigMobs.orchArmorMultiplier, SRPConfigMobs.orchHealthMultiplier, SRPConfigMobs.orchKDResistanceMultiplier, SRPConfigMobs.orchSpawnRate));
        srpMobConfig.put("overseer", new SRPMobConfig(SRPConfigMobs.alafhaEnabled, SRPConfigMobs.alafhaDamageMultiplier, SRPConfigMobs.alafhaArmorMultiplier, SRPConfigMobs.alafhaHealthMultiplier, SRPConfigMobs.alafhaKDResistanceMultiplier, SRPConfigMobs.alafhaSpawnRate));
        srpMobConfig.put("pri_bolster", new SRPMobConfig(SRPConfigMobs.zetmoEnabled, SRPConfigMobs.zetmoDamageMultiplier, SRPConfigMobs.zetmoArmorMultiplier, SRPConfigMobs.zetmoHealthMultiplier, SRPConfigMobs.zetmoKDResistanceMultiplier, SRPConfigMobs.zetmoSpawnRate));
        srpMobConfig.put("pri_devourer", new SRPMobConfig(SRPConfigMobs.lumEnabled, SRPConfigMobs.lumDamageMultiplier, SRPConfigMobs.lumArmorMultiplier, SRPConfigMobs.lumHealthMultiplier, SRPConfigMobs.lumKDResistanceMultiplier, SRPConfigMobs.lumSpawnRate));
        srpMobConfig.put("pri_longarms", new SRPMobConfig(SRPConfigMobs.shycoEnabled, SRPConfigMobs.shycoDamageMultiplier, SRPConfigMobs.shycoArmorMultiplier, SRPConfigMobs.shycoHealthMultiplier, SRPConfigMobs.shycoKDResistanceMultiplier, SRPConfigMobs.shycoSpawnRate));
        srpMobConfig.put("pri_manducater", new SRPMobConfig(SRPConfigMobs.hullEnabled, SRPConfigMobs.hullDamageMultiplier, SRPConfigMobs.hullArmorMultiplier, SRPConfigMobs.hullHealthMultiplier, SRPConfigMobs.hullKDResistanceMultiplier, SRPConfigMobs.hullSpawnRate));
        srpMobConfig.put("pri_reeker", new SRPMobConfig(SRPConfigMobs.noglaEnabled, SRPConfigMobs.noglaDamageMultiplier, SRPConfigMobs.noglaArmorMultiplier, SRPConfigMobs.noglaHealthMultiplier, SRPConfigMobs.noglaKDResistanceMultiplier, SRPConfigMobs.noglaSpawnRate));
        srpMobConfig.put("pri_summoner", new SRPMobConfig(SRPConfigMobs.canraEnabled, SRPConfigMobs.canraDamageMultiplier, SRPConfigMobs.canraArmorMultiplier, SRPConfigMobs.canraHealthMultiplier, SRPConfigMobs.canraKDResistanceMultiplier, SRPConfigMobs.canraSpawnRate));
        srpMobConfig.put("pri_tozoon", new SRPMobConfig(SRPConfigMobs.wymoEnabled, SRPConfigMobs.wymoDamageMultiplier, SRPConfigMobs.wymoArmorMultiplier, SRPConfigMobs.wymoHealthMultiplier, SRPConfigMobs.wymoKDResistanceMultiplier, SRPConfigMobs.wymoSpawnRate));
        srpMobConfig.put("pri_vermin", new SRPMobConfig(SRPConfigMobs.ikiEnabled, SRPConfigMobs.ikiDamageMultiplier, SRPConfigMobs.ikiArmorMultiplier, SRPConfigMobs.ikiHealthMultiplier, SRPConfigMobs.ikiKDResistanceMultiplier, SRPConfigMobs.ikiSpawnRate));
        srpMobConfig.put("pri_yelloweye", new SRPMobConfig(SRPConfigMobs.emanaEnabled, SRPConfigMobs.emanaDamageMultiplier, SRPConfigMobs.emanaArmorMultiplier, SRPConfigMobs.emanaHealthMultiplier, SRPConfigMobs.emanaKDResistanceMultiplier, SRPConfigMobs.emanaSpawnRate));
        srpMobConfig.put("rupter", new SRPMobConfig(SRPConfigMobs.mudoEnabled, SRPConfigMobs.mudoDamageMultiplier, SRPConfigMobs.mudoArmorMultiplier, SRPConfigMobs.mudoHealthMultiplier, SRPConfigMobs.mudoKDResistanceMultiplier, SRPConfigMobs.mudoSpawnRate));
        srpMobConfig.put("seizer", new SRPMobConfig(SRPConfigMobs.nakEnabled, SRPConfigMobs.nakDamageMultiplier, SRPConfigMobs.nakArmorMultiplier, SRPConfigMobs.nakHealthMultiplier, null, SRPConfigMobs.nakSpawnRate));
        srpMobConfig.put("sentry", new SRPMobConfig(SRPConfigMobs.unvoEnabled, SRPConfigMobs.unvoDamageMultiplier, SRPConfigMobs.unvoArmorMultiplier, SRPConfigMobs.unvoHealthMultiplier, null, null));
        srpMobConfig.put("sim_adventurer", new SRPMobConfig(SRPConfigMobs.infadventurerEnabled, SRPConfigMobs.infadventurerDamageMultiplier, SRPConfigMobs.infadventurerArmorMultiplier, SRPConfigMobs.infadventurerHealthMultiplier, SRPConfigMobs.infadventurerKDResistanceMultiplier, SRPConfigMobs.infadventurerSpawnRate));
        srpMobConfig.put("sim_bear", new SRPMobConfig(SRPConfigMobs.infbearEnabled, SRPConfigMobs.infbearDamageMultiplier, SRPConfigMobs.infbearArmorMultiplier, SRPConfigMobs.infbearHealthMultiplier, SRPConfigMobs.infbearKDResistanceMultiplier, SRPConfigMobs.infbearSpawnRate));
        srpMobConfig.put("sim_bigspider", new SRPMobConfig(SRPConfigMobs.dorpaEnabled, SRPConfigMobs.dorpaDamageMultiplier, SRPConfigMobs.dorpaArmorMultiplier, SRPConfigMobs.dorpaHealthMultiplier, SRPConfigMobs.dorpaKDResistanceMultiplier, SRPConfigMobs.dorpaSpawnRate));
        srpMobConfig.put("sim_cow", new SRPMobConfig(SRPConfigMobs.infcowEnabled, SRPConfigMobs.infcowDamageMultiplier, SRPConfigMobs.infcowArmorMultiplier, SRPConfigMobs.infcowHealthMultiplier, SRPConfigMobs.infcowKDResistanceMultiplier, SRPConfigMobs.infcowSpawnRate));
        srpMobConfig.put("sim_dragone", new SRPMobConfig(SRPConfigMobs.infdragoneEnabled, SRPConfigMobs.infdragoneDamageMultiplier, SRPConfigMobs.infdragoneArmorMultiplier, SRPConfigMobs.infdragoneHealthMultiplier, SRPConfigMobs.infdragoneKDResistanceMultiplier, SRPConfigMobs.infdragoneSpawnRate));
        srpMobConfig.put("sim_enderman", new SRPMobConfig(SRPConfigMobs.infendermanEnabled, SRPConfigMobs.infendermanDamageMultiplier, SRPConfigMobs.infendermanArmorMultiplier, SRPConfigMobs.infendermanHealthMultiplier, SRPConfigMobs.infendermanKDResistanceMultiplier, SRPConfigMobs.infendermanSpawnRate));
        srpMobConfig.put("sim_horse", new SRPMobConfig(SRPConfigMobs.infhorseEnabled, SRPConfigMobs.infhorseDamageMultiplier, SRPConfigMobs.infhorseArmorMultiplier, SRPConfigMobs.infhorseHealthMultiplier, SRPConfigMobs.infhorseKDResistanceMultiplier, SRPConfigMobs.infhorseSpawnRate));
        srpMobConfig.put("sim_human", new SRPMobConfig(SRPConfigMobs.infhumanEnabled, SRPConfigMobs.infhumanDamageMultiplier, SRPConfigMobs.infhumanArmorMultiplier, SRPConfigMobs.infhumanHealthMultiplier, SRPConfigMobs.infhumanKDResistanceMultiplier, SRPConfigMobs.infhumanSpawnRate));
        srpMobConfig.put("sim_pig", new SRPMobConfig(SRPConfigMobs.infpigEnabled, SRPConfigMobs.infpigDamageMultiplier, SRPConfigMobs.infpigArmorMultiplier, SRPConfigMobs.infpigHealthMultiplier, SRPConfigMobs.infpigKDResistanceMultiplier, SRPConfigMobs.infpigSpawnRate));
        srpMobConfig.put("sim_sheep", new SRPMobConfig(SRPConfigMobs.infsheepEnabled, SRPConfigMobs.infsheepDamageMultiplier, SRPConfigMobs.infsheepArmorMultiplier, SRPConfigMobs.infsheepHealthMultiplier, SRPConfigMobs.infsheepKDResistanceMultiplier, SRPConfigMobs.infsheepSpawnRate));
        srpMobConfig.put("sim_squid", new SRPMobConfig(SRPConfigMobs.infsquidEnabled, SRPConfigMobs.infsquidDamageMultiplier, SRPConfigMobs.infsquidArmorMultiplier, SRPConfigMobs.infsquidHealthMultiplier, SRPConfigMobs.infsquidKDResistanceMultiplier, SRPConfigMobs.infsquidSpawnRate));
        srpMobConfig.put("sim_villager", new SRPMobConfig(SRPConfigMobs.infvillagerEnabled, SRPConfigMobs.infvillagerDamageMultiplier, SRPConfigMobs.infvillagerArmorMultiplier, SRPConfigMobs.infvillagerHealthMultiplier, SRPConfigMobs.infvillagerKDResistanceMultiplier, SRPConfigMobs.infvillagerSpawnRate));
        srpMobConfig.put("sim_wolf", new SRPMobConfig(SRPConfigMobs.infwolfEnabled, SRPConfigMobs.infwolfDamageMultiplier, SRPConfigMobs.infwolfArmorMultiplier, SRPConfigMobs.infwolfHealthMultiplier, SRPConfigMobs.infwolfKDResistanceMultiplier, SRPConfigMobs.infwolfSpawnRate));
        srpMobConfig.put("succor", new SRPMobConfig(SRPConfigMobs.flamEnabled, null, SRPConfigMobs.flamArmorMultiplier, SRPConfigMobs.flamHealthMultiplier, SRPConfigMobs.flamKDResistanceMultiplier, null));
        srpMobConfig.put("thrall", new SRPMobConfig(SRPConfigMobs.thrallEnabled, SRPConfigMobs.thrallDamageMultiplier, SRPConfigMobs.thrallArmorMultiplier, SRPConfigMobs.thrallHealthMultiplier, SRPConfigMobs.thrallKDResistanceMultiplier, SRPConfigMobs.thrallSpawnRate));
        srpMobConfig.put("vigilante", new SRPMobConfig(SRPConfigMobs.angedEnabled, SRPConfigMobs.angedDamageMultiplier, SRPConfigMobs.angedArmorMultiplier, SRPConfigMobs.angedHealthMultiplier, SRPConfigMobs.angedKDResistanceMultiplier, SRPConfigMobs.angedSpawnRate));
        srpMobConfig.put("warden", new SRPMobConfig(SRPConfigMobs.ganroEnabled, SRPConfigMobs.ganroDamageMultiplier, SRPConfigMobs.ganroArmorMultiplier, SRPConfigMobs.ganroHealthMultiplier, SRPConfigMobs.ganroKDResistanceMultiplier, SRPConfigMobs.ganroSpawnRate));
        srpMobConfig.put("worker", new SRPMobConfig(SRPConfigMobs.kolEnabled, SRPConfigMobs.kolDamageMultiplier, SRPConfigMobs.kolArmorMultiplier, SRPConfigMobs.kolHealthMultiplier, SRPConfigMobs.kolKDResistanceMultiplier, SRPConfigMobs.kolSpawnRate));
        srpMobConfig.put("wraith", new SRPMobConfig(SRPConfigMobs.elviaEnabled, SRPConfigMobs.elviaDamageMultiplier, SRPConfigMobs.elviaArmorMultiplier, SRPConfigMobs.elviaHealthMultiplier, SRPConfigMobs.elviaKDResistanceMultiplier, SRPConfigMobs.elviaSpawnRate));

        String unused = "---";
        List<String> configList = new ArrayList<>();
        for (Map.Entry<String, SRPMobConfig> entry : srpMobConfig.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList()))
            configList.add(
                    (entry.getValue().enabled == null ? unused : entry.getValue().enabled) + "\t" +
                    (entry.getValue().healthMulti == null ? unused : entry.getValue().healthMulti) + "\t" +
                    (entry.getValue().dmgMulti == null ? unused : entry.getValue().dmgMulti) + "\t" +
                    (entry.getValue().armorMulti == null ? unused : entry.getValue().armorMulti) + "\t" +
                    (entry.getValue().kbresMulti == null ? unused : entry.getValue().kbresMulti) + "\t" +
                    (entry.getValue().spawnWeight == null ? unused : entry.getValue().spawnWeight) + "\t" +
                    entry.getKey()
            );

        String[] configArray = configList.toArray(configList.toArray(new String[0]));
        SRPMixins.CONFIG.get("general.SRP Mob Config", "SRP Mob Config", SRPMixinsConfigHandler.mobConfig.mobConfig).set(configArray);
        SRPMixinsConfigHandler.mobConfig.mobConfig = configArray;

        SRPMixins.CONFIG.save();
    }

    private static class SRPMobConfig {
        public final Boolean enabled;
        public final Float dmgMulti, armorMulti, healthMulti, kbresMulti;
        public final Integer spawnWeight;

        public SRPMobConfig(Boolean enabled, Float dmgMulti, Float armorMulti, Float healthMulti, Float kbresMulti, Integer spawnWeight) {
            this.enabled = enabled;
            this.dmgMulti = dmgMulti;
            this.armorMulti = armorMulti;
            this.healthMulti = healthMulti;
            this.kbresMulti = kbresMulti;
            this.spawnWeight = spawnWeight;
        }
    }

    public static float getMobConfigDamage(String paraName) {
        return srpMobConfig.get(paraName).dmgMulti;
    }

    public static float getMobConfigArmor(String paraName) {
        return srpMobConfig.get(paraName).armorMulti;
    }

    public static float getMobConfigHealth(String paraName) {
        return srpMobConfig.get(paraName).healthMulti;
    }

    public static float getMobConfigKBRes(String paraName) {
        return srpMobConfig.get(paraName).kbresMulti;
    }

    public static int getMobConfigSpawnWeight(String paraName) {
        return srpMobConfig.get(paraName).spawnWeight;
    }

    public static boolean getMobConfigEnabled(String paraName) {
        return srpMobConfig.get(paraName).enabled;
    }

    //---------------------------- CONVERSION PHASE LOCK ----------------------------

    public static int getConversionPhaseLock(String mobIn, String mobOut) {
        if (!conversionPathwayLocks.containsKey(mobIn) || !conversionPathwayLocks.get(mobIn).containsKey(mobOut)){
            if(SRPMixinsConfigHandler.spawns.autoFillConversionRules)
                setConversionPathwayLock(mobIn, mobOut, -2);
            return -2;
        }
        return conversionPathwayLocks.get(mobIn).get(mobOut);
    }

    private static void setConversionPathwayLock(String mobIn, String mobOut, int defaultValue) {
        if(!conversionPathwayLocks.containsKey(mobIn))
            conversionPathwayLocks.put(mobIn, new HashMap<>());
        conversionPathwayLocks.get(mobIn).put(mobOut, defaultValue);
    }

    public static void readConversionLockConfig(){
        for(String s : SRPMixinsConfigHandler.spawns.conversionRules){
            String[] split = s.split(",");
            if(split.length < 3)
                SRPMixins.LOGGER.warn("SRPMixins unable to parse conversion pathway line, too few arguments (expected 3): {}", s);
            else {
                try {
                    String mobIn = split[0].trim().replace("srparasites:","");
                    String mobOut = split[1].trim().replace("srparasites:","");
                    int phase = Integer.parseInt(split[2].trim());
                    setConversionPathwayLock(mobIn, mobOut, phase);
                } catch (Exception e) {
                    SRPMixins.LOGGER.warn("SRPMixins unable to parse conversion pathway line, expected integer in last entry, provided was {}", s);
                }
            }
        }
    }

    public static void writeConversionLockConfig(){
        int mapEntries = 0;
        for(Map<String, Integer> v : conversionPathwayLocks.values())
            mapEntries += v.size();

        if(mapEntries > SRPMixinsConfigHandler.spawns.conversionRules.length){
            List<String> configList = new ArrayList<>();
            for(Map.Entry<String, Map<String, Integer>> bigEntry : conversionPathwayLocks.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList())) {
                for (Map.Entry<String, Integer> smallEntry : bigEntry.getValue().entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList())) {
                    String mobIn = bigEntry.getKey();
                    String mobOut = smallEntry.getKey();
                    Integer phase = smallEntry.getValue();
                    configList.add(mobIn + ", " + mobOut + ", " + phase);
                }
            }
            SRPMixins.CONFIG.get("general.Spawning", "Conversion Phase Lock Rules", SRPMixinsConfigHandler.spawns.conversionRules).set(configList.toArray(new String[0]));
            SRPMixins.CONFIG.save();
        }
    }
}
