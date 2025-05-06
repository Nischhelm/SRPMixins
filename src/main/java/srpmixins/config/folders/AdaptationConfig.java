package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class AdaptationConfig {
    @Config.Comment("Overhaul Living/Sentient Armor adaptation, making it more performant and fixing some issues.\n" +
            "Fixes that are included without a toggle: \n" +
            "- When combining living+sentient gear, will use the point multiplier of each armor piece instead of using the last checked one.")
    @Config.Name("Overhaul Adaptation")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.adaptationoverhaul.json")
    public boolean overhaulAdaptation = true;

    @Config.Comment("Tweaks the adaption fire tick window to be set by any damage while burning instead of only fire tick damage. Also makes Fire Resistance make isBurning return false for adaptable parasites")
    @Config.Name("Burning State Sets Fire Tick Window")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.adaptburningfirewindowanydamage.json")
    public boolean anyDamageWhenBurningFailWindow = true;

    @Config.Comment("If adapting during an attack with no immediate attacker entity, SRP adapts to \"\". This fixes that bug. Requires \"Overhaul Adaptation\".")
    @Config.Name("Fix Null Adaptation")
    public boolean fixNullAdaptation = true;

    @Config.Comment("If SRP doesn't find a blacklisted damage type for a mob/player in BlackList Mobs, it will also search the Blacklist Else list. This fixes that bug. Requires \"Overhaul Adaptation\".")
    @Config.Name("Fix Blacklist Check")
    public boolean fixBlacklistCheck = true;

    @Config.Comment("SRPConfig has a list \"Adaptation Bonus\" which isn't read properly and will crash if filled with entries. This fixes it.")
    @Config.Name("Fix Adaptation Bonus Config")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.adaptationbonusfix.json")
    public boolean fixAdaptationBonusList = true;

    @Config.Comment("Adaptable parasites will adapt to the players mainhand weapon when hit by indirect dmgs (harming splash potions, arrows, modded indirect dmg sources). Enable this to make them adapt to the indirect dmg source instead (so magic, arrow etc). Moved from RLMixins (thanks Kotlin!)")
    @Config.Name("Fix Adaptation to Indirect Damages")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.adapttoindirect.json")
    public boolean fixAdaptationToIndirect = true;
}
