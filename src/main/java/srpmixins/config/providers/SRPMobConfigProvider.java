package srpmixins.config.providers;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.rules.VariantRule;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static srpmixins.rules.VariantRule.EnumVariant.*;

public class SRPMobConfigProvider {
    public static final Map<String, Integer> mobNameToParaIdMap = new HashMap<>();
    public static final Map<String, Byte> mobNameToParaTypeMap = new HashMap<>();
    public static final Map<Integer, String> paraIdToMobName;
    public static final Map<String, List<Integer>> mobGroupToParaIdMap = new HashMap<>();
    public static final Map<String, List<VariantRule.EnumVariant>> mobNameToVariantsMap = new HashMap<>();

    private static final Map<String, SRPMobConfig> srpMobConfig = new HashMap<>();

    public static void reset(){
        srpMobConfig.clear();
    }

    public static boolean init() {
        String unused = "---";
        for (String s : SRPMixinsConfigHandler.mobConfig.mobConfig) {
            List<String> split = Arrays.stream(s.split("\t")).map(String::trim).collect(Collectors.toList());
            if (split.size() < 7)
                SRPMixins.LOGGER.warn("SRPMixins unable to parse SRPMixins Mob Config entry, too few entries, provided was {}", s);
            else {
                try {
                    Boolean enabled = split.get(0).equals(unused) ? null : Boolean.parseBoolean(split.get(0));
                    Float healthMulti = split.get(1).equals(unused) ? null : Float.parseFloat(split.get(1));
                    Float dmgMulti = split.get(2).equals(unused) ? null : Float.parseFloat(split.get(2));
                    Float armorMulti = split.get(3).equals(unused) ? null : Float.parseFloat(split.get(3));
                    Float kbresMulti = split.get(4).equals(unused) ? null : Float.parseFloat(split.get(4));
                    Integer spawnRate = split.get(5).equals(unused) ? null : Integer.parseInt(split.get(5));
                    String mobName = split.get(6);
                    srpMobConfig.put(mobName, new SRPMobConfig(enabled, dmgMulti, armorMulti, healthMulti, kbresMulti, spawnRate));
                } catch (Exception e) {
                    SRPMixins.LOGGER.warn("SRPMixins unable to parse SRPMixins Mob Config entry, expected numbers, provided was {}", s);
                }
            }
        }
        return !srpMobConfig.isEmpty();
    }

