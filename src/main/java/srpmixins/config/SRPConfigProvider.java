package srpmixins.config;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import srpmixins.SRPMixins;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.util.configparse.ParaOrbEffect;
import srpmixins.util.configparse.Triple;

import java.util.*;
import java.util.stream.Collectors;

public class SRPConfigProvider {
    private static List<Integer> phaseCooldowns = null;
    public static int getPhaseCooldown(byte phase){
        if(SRPMixinsConfigHandler.morephases.enableMorePhases)
            return SRPMixinsConfigHandler.morephases.phaseDelayTicks[phase];

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
            return SRPMixinsConfigHandler.morephases.phaseKills[phase];

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
            return SRPMixinsConfigHandler.morephases.reinforcementSystemChance[phase];

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

    public static final Map<Integer, List<Triple<ItemStack, Integer, Boolean>>> lootPools = new HashMap<>();
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
            Map<Integer, String[]> lootTableConfigs = new HashMap<>();
            lootTableConfigs.put(1, SRPConfigMobs.shycoLoot);
            lootTableConfigs.put(2, SRPConfigMobs.dorpaLoot);
            lootTableConfigs.put(3, SRPConfigMobs.ratholLoot);
            lootTableConfigs.put(4, SRPConfigMobs.emanaLoot);
            lootTableConfigs.put(5, SRPConfigMobs.LodoLoot);
            lootTableConfigs.put(6, SRPConfigMobs.infhumanLoot);
            lootTableConfigs.put(7, SRPConfigMobs.hullLoot);
            lootTableConfigs.put(8, SRPConfigMobs.canraLoot);
            lootTableConfigs.put(9, SRPConfigMobs.alafhaLoot);
            lootTableConfigs.put(10, SRPConfigMobs.noglaLoot);
            lootTableConfigs.put(11, SRPConfigMobs.butholLoot);
            lootTableConfigs.put(12, SRPConfigMobs.mudoLoot);
            lootTableConfigs.put(13, SRPConfigMobs.infcowLoot);
            lootTableConfigs.put(14, SRPConfigMobs.infsheepLoot);
            lootTableConfigs.put(15, SRPConfigMobs.infwolfLoot);
            lootTableConfigs.put(16, SRPConfigMobs.venkrolLoot);
            lootTableConfigs.put(17, SRPConfigMobs.zetmoLoot);
            lootTableConfigs.put(18, SRPConfigMobs.venkrolsiiLoot);
            lootTableConfigs.put(19, SRPConfigMobs.venkrolsiiiLoot);
            lootTableConfigs.put(20, SRPConfigMobs.terlaLoot);
            lootTableConfigs.put(21, SRPConfigMobs.infwolfheadLoot);
            lootTableConfigs.put(22, SRPConfigMobs.infsheepheadLoot);
            lootTableConfigs.put(23, SRPConfigMobs.kolLoot);
            lootTableConfigs.put(24, SRPConfigMobs.oroncoLoot);
            lootTableConfigs.put(25, SRPConfigMobs.angedLoot);
            lootTableConfigs.put(26, SRPConfigMobs.infpigLoot);
            lootTableConfigs.put(27, SRPConfigMobs.infvillagerLoot);
            lootTableConfigs.put(28, SRPConfigMobs.infcowheadLoot);
            lootTableConfigs.put(29, SRPConfigMobs.tonroLoot);
            lootTableConfigs.put(30, SRPConfigMobs.unvoLoot);
            lootTableConfigs.put(31, SRPConfigMobs.infpigheadLoot);
            lootTableConfigs.put(32, SRPConfigMobs.infvillagerheadLoot);
            lootTableConfigs.put(33, SRPConfigMobs.ganroLoot);
            lootTableConfigs.put(34, SRPConfigMobs.pod1Loot);
            lootTableConfigs.put(36, SRPConfigMobs.kolLoot);
            lootTableConfigs.put(37, SRPConfigMobs.wymoLoot);
            lootTableConfigs.put(38, SRPConfigMobs.arachnidaLoot);
            lootTableConfigs.put(39, SRPConfigMobs.inhooSLoot);
            lootTableConfigs.put(40, SRPConfigMobs.infadventurerLoot);
            lootTableConfigs.put(41, SRPConfigMobs.venkrolsivLoot);
            lootTableConfigs.put(43, SRPConfigMobs.inhooMLoot);
            lootTableConfigs.put(44, SRPConfigMobs.infhorseLoot);
            lootTableConfigs.put(45, SRPConfigMobs.infhorseheadLoot);
            lootTableConfigs.put(46, SRPConfigMobs.infhumanheadLoot);
            lootTableConfigs.put(47, SRPConfigMobs.ombooLoot);
            lootTableConfigs.put(48, SRPConfigMobs.hostLoot);
            lootTableConfigs.put(49, SRPConfigMobs.infbearLoot);
            lootTableConfigs.put(50, SRPConfigMobs.esorLoot);
            lootTableConfigs.put(51, SRPConfigMobs.shycoadaptedloot);
            lootTableConfigs.put(52, SRPConfigMobs.hulladaptedloot);
            lootTableConfigs.put(53, SRPConfigMobs.canraadaptedloot);
            lootTableConfigs.put(54, SRPConfigMobs.noglaadaptedloot);
            lootTableConfigs.put(55, SRPConfigMobs.emanaadaptedloot);
            lootTableConfigs.put(56, SRPConfigMobs.zetmoadaptedloot);
            lootTableConfigs.put(57, SRPConfigMobs.shycoLoot);
            lootTableConfigs.put(58, SRPConfigMobs.arachnidaadaptedloot);
            lootTableConfigs.put(59, SRPConfigMobs.infendermanLoot);
            lootTableConfigs.put(60, SRPConfigMobs.flogLoot);
            lootTableConfigs.put(62, SRPConfigMobs.cruxaLoot);
            lootTableConfigs.put(63, SRPConfigMobs.heedLoot);
            lootTableConfigs.put(64, SRPConfigMobs.infdragoneLoot);
            lootTableConfigs.put(65, SRPConfigMobs.jinjoLoot);
            lootTableConfigs.put(66, SRPConfigMobs.lumLoot);
            lootTableConfigs.put(69, SRPConfigMobs.infendermanheadLoot);
            lootTableConfigs.put(70, SRPConfigMobs.infdragoneheadLoot);
            lootTableConfigs.put(71, SRPConfigMobs.infadventurerheadLoot);
            lootTableConfigs.put(72, SRPConfigMobs.nakLoot);
            lootTableConfigs.put(73, SRPConfigMobs.dodsiLoot);
            lootTableConfigs.put(74, SRPConfigMobs.ratholLoot);
            lootTableConfigs.put(75, SRPConfigMobs.herdLoot);
            lootTableConfigs.put(76, SRPConfigMobs.nuuhLoot);
            lootTableConfigs.put(77, SRPConfigMobs.dodsiiLoot);
            lootTableConfigs.put(78, SRPConfigMobs.dodsiiiLoot);
            lootTableConfigs.put(79, SRPConfigMobs.dodsivLoot);
            lootTableConfigs.put(80, SRPConfigMobs.thrallLoot);
            lootTableConfigs.put(81, SRPConfigMobs.lumadaptedloot);
            lootTableConfigs.put(82, SRPConfigMobs.ombooLoot);
            lootTableConfigs.put(84, SRPConfigMobs.orchLoot);
            lootTableConfigs.put(85, SRPConfigMobs.elviaLoot);
            lootTableConfigs.put(86, SRPConfigMobs.lenciaLoot);
            lootTableConfigs.put(87, SRPConfigMobs.pheonLoot);
            lootTableConfigs.put(88, SRPConfigMobs.vestaLoot);
            lootTableConfigs.put(89, SRPConfigMobs.flamLoot);
            lootTableConfigs.put(91, SRPConfigMobs.ataLoot);
            lootTableConfigs.put(92, SRPConfigMobs.ikiLoot);
            lootTableConfigs.put(93, SRPConfigMobs.fercowLoot);
            lootTableConfigs.put(94, SRPConfigMobs.ferendermanLoot);
            lootTableConfigs.put(95, SRPConfigMobs.ferhorseLoot);
            lootTableConfigs.put(96, SRPConfigMobs.ferhumanLoot);
            lootTableConfigs.put(97, SRPConfigMobs.ferpigLoot);
            lootTableConfigs.put(98, SRPConfigMobs.fersheepLoot);
            lootTableConfigs.put(99, SRPConfigMobs.fervillagerLoot);
            lootTableConfigs.put(300, SRPConfigMobs.ferwolfLoot);
            lootTableConfigs.put(301, SRPConfigMobs.higolemLoot);
            lootTableConfigs.put(304, SRPConfigMobs.gotholLoot);
            lootTableConfigs.put(306, SRPConfigMobs.ferbearLoot);
            lootTableConfigs.put(307, SRPConfigMobs.infsquidLoot);
            lootTableConfigs.put(309, SRPConfigMobs.hebluLoot);

            for (Map.Entry<Integer, String[]> entry : lootTableConfigs.entrySet()) {
                List<Triple<ItemStack, Integer, Boolean>> dropList = new ArrayList<>();
                for (String s : entry.getValue()) {
                    String[] split = s.split(";");
                    if(split.length < 4){
                        SRPMixins.LOGGER.warn("SRPMixins: Unable to parse SRP Loot Pool line, too few entries (Expected pattern: modid:itemname;odds;maxQuantity;alwaysDrop): {}", s);
                        continue;
                    }
                    Item item = Item.getByNameOrId(split[0].trim());

                    if (item == null) SRPMixins.LOGGER.warn("SRPMixins: Unable to parse SRP Loot Pool line, item {} doesn't exist: {}", split[0], s);
                    else {
                        try {
                            int chance = Integer.parseInt(split[1].trim());
                            int maxQuantity = Integer.parseInt(split[2].trim());
                            boolean alwaysDrop = Boolean.parseBoolean(split[3].trim());

                            dropList.add(new Triple<>(new ItemStack(item, maxQuantity), chance, alwaysDrop));
                        } catch (Exception e) {
                            SRPMixins.LOGGER.warn("SRPMixins: Unable to parse SRP Loot Pool line (Expected pattern: modid:itemname;odds;maxQuantity;alwaysDrop): {}", s);
                        }
                    }
                }
                lootPools.put(entry.getKey(), dropList);
            }

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
