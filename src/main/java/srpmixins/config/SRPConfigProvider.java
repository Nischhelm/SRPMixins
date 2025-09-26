package srpmixins.config;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import srpmixins.SRPMixins;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.util.configparse.ParaOrbEffect;

import java.util.*;
import java.util.stream.Collectors;

public class SRPConfigProvider {
    private static List<Integer> phaseCooldowns = null;
    public static int getPhaseCooldown(byte phase){
        if(SRPMixinsConfigHandler.morephases.enableMorePhases)
            return phase >= 0 && phase <= getMaxPhase() ? SRPMixinsConfigHandler.morephases.phaseDelayTicks[phase] : 0;

        if(phaseCooldowns == null){
            phaseCooldowns = Arrays.asList(
                    0,
                    SRPConfigSystems.phaseDelayTicksOne,
                    SRPConfigSystems.phaseDelayTicksTwo,
                    SRPConfigSystems.phaseDelayTicksThree,
                    SRPConfigSystems.phaseDelayTicksFour,
                    SRPConfigSystems.phaseDelayTicksFive,
                    SRPConfigSystems.phaseDelayTicksSix,
                    SRPConfigSystems.phaseDelayTicksSeven,
                    SRPConfigSystems.phaseDelayTicksEight,
                    SRPConfigSystems.phaseDelayTicksNine,
                    SRPConfigSystems.phaseDelayTicksTen
            );
        }
        return phaseCooldowns.get(MathHelper.clamp(phase, 0, 10));
    }

    private static List<Integer> phasePointThresholds = null;
    public static int getPhaseMinPoints(byte phase){
        if(SRPMixinsConfigHandler.morephases.enableMorePhases)
            return SRPMixinsConfigHandler.morephases.phaseKills[MathHelper.clamp(phase, 0, getMaxPhase())];

        if(phasePointThresholds == null) {
            phasePointThresholds = Arrays.asList(
                    0,
                    SRPConfigSystems.phaseKillsOne,
                    SRPConfigSystems.phaseKillsTwo,
                    SRPConfigSystems.phaseKillsThree,
                    SRPConfigSystems.phaseKillsFour,
                    SRPConfigSystems.phaseKillsFive,
                    SRPConfigSystems.phaseKillsSix,
                    SRPConfigSystems.phaseKillsSeven,
                    SRPConfigSystems.phaseKillsEight,
                    SRPConfigSystems.phaseKillsNine,
                    SRPConfigSystems.phaseKillsTen
            );
        }
        return phasePointThresholds.get(MathHelper.clamp(phase, 0, 10));
    }

    public static byte getMaxPhase(){
        if(SRPMixinsConfigHandler.morephases.enableMorePhases) return SRPMixinsConfigHandler.morephases.maxEvolutionPhase;
        return 10;
    }

    private static List<Double> reinforcementChancePerPhase;
    public static double getReinforcementChance(byte phase){
        if(SRPMixinsConfigHandler.morephases.enableMorePhases)
            return phase >= 0 && phase <= getMaxPhase() ? SRPMixinsConfigHandler.morephases.reinforcementSystemChance[phase] : 0;

        if(reinforcementChancePerPhase == null)
            reinforcementChancePerPhase = Arrays.asList(
                    0.0,
                    SRPConfigSystems.reinforcementSystemChanceOne,
                    SRPConfigSystems.reinforcementSystemChanceTwo,
                    SRPConfigSystems.reinforcementSystemChanceThree,
                    SRPConfigSystems.reinforcementSystemChanceFour,
                    SRPConfigSystems.reinforcementSystemChanceFive,
                    SRPConfigSystems.reinforcementSystemChanceSix,
                    SRPConfigSystems.reinforcementSystemChanceSeven,
                    SRPConfigSystems.reinforcementSystemChanceEight,
                    SRPConfigSystems.reinforcementSystemChanceNine,
                    SRPConfigSystems.reinforcementSystemChanceTen
            );

        return reinforcementChancePerPhase.get(MathHelper.clamp(phase, 0, 10));
    }

    public static List<Integer> dimensionCanGainPointsBlacklist;
    public static List<Integer> dimensionCantLosePointsBlacklist;

    //dimensionId, [phase, points]
    public static final Map<Integer, List<Integer>> evolutionStartPerDimension = new HashMap<>();
    public static final List<Integer> lockedParasites = new ArrayList<>();

    public static final Map<Integer, List<ParaOrbEffect>> orbEffects = new HashMap<>();

