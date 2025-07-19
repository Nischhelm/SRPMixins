package srpmixins.config.providers;

import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.*;

public class ChunkPhaseConfigProvider {
    public static final Map<String, Byte> biomeStartPhases = new HashMap<>();
    public static final List<Integer> chunkPhasesDimensionBlacklist = new ArrayList<>();

    public static void reset(){
        biomeStartPhases.clear();
        chunkPhasesDimensionBlacklist.clear();

        init();
    }

    public static void init(){

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
    }
}