    //This only runs once on first startup with config enabled
    public static void initMobConfigs() {
        srpMobConfig.put("anc_dreadnaut", new SRPMobConfig(SRPConfigMobs.oroncoEnabled, SRPConfigMobs.oroncoDamageMultiplier, SRPConfigMobs.oroncoArmorMultiplier, SRPConfigMobs.oroncoHealthMultiplier, SRPConfigMobs.oroncoKDResistanceMultiplier, SRPConfigMobs.oroncoSpawnRate));
        srpMobConfig.put("anc_overlord", new SRPMobConfig(SRPConfigMobs.terlaEnabled, SRPConfigMobs.terlaDamageMultiplier, SRPConfigMobs.terlaArmorMultiplier, SRPConfigMobs.terlaHealthMultiplier, SRPConfigMobs.terlaKDResistanceMultiplier, SRPConfigMobs.terlaSpawnRate));
        srpMobConfig.put("anc_pod", new SRPMobConfig(null, SRPConfigMobs.pod1DamageMultiplier, SRPConfigMobs.pod1ArmorMultiplier, SRPConfigMobs.pod1HealthMultiplier, null, null));
        srpMobConfig.put("pri_arachnida", new SRPMobConfig(SRPConfigMobs.arachnidaEnabled, SRPConfigMobs.arachnidaDamageMultiplier, SRPConfigMobs.arachnidaArmorMultiplier, SRPConfigMobs.arachnidaHealthMultiplier, SRPConfigMobs.arachnidaKDResistanceMultiplier, SRPConfigMobs.arachnidaSpawnRate));
        srpMobConfig.put("bogle", new SRPMobConfig(SRPConfigMobs.lenciaEnabled, SRPConfigMobs.lenciaDamageMultiplier, SRPConfigMobs.lenciaArmorMultiplier, SRPConfigMobs.lenciaHealthMultiplier, SRPConfigMobs.lenciaKDResistanceMultiplier, SRPConfigMobs.lenciaSpawnRate));
        srpMobConfig.put("bomber_heavy", new SRPMobConfig(SRPConfigMobs.jinjoEnabled, SRPConfigMobs.jinjoDamageMultiplier, SRPConfigMobs.jinjoArmorMultiplier, SRPConfigMobs.jinjoHealthMultiplier, SRPConfigMobs.jinjoKDResistanceMultiplier, SRPConfigMobs.jinjoSpawnRate));
        srpMobConfig.put("bomber_light", new SRPMobConfig(SRPConfigMobs.ombooEnabled, SRPConfigMobs.ombooDamageMultiplier, SRPConfigMobs.ombooArmorMultiplier, SRPConfigMobs.ombooHealthMultiplier, SRPConfigMobs.ombooKDResistanceMultiplier, SRPConfigMobs.ombooSpawnRate));
        srpMobConfig.put("buglin", new SRPMobConfig(SRPConfigMobs.lodoEnabled, SRPConfigMobs.lodoDamageMultiplier, SRPConfigMobs.lodoArmorMultiplier, SRPConfigMobs.lodoHealthMultiplier, SRPConfigMobs.lodoKDResistanceMultiplier, SRPConfigMobs.lodoSpawnRate));
        srpMobConfig.put("carrier_colony", new SRPMobConfig(SRPConfigMobs.vestaEnabled, SRPConfigMobs.vestaDamageMultiplier, SRPConfigMobs.vestaArmorMultiplier, SRPConfigMobs.vestaHealthMultiplier, SRPConfigMobs.vestaKDResistanceMultiplier, SRPConfigMobs.vestaSpawnRate));
        srpMobConfig.put("carrier_flying", new SRPMobConfig(SRPConfigMobs.butholEnabled, SRPConfigMobs.butholDamageMultiplier, SRPConfigMobs.butholArmorMultiplier, SRPConfigMobs.butholHealthMultiplier, SRPConfigMobs.butholKDResistanceMultiplier, SRPConfigMobs.butholSpawnRate));
        srpMobConfig.put("carrier_heavy", new SRPMobConfig(SRPConfigMobs.ratholEnabled, SRPConfigMobs.ratholDamageMultiplier, SRPConfigMobs.ratholArmorMultiplier, SRPConfigMobs.ratholHealthMultiplier, SRPConfigMobs.ratholKDResistanceMultiplier, SRPConfigMobs.ratholSpawnRate));
        srpMobConfig.put("carrier_light", new SRPMobConfig(SRPConfigMobs.gotholEnabled, SRPConfigMobs.gotholDamageMultiplier, SRPConfigMobs.gotholArmorMultiplier, SRPConfigMobs.gotholHealthMultiplier, SRPConfigMobs.gotholKDResistanceMultiplier, SRPConfigMobs.gotholSpawnRate));
        srpMobConfig.put("crux", new SRPMobConfig(SRPConfigMobs.cruxaEnabled, SRPConfigMobs.cruxaDamageMultiplier, SRPConfigMobs.cruxaArmorMultiplier, SRPConfigMobs.cruxaHealthMultiplier, SRPConfigMobs.cruxaKDResistanceMultiplier, SRPConfigMobs.cruxaSpawnRate));
        srpMobConfig.put("draconite", new SRPMobConfig(SRPConfigMobs.hebluEnabled, SRPConfigMobs.hebluDamageMultiplier, SRPConfigMobs.hebluArmorMultiplier, SRPConfigMobs.hebluHealthMultiplier, SRPConfigMobs.hebluKDResistanceMultiplier, SRPConfigMobs.hebluSpawnRate));
        srpMobConfig.put("fer_bear", new SRPMobConfig(SRPConfigMobs.ferbearEnabled, SRPConfigMobs.ferbearDamageMultiplier, SRPConfigMobs.ferbearArmorMultiplier, SRPConfigMobs.ferbearHealthMultiplier, SRPConfigMobs.ferbearKDResistanceMultiplier, SRPConfigMobs.ferbearSpawnRate));
        srpMobConfig.put("fer_cow", new SRPMobConfig(SRPConfigMobs.fercowEnabled, SRPConfigMobs.fercowDamageMultiplier, SRPConfigMobs.fercowArmorMultiplier, SRPConfigMobs.fercowHealthMultiplier, SRPConfigMobs.fercowKDResistanceMultiplier, SRPConfigMobs.fercowSpawnRate));
        srpMobConfig.put("fer_enderman", new SRPMobConfig(SRPConfigMobs.ferendermanEnabled, SRPConfigMobs.ferendermanDamageMultiplier, SRPConfigMobs.ferendermanArmorMultiplier, SRPConfigMobs.ferendermanHealthMultiplier, SRPConfigMobs.ferendermanKDResistanceMultiplier, SRPConfigMobs.ferendermanSpawnRate));
        srpMobConfig.put("fer_horse", new SRPMobConfig(SRPConfigMobs.ferhorseEnabled, SRPConfigMobs.ferhorseDamageMultiplier, SRPConfigMobs.ferhorseArmorMultiplier, SRPConfigMobs.ferhorseHealthMultiplier, SRPConfigMobs.ferhorseKDResistanceMultiplier, SRPConfigMobs.ferhorseSpawnRate));
        srpMobConfig.put("fer_human", new SRPMobConfig(SRPConfigMobs.ferhumanEnabled, SRPConfigMobs.ferhumanDamageMultiplier, SRPConfigMobs.ferhumanArmorMultiplier, SRPConfigMobs.ferhumanHealthMultiplier, SRPConfigMobs.ferhumanKDResistanceMultiplier, SRPConfigMobs.ferhumanSpawnRate));
        srpMobConfig.put("fer_pig", new SRPMobConfig(SRPConfigMobs.ferpigEnabled, SRPConfigMobs.ferpigDamageMultiplier, SRPConfigMobs.ferpigArmorMultiplier, SRPConfigMobs.ferpigHealthMultiplier, SRPConfigMobs.ferpigKDResistanceMultiplier, SRPConfigMobs.ferpigSpawnRate));
        srpMobConfig.put("fer_sheep", new SRPMobConfig(SRPConfigMobs.fersheepEnabled, SRPConfigMobs.fersheepDamageMultiplier, SRPConfigMobs.fersheepArmorMultiplier, SRPConfigMobs.fersheepHealthMultiplier, SRPConfigMobs.fersheepKDResistanceMultiplier, SRPConfigMobs.fersheepSpawnRate));
        srpMobConfig.put("fer_villager", new SRPMobConfig(SRPConfigMobs.fervillagerEnabled, SRPConfigMobs.fervillagerDamageMultiplier, SRPConfigMobs.fervillagerArmorMultiplier, SRPConfigMobs.fervillagerHealthMultiplier, SRPConfigMobs.fervillagerKDResistanceMultiplier, SRPConfigMobs.fervillagerSpawnRate));
        srpMobConfig.put("fer_wolf", new SRPMobConfig(SRPConfigMobs.ferwolfEnabled, SRPConfigMobs.ferwolfDamageMultiplier, SRPConfigMobs.ferwolfArmorMultiplier, SRPConfigMobs.ferwolfHealthMultiplier, SRPConfigMobs.ferwolfKDResistanceMultiplier, SRPConfigMobs.ferwolfSpawnRate));
        srpMobConfig.put("gnat", new SRPMobConfig(SRPConfigMobs.ataEnabled, SRPConfigMobs.ataDamageMultiplier, SRPConfigMobs.ataArmorMultiplier, SRPConfigMobs.ataHealthMultiplier, SRPConfigMobs.ataKDResistanceMultiplier, SRPConfigMobs.ataSpawnRate));
        srpMobConfig.put("grunt", new SRPMobConfig(SRPConfigMobs.flogEnabled, SRPConfigMobs.flogDamageMultiplier, SRPConfigMobs.flogArmorMultiplier, SRPConfigMobs.flogHealthMultiplier, SRPConfigMobs.flogKDResistanceMultiplier, SRPConfigMobs.flogSpawnRate));
        srpMobConfig.put("haunter", new SRPMobConfig(SRPConfigMobs.pheonEnabled, SRPConfigMobs.pheonDamageMultiplier, SRPConfigMobs.pheonArmorMultiplier, SRPConfigMobs.pheonHealthMultiplier, SRPConfigMobs.pheonKDResistanceMultiplier, SRPConfigMobs.pheonSpawnRate));
        srpMobConfig.put("heed", new SRPMobConfig(SRPConfigMobs.heedEnabled, SRPConfigMobs.heedDamageMultiplier, SRPConfigMobs.heedArmorMultiplier, SRPConfigMobs.heedHealthMultiplier, SRPConfigMobs.heedKDResistanceMultiplier, SRPConfigMobs.heedSpawnRate));
        srpMobConfig.put("hostii", new SRPMobConfig(SRPConfigMobs.herdEnabled, SRPConfigMobs.herdDamageMultiplier, SRPConfigMobs.herdArmorMultiplier, SRPConfigMobs.herdHealthMultiplier, SRPConfigMobs.herdKDResistanceMultiplier, SRPConfigMobs.herdSpawnRate));
        srpMobConfig.put("hi_blaze", new SRPMobConfig(SRPConfigMobs.hiblazeEnabled, SRPConfigMobs.hiblazeDamageMultiplier, SRPConfigMobs.hiblazeArmorMultiplier, SRPConfigMobs.hiblazeHealthMultiplier, SRPConfigMobs.hiblazeKDResistanceMultiplier, SRPConfigMobs.hiblazeSpawnRate));
        srpMobConfig.put("hi_golem", new SRPMobConfig(SRPConfigMobs.higolemEnabled, SRPConfigMobs.higolemDamageMultiplier, SRPConfigMobs.higolemArmorMultiplier, SRPConfigMobs.higolemHealthMultiplier, SRPConfigMobs.higolemKDResistanceMultiplier, SRPConfigMobs.higolemSpawnRate));
        srpMobConfig.put("hi_skeleton", new SRPMobConfig(SRPConfigMobs.hiskeletonEnabled, SRPConfigMobs.hiskeletonDamageMultiplier, SRPConfigMobs.hiskeletonArmorMultiplier, SRPConfigMobs.hiskeletonHealthMultiplier, SRPConfigMobs.hiskeletonKDResistanceMultiplier, SRPConfigMobs.hiskeletonSpawnRate));
        srpMobConfig.put("host", new SRPMobConfig(SRPConfigMobs.hostEnabled, SRPConfigMobs.hostDamageMultiplier, SRPConfigMobs.hostArmorMultiplier, SRPConfigMobs.hostHealthMultiplier, SRPConfigMobs.hostKDResistanceMultiplier, SRPConfigMobs.hostSpawnRate));
        srpMobConfig.put("incompleteform_medium", new SRPMobConfig(SRPConfigMobs.inhooMEnabled, SRPConfigMobs.inhooMDamageMultiplier, SRPConfigMobs.inhooMArmorMultiplier, SRPConfigMobs.inhooMHealthMultiplier, SRPConfigMobs.inhooMKDResistanceMultiplier, SRPConfigMobs.inhooMSpawnRate));
        srpMobConfig.put("incompleteform_small", new SRPMobConfig(SRPConfigMobs.inhooSEnabled, SRPConfigMobs.inhooSDamageMultiplier, SRPConfigMobs.inhooSArmorMultiplier, SRPConfigMobs.inhooSHealthMultiplier, SRPConfigMobs.inhooSKDResistanceMultiplier, SRPConfigMobs.inhooSSpawnRate));
        srpMobConfig.put("kyphosis", new SRPMobConfig(SRPConfigMobs.tonroEnabled, SRPConfigMobs.tonroDamageMultiplier, SRPConfigMobs.tonroArmorMultiplier, SRPConfigMobs.tonroHealthMultiplier, null, null));
        srpMobConfig.put("mangler", new SRPMobConfig(SRPConfigMobs.nuuhEnabled, SRPConfigMobs.nuuhDamageMultiplier, SRPConfigMobs.nuuhArmorMultiplier, SRPConfigMobs.nuuhHealthMultiplier, SRPConfigMobs.nuuhKDResistanceMultiplier, SRPConfigMobs.nuuhSpawnRate));
        srpMobConfig.put("marauder", new SRPMobConfig(SRPConfigMobs.esorEnabled, SRPConfigMobs.esorDamageMultiplier, SRPConfigMobs.esorArmorMultiplier, SRPConfigMobs.esorHealthMultiplier, SRPConfigMobs.esorKDResistanceMultiplier, SRPConfigMobs.esorSpawnRate));
        srpMobConfig.put("monarch", new SRPMobConfig(SRPConfigMobs.orchEnabled, SRPConfigMobs.orchDamageMultiplier, SRPConfigMobs.orchArmorMultiplier, SRPConfigMobs.orchHealthMultiplier, SRPConfigMobs.orchKDResistanceMultiplier, SRPConfigMobs.orchSpawnRate));
        srpMobConfig.put("overseer", new SRPMobConfig(SRPConfigMobs.alafhaEnabled, SRPConfigMobs.alafhaDamageMultiplier, SRPConfigMobs.alafhaArmorMultiplier, SRPConfigMobs.alafhaHealthMultiplier, SRPConfigMobs.alafhaKDResistanceMultiplier, SRPConfigMobs.alafhaSpawnRate));
        srpMobConfig.put("pri_bolster", new SRPMobConfig(SRPConfigMobs.zetmoEnabled, SRPConfigMobs.zetmoDamageMultiplier, SRPConfigMobs.zetmoArmorMultiplier, SRPConfigMobs.zetmoHealthMultiplier, SRPConfigMobs.zetmoKDResistanceMultiplier, SRPConfigMobs.zetmoSpawnRate));
        srpMobConfig.put("pri_devourer", new SRPMobConfig(SRPConfigMobs.lumEnabled, SRPConfigMobs.lumDamageMultiplier, SRPConfigMobs.lumArmorMultiplier, SRPConfigMobs.lumHealthMultiplier, SRPConfigMobs.lumKDResistanceMultiplier, SRPConfigMobs.lumSpawnRate));
        srpMobConfig.put("pri_longarms", new SRPMobConfig(SRPConfigMobs.shycoEnabled, SRPConfigMobs.shycoDamageMultiplier, SRPConfigMobs.shycoArmorMultiplier, SRPConfigMobs.shycoHealthMultiplier, SRPConfigMobs.shycoKDResistanceMultiplier, SRPConfigMobs.shycoSpawnRate));
        srpMobConfig.put("pri_manducater", new SRPMobConfig(SRPConfigMobs.hullEnabled, SRPConfigMobs.hullDamageMultiplier, SRPConfigMobs.hullArmorMultiplier, SRPConfigMobs.hullHealthMultiplier, SRPConfigMobs.hullKDResistanceMultiplier, SRPConfigMobs.hullSpawnRate));
        srpMobConfig.put("pri_reeker", new SRPMobConfig(SRPConfigMobs.noglaEnabled, SRPConfigMobs.noglaDamageMultiplier, SRPConfigMobs.noglaArmorMultiplier, SRPConfigMobs.noglaHealthMultiplier, SRPConfigMobs.noglaKDResistanceMultiplier, SRPConfigMobs.noglaSpawnRate));
        srpMobConfig.put("pri_summoner", new SRPMobConfig(SRPConfigMobs.canraEnabled, SRPConfigMobs.canraDamageMultiplier, SRPConfigMobs.canraArmorMultiplier, SRPConfigMobs.canraHealthMultiplier, SRPConfigMobs.canraKDResistanceMultiplier, SRPConfigMobs.canraSpawnRate));
        srpMobConfig.put("pri_tozoon", new SRPMobConfig(SRPConfigMobs.wymoEnabled, SRPConfigMobs.wymoDamageMultiplier, SRPConfigMobs.wymoArmorMultiplier, SRPConfigMobs.wymoHealthMultiplier, SRPConfigMobs.wymoKDResistanceMultiplier, SRPConfigMobs.wymoSpawnRate));
        srpMobConfig.put("pri_vermin", new SRPMobConfig(SRPConfigMobs.ikiEnabled, SRPConfigMobs.ikiDamageMultiplier, SRPConfigMobs.ikiArmorMultiplier, SRPConfigMobs.ikiHealthMultiplier, SRPConfigMobs.ikiKDResistanceMultiplier, SRPConfigMobs.ikiSpawnRate));
        srpMobConfig.put("pri_yelloweye", new SRPMobConfig(SRPConfigMobs.emanaEnabled, SRPConfigMobs.emanaDamageMultiplier, SRPConfigMobs.emanaArmorMultiplier, SRPConfigMobs.emanaHealthMultiplier, SRPConfigMobs.emanaKDResistanceMultiplier, SRPConfigMobs.emanaSpawnRate));
        srpMobConfig.put("rupter", new SRPMobConfig(SRPConfigMobs.mudoEnabled, SRPConfigMobs.mudoDamageMultiplier, SRPConfigMobs.mudoArmorMultiplier, SRPConfigMobs.mudoHealthMultiplier, SRPConfigMobs.mudoKDResistanceMultiplier, SRPConfigMobs.mudoSpawnRate));
        srpMobConfig.put("seizer", new SRPMobConfig(SRPConfigMobs.nakEnabled, SRPConfigMobs.nakDamageMultiplier, SRPConfigMobs.nakArmorMultiplier, SRPConfigMobs.nakHealthMultiplier, null, SRPConfigMobs.nakSpawnRate));
        srpMobConfig.put("sentry", new SRPMobConfig(SRPConfigMobs.unvoEnabled, SRPConfigMobs.unvoDamageMultiplier, SRPConfigMobs.unvoArmorMultiplier, SRPConfigMobs.unvoHealthMultiplier, null, null));
        srpMobConfig.put("sim_adventurer", new SRPMobConfig(SRPConfigMobs.infadventurerEnabled, SRPConfigMobs.infadventurerDamageMultiplier, SRPConfigMobs.infadventurerArmorMultiplier, SRPConfigMobs.infadventurerHealthMultiplier, SRPConfigMobs.infadventurerKDResistanceMultiplier, SRPConfigMobs.infadventurerSpawnRate));
        srpMobConfig.put("sim_bear", new SRPMobConfig(SRPConfigMobs.infbearEnabled, SRPConfigMobs.infbearDamageMultiplier, SRPConfigMobs.infbearArmorMultiplier, SRPConfigMobs.infbearHealthMultiplier, SRPConfigMobs.infbearKDResistanceMultiplier, SRPConfigMobs.infbearSpawnRate));
        srpMobConfig.put("sim_bigspider", new SRPMobConfig(SRPConfigMobs.dorpaEnabled, SRPConfigMobs.dorpaDamageMultiplier, SRPConfigMobs.dorpaArmorMultiplier, SRPConfigMobs.dorpaHealthMultiplier, SRPConfigMobs.dorpaKDResistanceMultiplier, SRPConfigMobs.dorpaSpawnRate));
        srpMobConfig.put("sim_cow", new SRPMobConfig(SRPConfigMobs.infcowEnabled, SRPConfigMobs.infcowDamageMultiplier, SRPConfigMobs.infcowArmorMultiplier, SRPConfigMobs.infcowHealthMultiplier, SRPConfigMobs.infcowKDResistanceMultiplier, SRPConfigMobs.infcowSpawnRate));
        srpMobConfig.put("sim_dragone", new SRPMobConfig(SRPConfigMobs.infdragoneEnabled, SRPConfigMobs.infdragoneDamageMultiplier, SRPConfigMobs.infdragoneArmorMultiplier, SRPConfigMobs.infdragoneHealthMultiplier, SRPConfigMobs.infdragoneKDResistanceMultiplier, SRPConfigMobs.infdragoneSpawnRate));
        srpMobConfig.put("sim_enderman", new SRPMobConfig(SRPConfigMobs.infendermanEnabled, SRPConfigMobs.infendermanDamageMultiplier, SRPConfigMobs.infendermanArmorMultiplier, SRPConfigMobs.infendermanHealthMultiplier, SRPConfigMobs.infendermanKDResistanceMultiplier, SRPConfigMobs.infendermanSpawnRate));
        srpMobConfig.put("sim_horse", new SRPMobConfig(SRPConfigMobs.infhorseEnabled, SRPConfigMobs.infhorseDamageMultiplier, SRPConfigMobs.infhorseArmorMultiplier, SRPConfigMobs.infhorseHealthMultiplier, SRPConfigMobs.infhorseKDResistanceMultiplier, SRPConfigMobs.infhorseSpawnRate));
        srpMobConfig.put("sim_human", new SRPMobConfig(SRPConfigMobs.infhumanEnabled, SRPConfigMobs.infhumanDamageMultiplier, SRPConfigMobs.infhumanArmorMultiplier, SRPConfigMobs.infhumanHealthMultiplier, SRPConfigMobs.infhumanKDResistanceMultiplier, SRPConfigMobs.infhumanSpawnRate));
        srpMobConfig.put("sim_pig", new SRPMobConfig(SRPConfigMobs.infpigEnabled, SRPConfigMobs.infpigDamageMultiplier, SRPConfigMobs.infpigArmorMultiplier, SRPConfigMobs.infpigHealthMultiplier, SRPConfigMobs.infpigKDResistanceMultiplier, SRPConfigMobs.infpigSpawnRate));
        srpMobConfig.put("sim_sheep", new SRPMobConfig(SRPConfigMobs.infsheepEnabled, SRPConfigMobs.infsheepDamageMultiplier, SRPConfigMobs.infsheepArmorMultiplier, SRPConfigMobs.infsheepHealthMultiplier, SRPConfigMobs.infsheepKDResistanceMultiplier, SRPConfigMobs.infsheepSpawnRate));
        srpMobConfig.put("sim_squid", new SRPMobConfig(SRPConfigMobs.infsquidEnabled, SRPConfigMobs.infsquidDamageMultiplier, SRPConfigMobs.infsquidArmorMultiplier, SRPConfigMobs.infsquidHealthMultiplier, SRPConfigMobs.infsquidKDResistanceMultiplier, SRPConfigMobs.infsquidSpawnRate));
        srpMobConfig.put("sim_villager", new SRPMobConfig(SRPConfigMobs.infvillagerEnabled, SRPConfigMobs.infvillagerDamageMultiplier, SRPConfigMobs.infvillagerArmorMultiplier, SRPConfigMobs.infvillagerHealthMultiplier, SRPConfigMobs.infvillagerKDResistanceMultiplier, SRPConfigMobs.infvillagerSpawnRate));
        srpMobConfig.put("sim_wolf", new SRPMobConfig(SRPConfigMobs.infwolfEnabled, SRPConfigMobs.infwolfDamageMultiplier, SRPConfigMobs.infwolfArmorMultiplier, SRPConfigMobs.infwolfHealthMultiplier, SRPConfigMobs.infwolfKDResistanceMultiplier, SRPConfigMobs.infwolfSpawnRate));
        srpMobConfig.put("succor", new SRPMobConfig(SRPConfigMobs.flamEnabled, null, SRPConfigMobs.flamArmorMultiplier, SRPConfigMobs.flamHealthMultiplier, SRPConfigMobs.flamKDResistanceMultiplier, null));
        srpMobConfig.put("thrall", new SRPMobConfig(SRPConfigMobs.thrallEnabled, SRPConfigMobs.thrallDamageMultiplier, SRPConfigMobs.thrallArmorMultiplier, SRPConfigMobs.thrallHealthMultiplier, SRPConfigMobs.thrallKDResistanceMultiplier, SRPConfigMobs.thrallSpawnRate));
        srpMobConfig.put("vigilante", new SRPMobConfig(SRPConfigMobs.angedEnabled, SRPConfigMobs.angedDamageMultiplier, SRPConfigMobs.angedArmorMultiplier, SRPConfigMobs.angedHealthMultiplier, SRPConfigMobs.angedKDResistanceMultiplier, SRPConfigMobs.angedSpawnRate));
        srpMobConfig.put("warden", new SRPMobConfig(SRPConfigMobs.ganroEnabled, SRPConfigMobs.ganroDamageMultiplier, SRPConfigMobs.ganroArmorMultiplier, SRPConfigMobs.ganroHealthMultiplier, SRPConfigMobs.ganroKDResistanceMultiplier, SRPConfigMobs.ganroSpawnRate));
        srpMobConfig.put("worker", new SRPMobConfig(SRPConfigMobs.kolEnabled, SRPConfigMobs.kolDamageMultiplier, SRPConfigMobs.kolArmorMultiplier, SRPConfigMobs.kolHealthMultiplier, SRPConfigMobs.kolKDResistanceMultiplier, SRPConfigMobs.kolSpawnRate));
        srpMobConfig.put("wraith", new SRPMobConfig(SRPConfigMobs.elviaEnabled, SRPConfigMobs.elviaDamageMultiplier, SRPConfigMobs.elviaArmorMultiplier, SRPConfigMobs.elviaHealthMultiplier, SRPConfigMobs.elviaKDResistanceMultiplier, SRPConfigMobs.elviaSpawnRate));

        String unused = "---";
        List<String> configList = new ArrayList<>();
        for (Map.Entry<String, SRPMobConfig> entry : srpMobConfig.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList()))
            configList.add(
                    (entry.getValue().enabled == null ? unused : entry.getValue().enabled) + "\t" +
                            (entry.getValue().healthMulti == null ? unused : entry.getValue().healthMulti) + "\t" +
                            (entry.getValue().dmgMulti == null ? unused : entry.getValue().dmgMulti) + "\t" +
                            (entry.getValue().armorMulti == null ? unused : entry.getValue().armorMulti) + "\t" +
                            (entry.getValue().kbresMulti == null ? unused : entry.getValue().kbresMulti) + "\t" +
                            (entry.getValue().spawnWeight == null ? unused : entry.getValue().spawnWeight) + "\t" +
                            entry.getKey()
            );