    public static void init(){
        dimensionCanGainPointsBlacklist = Arrays.stream(SRPConfigSystems.evolutionDimGain).boxed().collect(Collectors.toList());
        dimensionCantLosePointsBlacklist = Arrays.stream(SRPConfigSystems.evolutionDimLoss).boxed().collect(Collectors.toList());

        for(String s : SRPConfigSystems.evolutionParasiteLock) {
            if (s != null) {
                String[] split = s.split(";");
                if(split.length >= 3) {
                    String mobIdOrName = split[2].trim();
                    try {
                        int id = Integer.parseInt(mobIdOrName);
                        lockedParasites.add(id);
                    } catch (Exception e){
                        if(SRPMobConfigProvider.mobNameToParaIdMap.containsKey(mobIdOrName))
                            lockedParasites.add(SRPMobConfigProvider.mobNameToParaIdMap.get(mobIdOrName));
                        else if(SRPMobConfigProvider.mobNameToParaIdMap.containsKey(mobIdOrName.replace("srparasites:","")))
                            lockedParasites.add(SRPMobConfigProvider.mobNameToParaIdMap.get(mobIdOrName.replace("srparasites:","")));
                        else
                            SRPMixins.LOGGER.warn("SRPMixins unable to parse \"SRP Evolution Parasite Lock\" entry, expected parasite id or name in last entry, provided was {}", s);
                    }
                } else
                    SRPMixins.LOGGER.warn("SRPMixins unable to parse SRP \"Evolution Parasite Lock\" line, line has not enough entries to be valid (expected pattern: dim; phase; parasite id or name), provided was: {}", s);
            }
        }

        for (String s : SRPConfigSystems.evolutionDimStart) {
            String[] split = s.split(";");
            if(split.length >= 3) {
                try {
                    int dim = Integer.parseInt(split[0].trim());
                    int phase = Integer.parseInt(split[1].trim());
                    int points = Integer.parseInt(split[2].trim());
                    evolutionStartPerDimension.put(dim, Arrays.asList(phase, points));
                } catch (Exception e){
                    SRPMixins.LOGGER.warn("SRPMixins unable to parse SRP \"Evolution Phases Dimension Starting Phase\" line, expected only integers, provided was: {}", s);
                }
            } else
                SRPMixins.LOGGER.warn("SRPMixins unable to parse SRP \"Evolution Phases Dimension Starting Phase\" line, line has not enough entries to be valid (expected pattern: dim; phase; points), provided was: {}", s);
        }
    }

    public static void postInit(){
        if(SRPMixinsConfigHandler.various.fixConfigListParse) {
            Map<Integer, String[]> orbConfigData = new HashMap<>();

            orbConfigData.put(1, SRPConfigMobs.shycoOrbEffects);
            orbConfigData.put(7, SRPConfigMobs.hullOrbEffects);
            orbConfigData.put(8, SRPConfigMobs.canraOrbEffects);
            orbConfigData.put(10, SRPConfigMobs.noglaOrbEffects);
            orbConfigData.put(17, SRPConfigMobs.zetmoOrbEffects);
            orbConfigData.put(25, SRPConfigMobs.angedOrbEffects);
            orbConfigData.put(33, SRPConfigMobs.ganroOrbEffects);
            orbConfigData.put(37, SRPConfigMobs.wymoOrbEffects);
            orbConfigData.put(38, SRPConfigMobs.arachnidaOrbEffects);
            orbConfigData.put(50, SRPConfigMobs.esorOrbEffects);
            orbConfigData.put(51, SRPConfigMobs.shycoadaptedOrbEffects);
            orbConfigData.put(52, SRPConfigMobs.hulladaptedOrbEffects);
            orbConfigData.put(53, SRPConfigMobs.canraadaptedOrbEffects);
            orbConfigData.put(54, SRPConfigMobs.noglaadaptedOrbEffects);
            orbConfigData.put(56, SRPConfigMobs.zetmoadaptedOrbEffects);
            orbConfigData.put(58, SRPConfigMobs.arachnidaadaptedOrbEffects);
            orbConfigData.put(65, SRPConfigMobs.jinjoOrbEffects);
            orbConfigData.put(84, SRPConfigMobs.orchOrbEffects);
            orbConfigData.put(85, SRPConfigMobs.elviaOrbEffects);
            orbConfigData.put(86, SRPConfigMobs.lenciaOrbEffects);
            orbConfigData.put(87, SRPConfigMobs.pheonOrbEffects);
            orbConfigData.put(88, SRPConfigMobs.vestaOrbEffects);
            orbConfigData.put(92, SRPConfigMobs.ikiOrbEffects);

            for (Map.Entry<Integer, String[]> entry : orbConfigData.entrySet()) {
                List<ParaOrbEffect> orbEffectList = new ArrayList<>();
                for (String s : entry.getValue()) {
                    String[] split = s.split(";");
                    if(split.length < 6){
                        SRPMixins.LOGGER.warn("SRPMixins: Unable to parse SRP Orb Effects line, too few entries (Expected pattern: int;int;int;modid:potionname;int;int): {}", s);
                        continue;
                    }
                    Potion potion = Potion.getPotionFromResourceLocation(split[3].trim());

                    if (potion == null) SRPMixins.LOGGER.warn("SRPMixins: Unable to parse SRP Orb Effects line, potion {} doesn't exist: {}", split[3].trim(), s);
                    else {
                        try {
                            int applicationMode = Integer.parseInt(split[0].trim());
                            int duration = Integer.parseInt(split[1].trim()) * 20;
                            int amplifier = Integer.parseInt(split[2].trim());
                            int mobDivisorAmplifier = Integer.parseInt(split[4].trim());
                            int mobDivisorDuration = Integer.parseInt(split[5].trim());

                            orbEffectList.add(new ParaOrbEffect(applicationMode, new PotionEffect(potion, duration, amplifier), mobDivisorAmplifier, mobDivisorDuration));
                        } catch (Exception e) {
                            SRPMixins.LOGGER.warn("SRPMixins: Unable to parse SRP Orb Effects line (Expected pattern: int;int;int;modid:potionname;int;int): {}", s);
                        }
                    }
                }
                orbEffects.put(entry.getKey(), orbEffectList);
            }
        }
    }
}
