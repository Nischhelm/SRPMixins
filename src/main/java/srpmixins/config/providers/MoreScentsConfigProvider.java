package srpmixins.config.providers;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import net.minecraft.util.WeightedRandom;
import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.*;
import java.util.stream.Collectors;

import static srpmixins.config.providers.MorePhasesConfigProvider.getPhaseListsContainingEntryAsString;

public final class MoreScentsConfigProvider {

    //This is only called once if "More Phases" is enabled but the config isn't filled with current SRP values yet
    public static void initMoreScentsConfig() {
        int[] pointsRequired = {0, SRPConfigSystems.scentLevelPointsOne, SRPConfigSystems.scentLevelPointsTwo, SRPConfigSystems.scentLevelPointsThree, SRPConfigSystems.scentLevelPointsFour, SRPConfigSystems.scentLevelPointsFive, SRPConfigSystems.scentLevelPointsSix, SRPConfigSystems.scentLevelPointsSeven, SRPConfigSystems.scentLevelPointsEight};
        int[] waveMin = {SRPConfigSystems.scentWaveMinimumZero, SRPConfigSystems.scentWaveMinimumOne, SRPConfigSystems.scentWaveMinimumTwo, SRPConfigSystems.scentWaveMinimumThree, SRPConfigSystems.scentWaveMinimumFour, SRPConfigSystems.scentWaveMinimumFive, SRPConfigSystems.scentWaveMinimumSix, SRPConfigSystems.scentWaveMinimumSeven, SRPConfigSystems.scentWaveMinimumEight};
        int[] waveMax = {SRPConfigSystems.scentWaveMaximumZero, SRPConfigSystems.scentWaveMaximumOne, SRPConfigSystems.scentWaveMaximumTwo, SRPConfigSystems.scentWaveMaximumThree, SRPConfigSystems.scentWaveMaximumFour, SRPConfigSystems.scentWaveMaximumFive, SRPConfigSystems.scentWaveMaximumSix, SRPConfigSystems.scentWaveMaximumSeven, SRPConfigSystems.scentWaveMaximumEight};
        int[] mobMin  = {SRPConfigSystems.scentWaveMinMobWaveZero, SRPConfigSystems.scentWaveMinMobWaveOne, SRPConfigSystems.scentWaveMinMobWaveTwo, SRPConfigSystems.scentWaveMinMobWaveThree, SRPConfigSystems.scentWaveMinMobWaveFour, SRPConfigSystems.scentWaveMinMobWaveFive, SRPConfigSystems.scentWaveMinMobWaveSix, SRPConfigSystems.scentWaveMinMobWaveSeven, SRPConfigSystems.scentWaveMinMobWaveEight};
        int[] mobMax  = {SRPConfigSystems.scentWaveMaxMobWaveZero, SRPConfigSystems.scentWaveMaxMobWaveOne, SRPConfigSystems.scentWaveMaxMobWaveTwo, SRPConfigSystems.scentWaveMaxMobWaveThree, SRPConfigSystems.scentWaveMaxMobWaveFour, SRPConfigSystems.scentWaveMaxMobWaveFive, SRPConfigSystems.scentWaveMaxMobWaveSix, SRPConfigSystems.scentWaveMaxMobWaveSeven, SRPConfigSystems.scentWaveMaxMobWaveEight};

        List<List<String>> scentSpawnListOriginal = Arrays.asList(Arrays.asList(SRPConfigSystems.scentLevelZero), Arrays.asList(SRPConfigSystems.scentLevelOne), Arrays.asList(SRPConfigSystems.scentLevelTwo), Arrays.asList(SRPConfigSystems.scentLevelThree), Arrays.asList(SRPConfigSystems.scentLevelFour), Arrays.asList(SRPConfigSystems.scentLevelFive), Arrays.asList(SRPConfigSystems.scentLevelSix), Arrays.asList(SRPConfigSystems.scentLevelSeven), Arrays.asList(SRPConfigSystems.scentLevelEight));
        List<String> newSpawnList = new ArrayList<>();
        List<String> spawnEntriesAlreadyProcessed = new ArrayList<>();

        for(List<String> oldListToProcess : scentSpawnListOriginal){
            for(String spawnEntry : oldListToProcess) {
                if(spawnEntriesAlreadyProcessed.contains(spawnEntry)) continue;
                spawnEntriesAlreadyProcessed.add(spawnEntry);
                newSpawnList.add("[" + getPhaseListsContainingEntryAsString(spawnEntry, scentSpawnListOriginal) + "]; " + (spawnEntry.replace("srparasites:", "")));
            }
        }
        String[] scentSpawnList = newSpawnList.toArray(new String[0]);

        Map<String, Integer> deathValues = new LinkedHashMap<String, Integer>(){{
            put("ASSIMILATED", SRPConfig.infectedOneMindDeathV);
            put("FERAL", SRPConfig.feralOneMindDeathV);
            put("HIJACKED", SRPConfig.feralOneMindDeathV);
            put("PRIMITIVE", SRPConfig.primitiveOneMindDeathV);
            put("ADAPTED", SRPConfig.adaptedOneMindDeathV);
            put("PURE", SRPConfig.pureOneMindDeathV);
            put("PREEMINENT", SRPConfig.preeminentOneMindDeathV);
            put("ANCIENT", SRPConfig.ancientOneMindDeathV);
            put("host", SRPConfig.primitiveOneMindDeathV);
            put("hostii", SRPConfig.adaptedOneMindDeathV);
            put("thrall", SRPConfig.primitiveOneMindDeathV);
            put("mangler", SRPConfig.pureOneMindDeathV);
        }};

        String[] deathValueList = deathValues.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).map(e -> e.getKey() + "; " + e.getValue()).toArray(String[]::new);

