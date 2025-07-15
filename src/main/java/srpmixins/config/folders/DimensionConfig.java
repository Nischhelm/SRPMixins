package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class DimensionConfig {
    @Config.Comment("Set to false to fully disable dimension stat+drop+mobcap multipliers")
    @Config.Name("Parasite Stat+Drop Multiplier: Global switch")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.dimensionmultis.json", defaultValue = true)
    public boolean doMultipliers = true;

    @Config.Comment("Changes the global health multiplier of SRP config to be dimension specific. This happens on top of the SRP global multiplier! Pattern: dimension, multiplier")
    @Config.Name("Parasite Health Multipliers")
    public String[] dimensionHealthMultipliers = {
            "-1,1",
            "0,1",
            "1,1"
    };

    @Config.Comment("Changes the global damage multiplier of SRP config to be dimension specific. This happens on top of the SRP global multiplier! Pattern: dimension, multiplier")
    @Config.Name("Parasite Dmg Multipliers")
    public String[] dimensionDmgMultipliers = {
            "-1,1",
            "0,1",
            "1,1"
    };

    @Config.Comment("Changes the global armor multiplier of SRP config to be dimension specific. This happens on top of the SRP global multiplier! Pattern: dimension, multiplier")
    @Config.Name("Parasite Armor Multipliers")
    public String[] dimensionArmorMultipliers = {
            "-1,1",
            "0,1",
            "1,1"
    };

    @Config.Comment("Changes the global stat knockback resistance multiplier of SRP config to be dimension specific. This happens on top of the SRP global multiplier! Pattern: dimension, multiplier")
    @Config.Name("Parasite KBRes Multipliers")
    public String[] dimensionKBResMultipliers = {
            "-1,1",
            "0,1",
            "1,1"
    };

    @Config.Comment("Decreases drop chance of SRP Items per dimension. Set to 1 for default behavior")
    @Config.Name("Parasite Drop chance Multipliers")
    public String[] dimensionDropMultipliers = {
            "-1,1",
            "0,1",
            "1,1"
    };

    @Config.Comment("Increases parasite mob cap and per player cap by this multiplier per dimension.\n" +
            "Doesn't work if \"Fix Spawning Entirely\" is enabled, use \"Parasite Mob Cap Rules\" for that")
    @Config.Name("Parasite mob cap Multipliers")
    public String[] dimensionMobCapMultipliers = {
            "-1,1",
            "0,1",
            "1,1"
    };
}
