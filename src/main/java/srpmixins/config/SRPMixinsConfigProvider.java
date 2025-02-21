package srpmixins.config;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import srpmixins.SRPMixins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SRPMixinsConfigProvider {
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

    //Deliberately copied/initialised here to stop ppl from changing it in game
    public static int chunkPhasesSpacing = SRPMixinsConfigHandler.chunkphases.chunkSpacing;
    public static int chunkPhasesHalfSpacing = chunkPhasesSpacing >> 1; //Spacing divided by two and truncated (so it's different for odd vs even spacing)
    public static boolean chunkPhasesSpacingIsOdd = (chunkPhasesSpacing & 1) == 1;

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

        for (String s : SRPMixinsConfigHandler.chunkphases.biomeStartPhases) {
            String[] split = s.split(",");
            String biomeId = split[0].trim();
            byte startPhase = Byte.parseByte(split[1].trim());
            biomeStartPhases.put(biomeId, startPhase);
        }

        for(int dimId : SRPMixinsConfigHandler.chunkphases.dimensionBlacklist)
            chunkPhasesDimensionBlacklist.add(dimId);
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

}
