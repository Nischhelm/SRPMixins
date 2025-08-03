package srpmixins.util.configparse;

import srpmixins.SRPMixins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParaSpawnEntry {
    public String mobid;

    public int minCount;
    public int maxCount;

    public double chance;
    public int points;

    public ParaSpawnEntry(String mobid, int min, int max){
        this.mobid = mobid;
        this.minCount = min;
        this.maxCount = max;
        this.chance = 1.0D;
        this.points = 0;
    }

    public ParaSpawnEntry(String mobid, double chance, int points){
        this.mobid = mobid;
        this.minCount = 1;
        this.maxCount = 1;
        this.chance = chance;
        this.points = points;
    }

    public static List<ParaSpawnEntry> parseMobList(String[] list, boolean isSpawn /*is spawn, not summon */) {
        List<ParaSpawnEntry> cache = new ArrayList<>();
        for (String s : list) {
            String[] split = s.split(";");
            if(split.length <= 1){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse spawn/summon entry, expected pattern is modid:mobname; int or double; optional int, provided was {}", s);
                continue;
            }
            String name = split[0].trim();

            try {
                if (isSpawn) {
                    int min = Integer.parseInt(split[1].trim());
                    int max = split.length >= 3 ? Integer.parseInt(split[2].trim()) : min;
                    cache.add(new ParaSpawnEntry(name, max, min));
                } else {
                    double chance = Double.parseDouble(split[1].trim());
                    int points = split.length >= 3 ? Integer.parseInt(split[2].trim()) : 0;
                    cache.add(new ParaSpawnEntry(name, chance, points));
                }
            } catch (Exception e){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse spawn/summon entry, expected number(s) after the first semicolon, provided was {}", s);
            }
        }
        return cache;
    }

    private static final ThreadLocal<List<ParaSpawnEntry>> currentSpawnList = ThreadLocal.withInitial(() -> null);
    public static void setCurrentSpawnList(List<ParaSpawnEntry> list){
        currentSpawnList.set(list);
    }
    public static void setCurrentSpawnList(ParaSpawnEntry entry){
        currentSpawnList.set(Collections.singletonList(entry));
    }
    public static List<ParaSpawnEntry> getAndClearCurrentSpawnList(){
        List<ParaSpawnEntry> tmp = currentSpawnList.get();
        currentSpawnList.remove();
        return tmp;
    }
}
