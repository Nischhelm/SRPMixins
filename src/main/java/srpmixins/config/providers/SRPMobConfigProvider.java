package srpmixins.config.providers;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import net.minecraftforge.fml.common.Loader;
import srpmixins.SRPMixins;
import srpmixins.compat.SRPExtraCompat;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.rules.rulesets.VariantDisableRuleSet;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static srpmixins.rules.rulesets.VariantDisableRuleSet.EnumVariant.*;

public class SRPMobConfigProvider {
    public static final Map<String, Integer> mobNameToParaIdMap = new HashMap<>();
    public static final Map<String, Byte> mobNameToParaTypeMap = new HashMap<>();
    public static final Map<Integer, String> paraIdToMobName = new HashMap<>();
    public static final Map<String, List<Integer>> mobGroupToParaIdMap = new HashMap<>();
    public static final Map<String, List<VariantDisableRuleSet.EnumVariant>> mobNameToVariantsMap = new HashMap<>();

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

    public static String getParaGroup(int paraId){
        for (Map.Entry<String, List<Integer>> entry : SRPMobConfigProvider.mobGroupToParaIdMap.entrySet())
            if (entry.getValue().contains(paraId)) {
                return entry.getKey();
            }
        return "";
    }

    public static void registerParasite(String name, int paraId){
        registerParasite(name, paraId, null, null, null);
    }
    public static void registerParasite(String name, int paraId, @Nullable String group){
        registerParasite(name, paraId, group, null, null);
    }
    public static void registerParasite(String name, int paraId, @Nullable String group, @Nullable Byte typeId){
        registerParasite(name, paraId, group, typeId, null);
    }
    public static void registerParasite(String name, int paraId, @Nullable String group, @Nullable Byte typeId, @Nullable List<VariantDisableRuleSet.EnumVariant> variants){
        mobNameToParaIdMap.put(name, paraId);
        paraIdToMobName.put(paraId, name);
        if(group != null) mobGroupToParaIdMap.computeIfAbsent(group, newGroup -> new ArrayList<>()).add(paraId);
        if(typeId != null) mobNameToParaTypeMap.put(name, typeId);
        if(variants != null) mobNameToVariantsMap.put(name, variants);
    }

