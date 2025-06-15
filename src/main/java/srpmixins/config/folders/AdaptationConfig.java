package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class AdaptationConfig {
    @Config.Comment("Overhaul Living/Sentient Armor adaptation, making it more performant and fixing some issues")
    @Config.Name("Overhaul Adaptation")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.adaptationoverhaul.json")
    public boolean overhaulAdaptation = true;

    @Config.Comment("In SRP, adaptable Parasites will have a chance to fail adapting to a damage type if they got hit by inFire or onFire dmg maximum 10 ticks (half a second) before the current hit. \n" +
            "This means you would have to hit them during the iframe the fire tick creates to make them fail the adaptation. \n" +
            "Enable this fix to instead make them have a chance to fail adaptation whenever they are burning (and not having fire resistance).\n" +
                    "Warning: this makes any burn inflicting method to deal with parasites about twice as useful against their adaptation")
    @Config.Name("Fix Adaptation While Burning")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.adaptwhileburningfix.json")
    public boolean fixAdaptationWhileBurning = true;

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

    @Config.Comment("Wearing Living or Sentient Armor is supposed to apply the SRP config \"Mob Fire Damage Multiplier\" to all fire dmgs that the wearing player takes. This didn't work in base SRP due to the same bug that makes armor adapt to \"\". Fixing the latter also fixed the former, making players wearing living/sentient armor take huge amounts of fire dmg. Keeping this disabled keeps the vanilla (unintended) SRP behavior of not increasing indirect fire dmg on players. Requires \"Overhaul Adaptation\". Enable this if you want to make people wearing living/sentient armor fear fire.")
    @Config.Name("Apply Fire Dmg Multi")
    public boolean fixFireDmgOnSentient = false;
}