        String[] configArray = configList.toArray(configList.toArray(new String[0]));
        SRPMixins.CONFIG.get("general.SRP Mob Config", "SRP Mob Config", SRPMixinsConfigHandler.mobConfig.mobConfig).set(configArray);
        SRPMixinsConfigHandler.mobConfig.mobConfig = configArray;

        SRPMixins.CONFIG.save();
    }

    private static class SRPMobConfig {
        public final Boolean enabled;
        public final Float dmgMulti, armorMulti, healthMulti, kbresMulti;
        public final Integer spawnWeight;

        public SRPMobConfig(Boolean enabled, Float dmgMulti, Float armorMulti, Float healthMulti, Float kbresMulti, Integer spawnWeight) {
            this.enabled = enabled;
            this.dmgMulti = dmgMulti;
            this.armorMulti = armorMulti;
            this.healthMulti = healthMulti;
            this.kbresMulti = kbresMulti;
            this.spawnWeight = spawnWeight;
        }
    }

    public static float getMobConfigDamage(String paraName) {
        return srpMobConfig.get(paraName).dmgMulti;
    }
    public static float getMobConfigArmor(String paraName) {
        return srpMobConfig.get(paraName).armorMulti;
    }
    public static float getMobConfigHealth(String paraName) {
        return srpMobConfig.get(paraName).healthMulti;
    }
    public static float getMobConfigKBRes(String paraName) {
        return srpMobConfig.get(paraName).kbresMulti;
    }
    public static int getMobConfigSpawnWeight(String paraName) {
        return srpMobConfig.get(paraName).spawnWeight;
    }
    public static boolean getMobConfigEnabled(String paraName) {
        return srpMobConfig.get(paraName).enabled;
    }

    public static void registerParasite(String name, int paraId){
        registerParasite(name, paraId, null, null);
    }
    public static void registerParasite(String name, int paraId, @Nullable String group){
        registerParasite(name, paraId, group, null);
    }
    public static void registerParasite(String name, int paraId, @Nullable List<String> variants){
        registerParasite(name, paraId, null, variants);
    }
    public static void registerParasite(String name, int paraId, @Nullable String group, @Nullable List<String> variants){
        mobNameToParaIdMap.put(name, paraId);
        paraIdToMobName.put(paraId, name);
        if(group != null) mobGroupToParaIdMap.get(group).add(paraId);
        if(variants != null) mobNameToVariantsMap.put(name, variants.stream().map(VariantRule.EnumVariant::valueOf).collect(Collectors.toList()));
    }

    static {
        mobNameToParaIdMap.put("pri_longarms", 1);
        mobNameToParaIdMap.put("sim_bigspider", 2);
        mobNameToParaIdMap.put("carrier_heavy", 3);
        mobNameToParaIdMap.put("pri_yelloweye", 4);
        mobNameToParaIdMap.put("buglin", 5);
        mobNameToParaIdMap.put("sim_human", 6);
        mobNameToParaIdMap.put("hi_blaze", 9960);  //TODO: placeholder id
        mobNameToParaIdMap.put("hi_skeleton", 9961); //TODO: placeholder id
        mobNameToParaIdMap.put("pri_manducater", 7);
        mobNameToParaIdMap.put("pri_summoner", 8);
        mobNameToParaIdMap.put("overseer", 9);
        mobNameToParaIdMap.put("pri_reeker", 10);
        mobNameToParaIdMap.put("carrier_flying", 11);
        mobNameToParaIdMap.put("rupter", 12);
        mobNameToParaIdMap.put("sim_cow", 13);
        mobNameToParaIdMap.put("sim_sheep", 14);
        mobNameToParaIdMap.put("sim_wolf", 15);
        mobNameToParaIdMap.put("beckon_si", 16);
        mobNameToParaIdMap.put("pri_bolster", 17);
        mobNameToParaIdMap.put("beckon_sii", 18);
        mobNameToParaIdMap.put("beckon_siii", 19);
        mobNameToParaIdMap.put("anc_overlord", 20);
        mobNameToParaIdMap.put("sim_wolfhead", 21);
        mobNameToParaIdMap.put("sim_sheephead", 22);
        mobNameToParaIdMap.put("movingflesh", 23);
        mobNameToParaIdMap.put("anc_dreadnaut", 24);
        mobNameToParaIdMap.put("vigilante", 25);
        mobNameToParaIdMap.put("sim_pig", 26);
        mobNameToParaIdMap.put("sim_villager", 27);
        mobNameToParaIdMap.put("sim_cowhead", 28);
        mobNameToParaIdMap.put("kyphosis", 29);
        mobNameToParaIdMap.put("sentry", 30);
        mobNameToParaIdMap.put("sim_pighead", 31);
        mobNameToParaIdMap.put("sim_villagerhead", 32);
        mobNameToParaIdMap.put("warden", 33);
        mobNameToParaIdMap.put("anc_pod", 34);
        mobNameToParaIdMap.put("anc_dreadnaut_ten", 35);
        mobNameToParaIdMap.put("worker", 36);
        mobNameToParaIdMap.put("pri_tozoon", 37);
        mobNameToParaIdMap.put("pri_arachnida", 38);
        mobNameToParaIdMap.put("incompleteform_small", 39);
        mobNameToParaIdMap.put("sim_adventurer", 40);
        mobNameToParaIdMap.put("beckon_siv", 41);
        mobNameToParaIdMap.put("incompleteform_medium", 43);
        mobNameToParaIdMap.put("sim_horse", 44);
        mobNameToParaIdMap.put("sim_horsehead", 45);
        mobNameToParaIdMap.put("sim_humanhead", 46);
        mobNameToParaIdMap.put("bomber_light", 47);
        mobNameToParaIdMap.put("host", 48);
        mobNameToParaIdMap.put("sim_bear", 49);
        mobNameToParaIdMap.put("marauder", 50);
        mobNameToParaIdMap.put("ada_longarms", 51);
        mobNameToParaIdMap.put("ada_manducater", 52);
        mobNameToParaIdMap.put("ada_summoner", 53);
        mobNameToParaIdMap.put("ada_reeker", 54);
        mobNameToParaIdMap.put("ada_yelloweye", 55);
        mobNameToParaIdMap.put("ada_bolster", 56);
        mobNameToParaIdMap.put("ada_arachnida", 58);
        mobNameToParaIdMap.put("sim_enderman", 59);
        mobNameToParaIdMap.put("grunt", 60);
        mobNameToParaIdMap.put("crux", 62);
        mobNameToParaIdMap.put("heed", 63);
        mobNameToParaIdMap.put("sim_dragone", 64);
        mobNameToParaIdMap.put("bomber_heavy", 65);
        mobNameToParaIdMap.put("pri_devourer", 66);
        mobNameToParaIdMap.put("sim_endermanhead", 69);
        mobNameToParaIdMap.put("sim_dragonehead", 70);
        mobNameToParaIdMap.put("sim_adventurerhead", 71);
        mobNameToParaIdMap.put("seizer", 72);
        mobNameToParaIdMap.put("dispatcher_si", 73);
        mobNameToParaIdMap.put("dispatcherten", 74);
        mobNameToParaIdMap.put("hostii", 75);
        mobNameToParaIdMap.put("mangler", 76);
        mobNameToParaIdMap.put("dispatcher_sii", 77);
        mobNameToParaIdMap.put("dispatcher_siii", 78);
        mobNameToParaIdMap.put("dispatcher_siv", 79);
        mobNameToParaIdMap.put("thrall", 80);
        mobNameToParaIdMap.put("seeker", 82);
        mobNameToParaIdMap.put("monarch", 84);
        mobNameToParaIdMap.put("wraith", 85);
        mobNameToParaIdMap.put("bogle", 86);
        mobNameToParaIdMap.put("haunter", 87);
        mobNameToParaIdMap.put("carrier_colony", 88);
        mobNameToParaIdMap.put("succor", 89);
        mobNameToParaIdMap.put("architect", 90);
        mobNameToParaIdMap.put("gnat", 91);
        mobNameToParaIdMap.put("pri_vermin", 92);
        mobNameToParaIdMap.put("fer_cow", 93);
        mobNameToParaIdMap.put("fer_enderman", 94);
        mobNameToParaIdMap.put("fer_horse", 95);
        mobNameToParaIdMap.put("fer_human", 96);
        mobNameToParaIdMap.put("fer_pig", 97);
        mobNameToParaIdMap.put("fer_sheep", 98);
        mobNameToParaIdMap.put("fer_villager", 99);
        mobNameToParaIdMap.put("tendril", 202);
        mobNameToParaIdMap.put("biomass", 205);
        mobNameToParaIdMap.put("wave", 211);
        mobNameToParaIdMap.put("waveshock", 213);
        mobNameToParaIdMap.put("fer_wolf", 300);
        mobNameToParaIdMap.put("hi_golem", 301);
        mobNameToParaIdMap.put("carrier_light", 304);
        mobNameToParaIdMap.put("fer_bear", 306);
        mobNameToParaIdMap.put("sim_squid", 307);
        mobNameToParaIdMap.put("worm", 308);

        paraIdToMobName = mobNameToParaIdMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        mobGroupToParaIdMap.put("ADAPTED", new ArrayList<>(Arrays.asList(51, 52, 53, 54, 55, 56, 58)));
        mobGroupToParaIdMap.put("ANCIENT", new ArrayList<>(Arrays.asList(20, 24, 35)));
        mobGroupToParaIdMap.put("ASSIMILATED", new ArrayList<>(Arrays.asList(2, 6, 13, 14, 15, 21, 22, 26, 27, 28, 31, 32, 40, 44, 45, 46, 49, 59, 64, 69, 70, 71, 307)));
        mobGroupToParaIdMap.put("CRUDE", new ArrayList<>(Arrays.asList(23, 39, 43, 48, 62, 63, 75, 80)));
        mobGroupToParaIdMap.put("DETERRENT", new ArrayList<>(Arrays.asList(29, 30, 72, 74, 308)));
        mobGroupToParaIdMap.put("FERAL", new ArrayList<>(Arrays.asList(93, 94, 95, 96, 97, 98, 99, 300, 306)));
        mobGroupToParaIdMap.put("HIJACKED", new ArrayList<>(Collections.singletonList(301)));
        mobGroupToParaIdMap.put("INBORN", new ArrayList<>(Arrays.asList(3, 5, 11, 12, 36, 76, 91, 304)));
        mobGroupToParaIdMap.put("NEXUS", new ArrayList<>(Arrays.asList(16, 18, 19, 41, 73, 77, 78, 79)));
        mobGroupToParaIdMap.put("PREEMINENT", new ArrayList<>(Arrays.asList(65, 82, 85, 86, 87, 88, 89, 90)));
        mobGroupToParaIdMap.put("PRIMITIVE", new ArrayList<>(Arrays.asList(1, 4, 7, 8, 10, 17, 37, 38, 66, 92)));
        mobGroupToParaIdMap.put("PURE", new ArrayList<>(Arrays.asList(9, 25, 33, 47, 50, 60, 84)));

        mobNameToParaTypeMap.put("beckon_si", (byte) 0);
        mobNameToParaTypeMap.put("beckon_sii", (byte) 0);
        mobNameToParaTypeMap.put("beckon_siii", (byte) 0);
        mobNameToParaTypeMap.put("sim_wolfhead", (byte) 0);
        mobNameToParaTypeMap.put("sim_sheephead", (byte) 0);
        mobNameToParaTypeMap.put("sim_cowhead", (byte) 0);
        mobNameToParaTypeMap.put("sim_pighead", (byte) 0);
        mobNameToParaTypeMap.put("sim_villagerhead", (byte) 0);
        mobNameToParaTypeMap.put("beckon_siv", (byte) 0);
        mobNameToParaTypeMap.put("sim_horsehead", (byte) 0);
        mobNameToParaTypeMap.put("sim_humanhead", (byte) 0);
        mobNameToParaTypeMap.put("sim_endermanhead", (byte) 0);
        mobNameToParaTypeMap.put("sim_dragonehead", (byte) 0);
        mobNameToParaTypeMap.put("sim_adventurerhead", (byte) 0);
        mobNameToParaTypeMap.put("dispatcher_si", (byte) 0);
        mobNameToParaTypeMap.put("dispatcher_sii", (byte) 0);
        mobNameToParaTypeMap.put("dispatcher_siii", (byte) 0);
        mobNameToParaTypeMap.put("dispatcher_siv", (byte) 0);
        mobNameToParaTypeMap.put("tendril", (byte) 0);
        mobNameToParaTypeMap.put("wave", (byte) 0);
        mobNameToParaTypeMap.put("waveshock", (byte) 0);
        mobNameToParaTypeMap.put("buglin", (byte) 1);
        mobNameToParaTypeMap.put("rupter", (byte) 5);
        mobNameToParaTypeMap.put("gnat", (byte) 5);
        mobNameToParaTypeMap.put("worker", (byte) 7);
        mobNameToParaTypeMap.put("sim_human", (byte) 11);
        mobNameToParaTypeMap.put("hi_blaze", (byte) 11);
        mobNameToParaTypeMap.put("hi_skeleton", (byte) 11);
        mobNameToParaTypeMap.put("sim_cow", (byte) 11);
        mobNameToParaTypeMap.put("sim_villager", (byte) 11);
        mobNameToParaTypeMap.put("incompleteform_small", (byte) 11);
        mobNameToParaTypeMap.put("sim_adventurer", (byte) 11);
        mobNameToParaTypeMap.put("incompleteform_medium", (byte) 11);
        mobNameToParaTypeMap.put("sim_horse", (byte) 11);
        mobNameToParaTypeMap.put("host", (byte) 11);
        mobNameToParaTypeMap.put("sim_bear", (byte) 11);
        mobNameToParaTypeMap.put("fer_cow", (byte) 11);
        mobNameToParaTypeMap.put("fer_enderman", (byte) 11);
        mobNameToParaTypeMap.put("fer_horse", (byte) 11);
        mobNameToParaTypeMap.put("fer_human", (byte) 11);
        mobNameToParaTypeMap.put("fer_pig", (byte) 11);
        mobNameToParaTypeMap.put("fer_sheep", (byte) 11);
        mobNameToParaTypeMap.put("fer_villager", (byte) 11);
        mobNameToParaTypeMap.put("fer_wolf", (byte) 11);
        mobNameToParaTypeMap.put("hi_golem", (byte) 11);
        mobNameToParaTypeMap.put("fer_bear", (byte) 11);
        mobNameToParaTypeMap.put("sim_sheep", (byte) 12);
        mobNameToParaTypeMap.put("sim_wolf", (byte) 13);
        mobNameToParaTypeMap.put("sim_bigspider", (byte) 14);
        mobNameToParaTypeMap.put("sim_enderman", (byte) 14);
        mobNameToParaTypeMap.put("sim_dragone", (byte) 14);
        mobNameToParaTypeMap.put("sim_pig", (byte) 15);
        mobNameToParaTypeMap.put("sim_squid", (byte) 15);
        mobNameToParaTypeMap.put("pri_longarms", (byte) 31);
        mobNameToParaTypeMap.put("pri_yelloweye", (byte) 31);
        mobNameToParaTypeMap.put("pri_manducater", (byte) 31);
        mobNameToParaTypeMap.put("pri_summoner", (byte) 31);
        mobNameToParaTypeMap.put("pri_reeker", (byte) 31);
        mobNameToParaTypeMap.put("carrier_flying", (byte) 31);
        mobNameToParaTypeMap.put("pri_bolster", (byte) 31);
        mobNameToParaTypeMap.put("pri_tozoon", (byte) 31);
        mobNameToParaTypeMap.put("pri_arachnida", (byte) 31);
        mobNameToParaTypeMap.put("heed", (byte) 31);
        mobNameToParaTypeMap.put("pri_devourer", (byte) 31);
        mobNameToParaTypeMap.put("thrall", (byte) 31);
        mobNameToParaTypeMap.put("carrier_colony", (byte) 31);
        mobNameToParaTypeMap.put("pri_vermin", (byte) 31);
        mobNameToParaTypeMap.put("kyphosis", (byte) 40);
        mobNameToParaTypeMap.put("sentry", (byte) 40);
        mobNameToParaTypeMap.put("seizer", (byte) 40);
        mobNameToParaTypeMap.put("dispatcherten", (byte) 40);
        mobNameToParaTypeMap.put("worm", (byte) 40);
        mobNameToParaTypeMap.put("carrier_heavy", (byte) 41);
        mobNameToParaTypeMap.put("ada_longarms", (byte) 41);
        mobNameToParaTypeMap.put("ada_manducater", (byte) 41);
        mobNameToParaTypeMap.put("ada_summoner", (byte) 41);
        mobNameToParaTypeMap.put("ada_reeker", (byte) 41);
        mobNameToParaTypeMap.put("ada_yelloweye", (byte) 41);
        mobNameToParaTypeMap.put("ada_bolster", (byte) 41);
        mobNameToParaTypeMap.put("ada_arachnida", (byte) 41);
        mobNameToParaTypeMap.put("crux", (byte) 41);
        mobNameToParaTypeMap.put("hostii", (byte) 41);
        mobNameToParaTypeMap.put("carrier_light", (byte) 41);
        mobNameToParaTypeMap.put("overseer", (byte) 51);
        mobNameToParaTypeMap.put("vigilante", (byte) 51);
        mobNameToParaTypeMap.put("warden", (byte) 51);
        mobNameToParaTypeMap.put("bomber_light", (byte) 51);
        mobNameToParaTypeMap.put("marauder", (byte) 51);
        mobNameToParaTypeMap.put("grunt", (byte) 51);
        mobNameToParaTypeMap.put("mangler", (byte) 51);
        mobNameToParaTypeMap.put("seeker", (byte) 51);
        mobNameToParaTypeMap.put("monarch", (byte) 51);
        mobNameToParaTypeMap.put("architect", (byte) 51);
        mobNameToParaTypeMap.put("anc_pod", (byte) 61);
        mobNameToParaTypeMap.put("bomber_heavy", (byte) 61);
        mobNameToParaTypeMap.put("wraith", (byte) 61);
        mobNameToParaTypeMap.put("bogle", (byte) 61);
        mobNameToParaTypeMap.put("succor", (byte) 61);
        mobNameToParaTypeMap.put("anc_dreadnaut", (byte) 62);
        mobNameToParaTypeMap.put("anc_dreadnaut_ten", (byte) 62);
        mobNameToParaTypeMap.put("anc_overlord", (byte) 63);
        mobNameToParaTypeMap.put("haunter", (byte) 63);
        mobNameToParaTypeMap.put("movingflesh", (byte) 100);
        mobNameToParaTypeMap.put("biomass", (byte) 100);

        mobNameToVariantsMap.put("ada_arachnida", Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        mobNameToVariantsMap.put("ada_bolster", Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        mobNameToVariantsMap.put("ada_longarms", Arrays.asList(SPECIAL, VIRULENT, BERSERKER, BREACHER));
        mobNameToVariantsMap.put("ada_manducater", Collections.singletonList(BREACHER));
        mobNameToVariantsMap.put("ada_reeker", Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        mobNameToVariantsMap.put("ada_summoner", Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        mobNameToVariantsMap.put("ada_yelloweye", Collections.singletonList(BREACHER));
        mobNameToVariantsMap.put("pri_arachnida", Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        mobNameToVariantsMap.put("pri_bolster", Arrays.asList(VIRULENT, BREACHER));
        mobNameToVariantsMap.put("pri_devourer", Collections.singletonList(BREACHER));
        mobNameToVariantsMap.put("pri_longarms", Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        mobNameToVariantsMap.put("pri_manducater", Collections.singletonList(BREACHER));
        mobNameToVariantsMap.put("pri_reeker", Arrays.asList(SPECIAL, VIRULENT, BERSERKER, BREACHER));
        mobNameToVariantsMap.put("pri_summoner", Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        mobNameToVariantsMap.put("pri_yelloweye", Collections.singletonList(BREACHER));
        mobNameToVariantsMap.put("rupter", Arrays.asList(VIRULENT, BERSERKER));
        mobNameToVariantsMap.put("mangler", Arrays.asList(VIRULENT, BERSERKER));
        mobNameToVariantsMap.put("thrall", Collections.singletonList(SPECIAL));
        mobNameToVariantsMap.put("carrier_flying", Collections.singletonList(SPECIAL));
        mobNameToVariantsMap.put("carrier_light", Collections.singletonList(SPECIAL));
        mobNameToVariantsMap.put("carrier_heavy", Collections.singletonList(SPECIAL));
        mobNameToVariantsMap.put("sim_enderman", Collections.singletonList(SPECIAL));
        mobNameToVariantsMap.put("sim_squid", Collections.singletonList(BREACHER));
        mobNameToVariantsMap.put("overseer", Collections.singletonList(BREACHER));
        mobNameToVariantsMap.put("vigilante", Collections.singletonList(BREACHER));
        mobNameToVariantsMap.put("marauder", Collections.singletonList(BREACHER));
        mobNameToVariantsMap.put("grunt", Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        mobNameToVariantsMap.put("warden", Collections.singletonList(BREACHER));
        mobNameToVariantsMap.put("bomber_light", Collections.singletonList(BREACHER));
        mobNameToVariantsMap.put("monarch", Arrays.asList(SPECIAL, BREACHER));
        mobNameToVariantsMap.put("haunter", Collections.singletonList(SPECIAL));
        mobNameToVariantsMap.put("carrier_colony", Collections.singletonList(SPECIAL));
    }
}
