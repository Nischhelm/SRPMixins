package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class DamageFixConfig {
    @Config.Comment("Set to false to disable all fixes for parasite damages")
    @Config.Name("Damage Fix: Global switch")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.damagefix.json", defaultValue = true)
    public boolean doDamageFixes = true;

    @Config.Comment("Ancient Overlord homing missile base damage. Will be increased by various multipliers (parasite specific, global, dimensionspecific)")
    @Config.Name("Damage Fix: Overlord projectile base damage")
    @Config.RangeInt(min = 0)
    public float overlordProjectileDamage = 50;

    @Config.Comment("Haunter homing missile base damage. Will be increased by various multipliers (parasite specific, global, dimensionspecific)")
    @Config.Name("Damage Fix: Haunter projectile base damage")
    @Config.RangeInt(min = 0)
    public float haunterProjectileDamage = 32;

    @Config.Comment("Ancient Dreadnaught melee aura base damage. Will be increased by various multipliers (parasite specific, global, dimensionspecific)")
    @Config.Name("Damage Fix: Dreadnaught melee aura base damage")
    @Config.RangeInt(min = 0)
    public float dreadnaughtMeleeDamage = 32;

    @Config.Comment("Bogle melee aura base damage. Will be increased by various multipliers (parasite specific, global, dimensionspecific)")
    @Config.Name("Damage Fix: Bogle melee aura base damage")
    @Config.RangeInt(min = 0)
    public float bogleMeleeDamage = 25;

    @Config.Comment("Wraith melee aura base damage. Will be increased by various multipliers (parasite specific, global, dimensionspecific)")
    @Config.Name("Damage Fix: Wraith melee aura base damage")
    @Config.RangeInt(min = 0)
    public float wraithMeleeDamage = 25;

    @Config.Comment("Makes Succors deal fixed damage instead of 2 times its creator's dmg")
    @Config.Name("Fix Succor Damage")
    public boolean fixSuccorDamage = true;

    @Config.Comment("How much damage Succors should deal (x6 in Hard mode with x4 multiplier)")
    @Config.Name("Fix Succor Damage - Dealt damage")
    @Config.RangeInt(min = 0)
    public float fixedSuccorDamage = 30;

    @Config.Comment("Makes Arachnida Pullballs have modifiable hit range sizes")
    @Config.Name("Fix Arachnida Pull")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.arachnidapullballhit.json", defaultValue = true)
    public boolean changeArachnidaPull = true;

    @Config.Comment("How big of an area to scan for impacts with entities for the primitive arachnidas pullball. The value is what is added on top of the actual sidelength of the projectiles bounding box, so 0 would be using only its own bounding box.")
    @Config.Name("Fix Arachnida Pull - Primitive Range")
    @Config.RangeDouble(min = 0)
    public float primArachPullBallHit = 2F;

    @Config.Comment("How big of an area to scan for impacts with entities for the adapted arachnidas pullball. The value is what is added on top of the actual sidelength of the projectiles bounding box, so 0 would be using only its own bounding box.")
    @Config.Name("Fix Arachnida Pull - Adapted Range")
    @Config.RangeInt(min = 0)
    public float adaArachPullBallHit = 2.5F;

    @Config.Comment("Add rules to disable parasite variants. You can specify:\n" +
            "\t- group: mob group (INBORN, PRIMITIVE, ADAPTED, PURE, PREEMINENT)\n" +
            "\t- mob: specific mob or spacebar-separated list of mobs\n" +
            "\t- dim: dimension id\n" +
            "\t- phase: minphase and/or maxphase using operations >,<,=,<=,>=\n" +
            "\t- variant: VIRULENT (green), BERSERKER (red), BREACHER (purple/dark) or SPECIAL (see below), can be multiple spacebar-separated ones" +
            "In situations where a rule is applicable, the named variant will be disabled.\n" +
            "Example: To disable all breacher parasites until phase 5 you do \"[phase <= 5] [variant = BREACHER]\"\n" +
            "Available variants:\n" +
            "Parasites that have VIRULENT, BERSERKER and BREACHER variants: ada_arachnida, ada_bolster, ada_longarms, ada_reeker, ada_summoner, pri_arachnida, pri_longarms, pri_reeker, pri_summoner, grunt\n" +
            "Parasites that have a BREACHER variant but no VIRULENT/BERSERKER: ada_manducater, ada_yelloweye, pri_devourer, pri_manducater, pri_yelloweye, all pures except grunt (light_bomber, marauder, monarch, overseer, vigilante, warden)\n" +
            "Rupters and Manglers only have VIRULENT and BERSERKER variants, but no BREACHER.\n" +
            "Primitive Bolsters only have VIRULENT and BREACHER variants, but no BERSERKER\n" +
            "SPECIAL cases: \n" +
            "\t- Variants with changed stats: ada_longarms (tyrant), carrier_colony (armored), haunter (armored), pri_reeker (pale), monarch (deviantive), thrall (unnamed, no skin change)\n" +
            "\t- All three carriers (light_carrier, heavy_carrier, flying_carrier) have empty variant\n" +
            "\t- sim_enderman has crawling variant"
    )
    @Config.Name("Variant Disable Rules")
    public String[] variantRules = {
    };
}