    public static void registerMobs() {
        registerParasite("pri_longarms", 1, "PRIMITIVE", (byte) 31, Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        registerParasite("sim_bigspider", 2, "ASSIMILATED", (byte) 14);
        registerParasite("carrier_heavy", 3, "INBORN", (byte) 41, Collections.singletonList(SPECIAL));
        registerParasite("pri_yelloweye", 4, "PRIMITIVE", (byte) 31, Collections.singletonList(BREACHER));
        registerParasite("buglin", 5, "INBORN", (byte) 1);
        registerParasite("sim_human", 6, "ASSIMILATED", (byte) 11);
        registerParasite("hi_blaze", 9960, "HIJACKED", (byte) 11); //TODO: temp para id
        registerParasite("hi_skeleton", 9961, "HIJACKED", (byte) 11); //TODO: temp para id
        registerParasite("pri_manducater", 7, "PRIMITIVE", (byte) 31, Collections.singletonList(BREACHER));
        registerParasite("pri_summoner", 8, "PRIMITIVE", (byte) 31, Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        registerParasite("overseer", 9, "PURE", (byte) 51, Collections.singletonList(BREACHER));
        registerParasite("pri_reeker", 10, "PRIMITIVE", (byte) 31, Arrays.asList(SPECIAL, VIRULENT, BERSERKER, BREACHER));
        registerParasite("carrier_flying", 11, "INBORN", (byte) 31, Collections.singletonList(SPECIAL));
        registerParasite("rupter", 12, "INBORN", (byte) 5, Arrays.asList(VIRULENT, BERSERKER));
        registerParasite("sim_cow", 13, "ASSIMILATED", (byte) 11);
        registerParasite("sim_sheep", 14, "ASSIMILATED", (byte) 12);
        registerParasite("sim_wolf", 15, "ASSIMILATED", (byte) 13);
        registerParasite("beckon_si", 16, "NEXUS", (byte) 0);
        registerParasite("pri_bolster", 17, "PRIMITIVE", (byte) 31, Arrays.asList(VIRULENT, BREACHER));
        registerParasite("beckon_sii", 18, "NEXUS", (byte) 0);
        registerParasite("beckon_siii", 19, "NEXUS", (byte) 0);
        registerParasite("anc_overlord", 20, "ANCIENT", (byte) 63);
        registerParasite("sim_wolfhead", 21, "ASSIMILATED", (byte) 0);
        registerParasite("sim_sheephead", 22, "ASSIMILATED", (byte) 0);
        registerParasite("movingflesh", 23, "CRUDE", (byte) 100);
        registerParasite("anc_dreadnaut", 24, "ANCIENT", (byte) 62);
        registerParasite("vigilante", 25, "PURE", (byte) 51, Collections.singletonList(BREACHER));
        registerParasite("sim_pig", 26, "ASSIMILATED", (byte) 15);
        registerParasite("sim_villager", 27, "ASSIMILATED", (byte) 11);
        registerParasite("sim_cowhead", 28, "ASSIMILATED", (byte) 0);
        registerParasite("kyphosis", 29, "DETERRENT", (byte) 40);
        registerParasite("sentry", 30, "DETERRENT", (byte) 40);
        registerParasite("sim_pighead", 31, "ASSIMILATED", (byte) 0);
        registerParasite("sim_villagerhead", 32, "ASSIMILATED", (byte) 0);
        registerParasite("warden", 33, "PURE", (byte) 51, Collections.singletonList(BREACHER));
        registerParasite("anc_pod", 34, null, (byte) 61);
        registerParasite("anc_dreadnaut_ten", 35, "ANCIENT", (byte) 62);
        registerParasite("worker", 36, "INBORN", (byte) 7);
        registerParasite("pri_tozoon", 37, "PRIMITIVE", (byte) 31);
        registerParasite("pri_arachnida", 38, "PRIMITIVE", (byte) 31, Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        registerParasite("incompleteform_small", 39, "CRUDE", (byte) 11);
        registerParasite("sim_adventurer", 40, "ASSIMILATED", (byte) 11);
        registerParasite("beckon_siv", 41, "NEXUS", (byte) 0);
        registerParasite("incompleteform_medium", 43, "CRUDE", (byte) 11);
        registerParasite("sim_horse", 44, "ASSIMILATED", (byte) 11);
        registerParasite("sim_horsehead", 45, "ASSIMILATED", (byte) 0);
        registerParasite("sim_humanhead", 46, "ASSIMILATED", (byte) 0);
        registerParasite("bomber_light", 47, "PURE", (byte) 51, Collections.singletonList(BREACHER));
        registerParasite("host", 48, "CRUDE", (byte) 11);
        registerParasite("sim_bear", 49, "ASSIMILATED", (byte) 11);
        registerParasite("marauder", 50, "PURE", (byte) 51, Collections.singletonList(BREACHER));
        registerParasite("ada_longarms", 51, "ADAPTED", (byte) 41, Arrays.asList(SPECIAL, VIRULENT, BERSERKER, BREACHER));
        registerParasite("ada_manducater", 52, "ADAPTED", (byte) 41, Collections.singletonList(BREACHER));
        registerParasite("ada_summoner", 53, "ADAPTED", (byte) 41, Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        registerParasite("ada_reeker", 54, "ADAPTED", (byte) 41, Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        registerParasite("ada_yelloweye", 55, "ADAPTED", (byte) 41, Collections.singletonList(BREACHER));
        registerParasite("ada_bolster", 56, "ADAPTED", (byte) 41, Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        registerParasite("ada_arachnida", 58, "ADAPTED", (byte) 41, Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        registerParasite("sim_enderman", 59, "ASSIMILATED", (byte) 14, Collections.singletonList(SPECIAL));
        registerParasite("grunt", 60, "PURE", (byte) 51, Arrays.asList(VIRULENT, BERSERKER, BREACHER));
        registerParasite("crux", 62, "CRUDE", (byte) 41);
        registerParasite("heed", 63, "CRUDE", (byte) 31);
        registerParasite("sim_dragone", 64, "ASSIMILATED", (byte) 14);
        registerParasite("bomber_heavy", 65, "PREEMINENT", (byte) 61);
        registerParasite("pri_devourer", 66, "PRIMITIVE", (byte) 31, Collections.singletonList(BREACHER));
        registerParasite("sim_endermanhead", 69, "ASSIMILATED", (byte) 0);
        registerParasite("sim_dragonehead", 70, "ASSIMILATED", (byte) 0);
        registerParasite("sim_adventurerhead", 71, "ASSIMILATED", (byte) 0);
        registerParasite("seizer", 72, "DETERRENT", (byte) 40);
        registerParasite("dispatcher_si", 73, "NEXUS", (byte) 0);
        registerParasite("dispatcherten", 74, "DETERRENT", (byte) 40);
        registerParasite("hostii", 75, "CRUDE", (byte) 41);
        registerParasite("mangler", 76, "INBORN", (byte) 51, Arrays.asList(VIRULENT, BERSERKER));
        registerParasite("dispatcher_sii", 77, "NEXUS", (byte) 0);
        registerParasite("dispatcher_siii", 78, "NEXUS", (byte) 0);
        registerParasite("dispatcher_siv", 79, "NEXUS", (byte) 0);
        registerParasite("thrall", 80, "CRUDE", (byte) 31, Collections.singletonList(SPECIAL));
        registerParasite("seeker", 82, null, (byte) 51);
        registerParasite("monarch", 84, "PURE", (byte) 51, Arrays.asList(SPECIAL, BREACHER));
        registerParasite("wraith", 85, "PREEMINENT", (byte) 61);
        registerParasite("bogle", 86, "PREEMINENT", (byte) 61);
        registerParasite("haunter", 87, "PREEMINENT", (byte) 63, Collections.singletonList(SPECIAL));
        registerParasite("carrier_colony", 88, "PREEMINENT", SRPMixinsConfigHandler.spawns.fixColonyCarrierTypeId ? (byte) 63 : (byte) 31, Collections.singletonList(SPECIAL));
        registerParasite("succor", 89, "PREEMINENT", (byte) 61);
        registerParasite("architect", 90, "PREEMINENT", (byte) 51);
        registerParasite("gnat", 91, "INBORN", (byte) 5);
        registerParasite("pri_vermin", 92, "PRIMITIVE", (byte) 31);
        registerParasite("fer_cow", 93, "FERAL", (byte) 11);
        registerParasite("fer_enderman", 94, "FERAL", (byte) 11);
        registerParasite("fer_horse", 95, "FERAL", (byte) 11);
        registerParasite("fer_human", 96, "FERAL", (byte) 11);
        registerParasite("fer_pig", 97, "FERAL", (byte) 11);
        registerParasite("fer_sheep", 98, "FERAL", (byte) 11);
        registerParasite("fer_villager", 99, "FERAL", (byte) 11);
        registerParasite("tendril", 202, null, (byte) 0);
        registerParasite("biomass", 205, null, (byte) 100);
        registerParasite("wave", 211, null, (byte) 0);
        registerParasite("waveshock", 213, null, (byte) 0);
        registerParasite("fer_wolf", 300, "FERAL", (byte) 11);
        registerParasite("hi_golem", 301, "HIJACKED", (byte) 11);
        registerParasite("carrier_light", 304, "INBORN", (byte) 41, Collections.singletonList(SPECIAL));
        registerParasite("fer_bear", 306, "FERAL", (byte) 11);
        registerParasite("sim_squid", 307, "ASSIMILATED", (byte) 15, Collections.singletonList(BREACHER));
        registerParasite("worm", 308, "DETERRENT", (byte) 40);

        if(Loader.isModLoaded("srpextra")){
            SRPExtraCompat.init();
            registerSRPExtraMobs();
        }
        if(Loader.isModLoaded("srpdeepseadanger")) registerSRPDeepSeaDangerMobs();
        if(Loader.isModLoaded("srpquark")) registerSRPQuarkMobs();
        if(Loader.isModLoaded("srpmutantbeasts")) registerSRPMutantBeastsMobs();
    }
    
    public static void registerSRPExtraMobs(){
        registerParasite("hijacked_creeper", -1, "HIJACKED");
        registerParasite("hijacked_creeper_head", -257, "HIJACKED"); //Not actually like that in code yet
        registerParasite("hijacked_skeleton", -2, "HIJACKED");
        registerParasite("hijacked_skeleton_head", -258, "HIJACKED"); //Not actually like that in code yet
        registerParasite("hijacked_skeleton_stray", -3, "HIJACKED");
        registerParasite("stalker", -4, "PRIMITIVE");
        registerParasite("sim_witch", -5, "ASSIMILATED");
        registerParasite("sim_witch_head", -261, "ASSIMILATED"); //Not actually like that in code yet
        registerParasite("sim_vindicator", -6, "ASSIMILATED");
        registerParasite("sim_vindicator_head", -262, "ASSIMILATED"); //Not actually like that in code yet
        registerParasite("sim_evoker", -7, "ASSIMILATED");
        registerParasite("sim_ocelot", -8, "ASSIMILATED");
        registerParasite("sim_ocelot_head", -264, "ASSIMILATED"); //Not actually like that in code yet
        registerParasite("ada_vermin", -9, "ADAPTED");
    }
    public static void registerSRPDeepSeaDangerMobs(){
        registerParasite("swimmer", -10, "INBORN");
        registerParasite("leecher", -11, "INBORN");
        registerParasite("sim_drowned", -23, "ASSIMILATED");
        registerParasite("sim_dolphin", -24, "ASSIMILATED");
        registerParasite("sim_dolphin_head", -280, "ASSIMILATED"); //Not actually like that in code yet
        registerParasite("sim_fish", -25, "ASSIMILATED");
        registerParasite("fer_dolphin", -536, "FERAL"); //Not actually like that in code yet
        registerParasite("pri_hammerhead", -26, "PRIMITIVE");
        registerParasite("ada_hammerhead", -27, "ADAPTED");
        registerParasite("sprouter_si", -28, "NEXUS");
        registerParasite("sprouter_sii", -29, "NEXUS");
        registerParasite("sprouter_siii", -30, "NEXUS");
        registerParasite("sprouter_siv", -31, "NEXUS");
        registerParasite("plankton", -32, "INBORN");
        registerParasite("carrier_sea", -33, "INBORN");
        registerParasite("bomber_mini", -34, "INBORN");
        registerParasite("supporter", -35, "PURE");
        registerParasite("hi_guardian", -38, "HIJACKED");
        registerParasite("hi_elder_guardian", -39, "HIJACKED");
        registerParasite("fer_fish", -40, "FERAL"); //Not actually like that in code yet
        registerParasite("ada_devourer", -41, "ADAPTED"); //Not actually like that in code yet
        registerParasite("tendril_hammerhead", -283);
    }
    public static void registerSRPQuarkMobs(){
        registerParasite("hijacked_skeleton_pirate", -71, "HIJACKED");
        registerParasite("hijacked_skeleton_ashen", -72, "HIJACKED");
        registerParasite("sim_dweller", -73, "ASSIMILATED");
        registerParasite("sim_archaeologist", -74, "ASSIMILATED");
        registerParasite("sim_stoneling", -75, "ASSIMILATED");
        registerParasite("sim_crab", -76, "ASSIMILATED");
        registerParasite("sim_frog", -77, "ASSIMILATED");
    }
    public static void registerSRPMutantBeastsMobs(){
        registerParasite("assimilated_mutant_zombie", -81, "ASSIMILATED");
        registerParasite("assimilated_mutant_enderman", -82, "ASSIMILATED");
        registerParasite("hijacked_mutant_skeleton", -83, "HIJACKED");
    }
}
