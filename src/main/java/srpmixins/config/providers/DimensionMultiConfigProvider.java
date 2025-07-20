package srpmixins.config.providers;

import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;

import java.util.HashMap;
import java.util.Map;

public class DimensionMultiConfigProvider {
    // These four maps are stored in SRPMixinsConfigProvider and can't move due to compat relying on their placement in SRPM.C.Provider
    public static Map<Integer, Float> getDmgMap() {return SRPMixinsConfigProvider.dimensionDmgMultipliers;}
    public static Map<Integer, Float> getHPMap() {return SRPMixinsConfigProvider.dimensionHealthMultipliers;}
    public static Map<Integer, Float> getArmorMap() {return SRPMixinsConfigProvider.dimensionArmorMultipliers;}
    public static Map<Integer, Float> getKBResMap() {return SRPMixinsConfigProvider.dimensionKBResMultipliers;}

    public static final Map<Integer, Float> dimensionDropMultipliers = new HashMap<>();
    public static final Map<Integer, Float> dimensionMobCapMultipliers = new HashMap<>();

    public static void init() {
        setupDimensionMultiplierMap(getHPMap(), SRPMixinsConfigHandler.dimension.dimensionHealthMultipliers);
        setupDimensionMultiplierMap(getDmgMap(), SRPMixinsConfigHandler.dimension.dimensionDmgMultipliers);
        setupDimensionMultiplierMap(getArmorMap(), SRPMixinsConfigHandler.dimension.dimensionArmorMultipliers);
        setupDimensionMultiplierMap(getKBResMap(), SRPMixinsConfigHandler.dimension.dimensionKBResMultipliers);
        setupDimensionMultiplierMap(dimensionDropMultipliers, SRPMixinsConfigHandler.dimension.dimensionDropMultipliers);
        setupDimensionMultiplierMap(dimensionMobCapMultipliers, SRPMixinsConfigHandler.dimension.dimensionMobCapMultipliers);
    }

    public static void reset() {
        getDmgMap().clear();
        getHPMap().clear();
        getArmorMap().clear();
        getKBResMap().clear();
        dimensionDropMultipliers.clear();
        dimensionMobCapMultipliers.clear();

        init();
    }

    public static void setupDimensionMultiplierMap(Map<Integer, Float> map, String[] config) {
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
