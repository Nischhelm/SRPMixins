package srpmixins.config;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import srpmixins.SRPMixins;

import java.util.*;

public class SRPMixinsConfigProvider {
    public static final Map<String, Integer> mobNameToParaIdMap = new HashMap<>();
    public static final Map<String, List<Integer>> mobGroupToParaIdMap = new HashMap<>();

    public static Map<Integer,Float> dimensionHealthMultipliers = new HashMap<>();
    public static Map<Integer,Float> dimensionDmgMultipliers = new HashMap<>();
    public static Map<Integer,Float> dimensionArmorMultipliers = new HashMap<>();
    public static Map<Integer,Float> dimensionKBResMultipliers = new HashMap<>();
    public static Map<Integer,Float> dimensionDropMultipliers = new HashMap<>();
    public static Map<Integer,Float> dimensionMobCapMultipliers = new HashMap<>();
    public static Map<Integer, ArrayList<String>> biomeSpawningBlacklists = new HashMap<>();
    public static final Map<String, Byte> biomeStartPhases = new HashMap<>();
    public static final List<Integer> chunkPhasesDimensionBlacklist = new ArrayList<>();

    public static float playerNeedlerMulti = 0.4F;

    public static List<String> whiteListedDeterrents;

    //Deliberately copied/initialised here to stop ppl from changing it in game
    public static int chunkPhasesSpacing = SRPMixinsConfigHandler.chunkphases.chunkSpacing;
    public static int chunkPhasesHalfSpacing = chunkPhasesSpacing >> 1; //Spacing divided by two and truncated (so it's different for odd vs even spacing)
    public static boolean chunkPhasesSpacingIsOdd = (chunkPhasesSpacing & 1) == 1;

    public static final Map<Integer, Integer> minFeralisations = new HashMap<>();
    public static final Map<String, Integer> foodBlacklist = new HashMap<>();
    public static Map<String, List<Integer>> blockBreakBlacklist = new HashMap<>();

    public static void init(){
        setupDimensionMultiplierMap(dimensionHealthMultipliers, SRPMixinsConfigHandler.dimension.dimensionHealthMultipliers);
        setupDimensionMultiplierMap(dimensionDmgMultipliers, SRPMixinsConfigHandler.dimension.dimensionDmgMultipliers);
        setupDimensionMultiplierMap(dimensionArmorMultipliers, SRPMixinsConfigHandler.dimension.dimensionArmorMultipliers);
        setupDimensionMultiplierMap(dimensionKBResMultipliers, SRPMixinsConfigHandler.dimension.dimensionKBResMultipliers);
        setupDimensionMultiplierMap(dimensionDropMultipliers, SRPMixinsConfigHandler.dimension.dimensionDropMultipliers);
        setupDimensionMultiplierMap(dimensionMobCapMultipliers, SRPMixinsConfigHandler.dimension.dimensionMobCapMultipliers);
        setupBiomeBlacklistMap(biomeSpawningBlacklists, SRPMixinsConfigHandler.various.biomeBlacklist);

        playerNeedlerMulti = SRPMixinsConfigHandler.various.playerNeedlerMulti;
        if(playerNeedlerMulti < 0) playerNeedlerMulti = SRPConfigSystems.needlerDamage; //negative = use SRP default value

        whiteListedDeterrents = Arrays.asList(SRPMixinsConfigHandler.deterrents.whiteListedDeterrents);

        for (String s : SRPMixinsConfigHandler.chunkphases.biomeStartPhases) {
            String[] split = s.split(",");
            String biomeId = split[0].trim();
            byte startPhase = Byte.parseByte(split[1].trim());
            biomeStartPhases.put(biomeId, startPhase);
        }

        for(int dimId : SRPMixinsConfigHandler.chunkphases.dimensionBlacklist)
            chunkPhasesDimensionBlacklist.add(dimId);

        for(String s : SRPMixinsConfigHandler.coth.minFeralisations){
            String[] split = s.split(",");
            if(split.length == 2) {
                int paraId = mobNameToParaIdMap.getOrDefault(split[0].trim(), -1);
                if(paraId == -1) { SRPMixins.LOGGER.info("SRPMixins unable to parse Min Feralisation line {}. Mob name doesn't exist",s); continue; }
                int count;
                try { count = Integer.parseInt(split[1].trim()); }
                catch (Exception e){ SRPMixins.LOGGER.info("SRPMixins unable to parse Min Feralisation line {}. Count is not a number",s); continue; }
                minFeralisations.put(paraId, count);
            } else SRPMixins.LOGGER.info("SRPMixins unable to parse Min Feralisation line. Expected pattern: int, int, found {}",s);
        }

        for(String s : SRPMixinsConfigHandler.various.foodBlacklist){
            String[] split = s.split(",");
            if(split.length == 1) foodBlacklist.put(split[0].trim(), -1);
            else if(split.length == 2)
                try { foodBlacklist.put(split[0].trim(), Integer.parseInt(split[1].trim())); }
                catch (Exception e) { SRPMixins.LOGGER.info("SRPMixins unable to parse food blacklist entry {}. Metadata not a number", s); }
            else SRPMixins.LOGGER.info("SRPMixins unable to parse food blacklist entry {}. Expected pattern: modid:itemname, optional metadata", s);
        }

        for(String s : SRPMixinsConfigHandler.various.blockBreakBlacklist) {
            String[] split = s.split(",");
            if(split.length < 2) SRPMixins.LOGGER.info("SRPMixins unable to parse block break blacklist entry {}. Expected pattern: modid:blockname, comma separated parasite mobname/group list", s);
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

        init();
    }

    public static int getLurePhaseMultiplier(byte phase){
        if(!SRPMixinsConfigHandler.lures.variableCarcassValues) return 1;
        if(SRPMixinsConfigHandler.lures.carcassPhaseMultis.length != 11) return 1;
        if(phase < 0 || phase > 10) return 1;
        return SRPMixinsConfigHandler.lures.carcassPhaseMultis[phase];
    }

    public static void setupBiomeBlacklistMap(Map<Integer, ArrayList<String>> map, String[] config) {
        for (String line : config) {
            String[] split = line.split(" *, *");
            if (split.length >= 1) {
                try {
                    int dim = Integer.parseInt(split[0]);
                    if (!map.containsKey(dim))
                        map.put(dim, new ArrayList<>());
                    if(split.length>=2) {
                        String biome = split[1];
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
            String[] split = line.split(" *, *");
            if (split.length >= 2) {
                try {
                    int dim = Integer.parseInt(split[0]);
                    float multi = Float.parseFloat(split[1]);
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
        mobNameToParaIdMap.put("hi_blaze", 6);
        mobNameToParaIdMap.put("hi_skeleton", 6);
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
}
