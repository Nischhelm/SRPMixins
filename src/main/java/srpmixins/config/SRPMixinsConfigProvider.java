package srpmixins.config;

import srpmixins.SRPMixins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SRPMixinsConfigProvider {
    public static HashMap<Integer,Float> dimensionHealthMultipliers = new HashMap<>();
    public static HashMap<Integer,Float> dimensionDmgMultipliers = new HashMap<>();
    public static HashMap<Integer,Float> dimensionArmorMultipliers = new HashMap<>();
    public static HashMap<Integer,Float> dimensionKBResMultipliers = new HashMap<>();
    public static HashMap<Integer,Float> dimensionDropMultipliers = new HashMap<>();
    public static HashMap<Integer,Float> dimensionMobCapMultipliers = new HashMap<>();
    public static HashMap<Integer, ArrayList<String>> biomeSpawningBlacklists = new HashMap<>();
    public static final Map<String, Byte> biomeStartPhases = new HashMap<>();

    public static void init(){
        setupDimensionMultiplierMap(dimensionHealthMultipliers, SRPMixinsConfigHandler.dimension.dimensionHealthMultipliers);
        setupDimensionMultiplierMap(dimensionDmgMultipliers, SRPMixinsConfigHandler.dimension.dimensionDmgMultipliers);
        setupDimensionMultiplierMap(dimensionArmorMultipliers, SRPMixinsConfigHandler.dimension.dimensionArmorMultipliers);
        setupDimensionMultiplierMap(dimensionKBResMultipliers, SRPMixinsConfigHandler.dimension.dimensionKBResMultipliers);
        setupDimensionMultiplierMap(dimensionDropMultipliers, SRPMixinsConfigHandler.dimension.dimensionDropMultipliers);
        setupDimensionMultiplierMap(dimensionMobCapMultipliers, SRPMixinsConfigHandler.dimension.dimensionMobCapMultipliers);
        setupBiomeBlacklistMap(biomeSpawningBlacklists, SRPMixinsConfigHandler.various.biomeBlacklist);

        for (String s : SRPMixinsConfigHandler.phasepoints.biomeStartPhases) {
            String[] split = s.split(",");
            String biomeId = split[0].trim();
            byte startPhase = Byte.parseByte(split[1].trim());
            biomeStartPhases.put(biomeId, startPhase);
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
    }

    public static int getLurePhaseMultiplier(byte phase){
        if(!SRPMixinsConfigHandler.lures.variableCarcassValues) return 1;
        if(SRPMixinsConfigHandler.lures.carcassPhaseMultis.length != 11) return 1;
        if(phase < 0 || phase > 10) return 1;
        return SRPMixinsConfigHandler.lures.carcassPhaseMultis[phase];
    }

    public static void setupBiomeBlacklistMap(HashMap<Integer, ArrayList<String>> map, String[] config) {
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

    public static void setupDimensionMultiplierMap(HashMap<Integer,Float> map, String[] config) {
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
