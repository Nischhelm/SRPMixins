package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class DamageFixConfig {
    @Config.Comment("Set to false to disable all fixes for parasite damages")
    @Config.Name("Damage Fix: Global switch")
    @Config.RequiresMcRestart
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
}