        String[] mobCounts = new String[9];
        String[] waveCounts = new String[9];
        for(int i = 0; i < 9; i++) {
            mobCounts[i] = mobMin[i] + " - " + mobMax[i];
            waveCounts[i] = waveMin[i] + " - " + waveMax[i];
        }

        SRPMixins.CONFIG.get("general.More Scents", "Points Required", SRPMixinsConfigHandler.morescents.pointsRequired).set(pointsRequired);
        SRPMixins.CONFIG.get("general.More Scents", "Wave Count", SRPMixinsConfigHandler.morescents.waveCount).set(waveCounts);
        SRPMixins.CONFIG.get("general.More Scents", "Mobs per Wave Count", SRPMixinsConfigHandler.morescents.mobPerWaveCount).set(mobCounts);
        SRPMixins.CONFIG.get("general.More Scents", "Wave Spawn List", SRPMixinsConfigHandler.morescents.waveSpawnList).set(scentSpawnList);
        SRPMixins.CONFIG.get("general.More Scents", "Mob Death Values", SRPMixinsConfigHandler.morescents.deathValues).set(deathValueList);

        SRPMixinsConfigHandler.morescents.pointsRequired = pointsRequired;
        SRPMixinsConfigHandler.morescents.waveCount = waveCounts;
        SRPMixinsConfigHandler.morescents.mobPerWaveCount = mobCounts;
        SRPMixinsConfigHandler.morescents.waveSpawnList = scentSpawnList;
        SRPMixinsConfigHandler.morescents.deathValues = deathValueList;

        SRPMixins.CONFIG.save();
    }

    public static void init(){
        initDeathValues();
        initCounts();
        initSpawnLists();
    }

    private static final Map<Integer, List<SpawnEntryItem>> spawnLists = new HashMap<>();
    private static void initSpawnLists(){

        for(String s : SRPMixinsConfigHandler.morescents.waveSpawnList) {
            String[] split = s.split(";");
            if(split.length < 2){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse \"More Scents\" Spawn List entry, too few arguments (expected 2-3): {}", s);
                continue;
            }

            String mobid = split[1].trim();
            if(!mobid.contains(":")) mobid = "srparasites:" + mobid;

            List<Byte> spawnPhases = MorePhasesConfigProvider.parsePhaseList(split[0]);

            int weight;
            try {
                weight = split.length > 2 ? Integer.parseInt(split[2].trim()) : 1;
            } catch (Exception e) {
                SRPMixins.LOGGER.warn("SRPMixins unable to parse \"More Scents\" Spawn List entry, expected pattern [...]; modid:mobName; spawnWeight. Provided was: {}", s);
                return;
            }

            SpawnEntryItem entry = new SpawnEntryItem(mobid, weight);

            for (int i : spawnPhases) {
                spawnLists.computeIfAbsent(i, k -> new ArrayList<>());
                spawnLists.get(i).add(entry);
            }
        }
    }

    public static List<Integer> waveCountMin = new ArrayList<>();
    public static List<Integer> waveCountMax = new ArrayList<>();
    public static List<Integer> mobCountMin = new ArrayList<>();
    public static List<Integer> mobCountMax = new ArrayList<>();
    private static void initCounts() {
        try {
            for (String s : SRPMixinsConfigHandler.morescents.waveCount) {
                String[] split = s.split("-");
                waveCountMin.add(Integer.parseInt(split[0].trim()));
                waveCountMax.add(Integer.parseInt(split[1].trim()));
            }
            for (String s : SRPMixinsConfigHandler.morescents.mobPerWaveCount) {
                String[] split = s.split("-");
                mobCountMin.add(Integer.parseInt(split[0].trim()));
                mobCountMax.add(Integer.parseInt(split[1].trim()));
            }
        } catch (Exception e) {
            SRPMixins.LOGGER.warn("SRPMixins unable to parse More Scents Wave Count or Mobs per Wave Count, expected min - max. This will crash later.");
        }
    }

    public static final Map<String, Integer> deathValues = new HashMap<>();
    public static void initDeathValues(){
        for(String s : SRPMixinsConfigHandler.morescents.deathValues) {
            String[] split = s.split(";");
            if(split.length != 2){
                SRPMixins.LOGGER.warn("SRPMixins unable to parse \"More Scents\" Death Value entry, too few arguments, expected 2: {}", s);
                continue;
            }

            String mobOrGroupId = split[0].trim();
            int value;
            try {
                value = Integer.parseInt(split[1].trim());
            } catch (Exception e) {
                SRPMixins.LOGGER.warn("SRPMixins unable to parse \"More Scents\" Death Value entry, expected pattern mobName/Group; deathValue. Provided was: {}", s);
                continue;
            }

            if(!SRPMobConfigProvider.mobGroupToParaIdMap.containsKey(mobOrGroupId) && !mobOrGroupId.contains(":"))
                mobOrGroupId = "srparasites:" + mobOrGroupId;

            deathValues.put(mobOrGroupId, value);
        }
    }

    public static void reset(){
        spawnLists.clear();
        deathValues.clear();
        init();
    }

    public static String getRandomWaveSpawnListEntry(Random rand, int scentPhase) {
        return WeightedRandom.getRandomItem(rand, spawnLists.getOrDefault(scentPhase, Collections.emptyList())).entry;
    }

    public static class SpawnEntryItem extends WeightedRandom.Item {
        public final String entry;
        public SpawnEntryItem(String entry, int weight) {
            super(weight);
            this.entry = entry;
        }
    }
}
