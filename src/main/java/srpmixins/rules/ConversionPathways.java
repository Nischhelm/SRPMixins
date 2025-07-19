package srpmixins.rules;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConversionPathways {

    private static final Map<String, Map<String, Integer>> conversionPathwayLocks = new HashMap<>();

    public static int getConversionPhaseLock(String mobIn, String mobOut) {
        if (!conversionPathwayLocks.containsKey(mobIn) || !conversionPathwayLocks.get(mobIn).containsKey(mobOut)){
            if(SRPMixinsConfigHandler.spawns.autoFillConversionRules) setConversionPathwayLock(mobIn, mobOut, -2);
            return -2;
        }
        return conversionPathwayLocks.get(mobIn).get(mobOut);
    }

    private static void setConversionPathwayLock(String mobIn, String mobOut, int defaultValue) {
        conversionPathwayLocks.computeIfAbsent(mobIn, mob -> new HashMap<>()).put(mobOut, defaultValue);
    }

    public static void reset(){
        conversionPathwayLocks.clear();
        init();
    }

    public static void init(){
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

    @SubscribeEvent
    public static void writeConversionLockConfig(PlayerEvent.PlayerLoggedOutEvent event) {
        if(event.player.world.isRemote) return;

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
