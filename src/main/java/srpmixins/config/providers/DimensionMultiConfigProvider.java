package srpmixins.config.providers;

import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.HashMap;
import java.util.Map;

public class DimensionMultiConfigProvider {
    public static final Map<Integer,Float> dimensionHealthMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionDmgMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionArmorMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionKBResMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionDropMultipliers = new HashMap<>();
    public static final Map<Integer,Float> dimensionMobCapMultipliers = new HashMap<>();

    public static void init(){
        setupDimensionMultiplierMap(dimensionHealthMultipliers, SRPMixinsConfigHandler.dimension.dimensionHealthMultipliers);
        setupDimensionMultiplierMap(dimensionDmgMultipliers, SRPMixinsConfigHandler.dimension.dimensionDmgMultipliers);
        setupDimensionMultiplierMap(dimensionArmorMultipliers, SRPMixinsConfigHandler.dimension.dimensionArmorMultipliers);
        setupDimensionMultiplierMap(dimensionKBResMultipliers, SRPMixinsConfigHandler.dimension.dimensionKBResMultipliers);
        setupDimensionMultiplierMap(dimensionDropMultipliers, SRPMixinsConfigHandler.dimension.dimensionDropMultipliers);
        setupDimensionMultiplierMap(dimensionMobCapMultipliers, SRPMixinsConfigHandler.dimension.dimensionMobCapMultipliers);
    }

    public static void reset(){
        dimensionHealthMultipliers.clear();
        dimensionDmgMultipliers.clear();
        dimensionArmorMultipliers.clear();
        dimensionKBResMultipliers.clear();
        dimensionDropMultipliers.clear();
        dimensionMobCapMultipliers.clear();

        init();
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
