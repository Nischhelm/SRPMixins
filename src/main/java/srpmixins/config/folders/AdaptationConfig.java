package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class AdaptationConfig {
    @Config.Comment("Overhaul Living/Sentient Armor adaptation, making it more performant and fixing some issues.\n" +
            "Fixes that are included without a toggle: \n" +
            "- When combining living+sentient gear, will use the point multiplier of each armor piece instead of using the last checked one.")
    @Config.Name("Overhaul Adaptation")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(earlyMixin = "mixins.srpmixins.vanilla.adaptationoverhaul.json", lateMixin = "mixins.srpmixins.srp.adaptationoverhaul.json", defaultValue = true)
    public boolean overhaulAdaptation = true;

    @Config.Comment("In SRP, adaptable Parasites will have a chance to fail adapting to a damage type if they got hit by inFire or onFire dmg maximum 10 ticks (half a second) before the current hit. \n" +
            "This means you would have to hit them during the iframe the fire tick creates to make them fail the adaptation. \n" +
            "Enable this fix to instead make them have a chance to fail adaptation whenever they are burning (and not having fire resistance).\n" +
            "Warning: this makes any burn inflicting method to deal with parasites about twice as useful against their adaptation")
    @Config.Name("Fix Adaptation While Burning")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.adaptwhileburningfix.json", defaultValue = true)
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
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.adaptationbonusfix.json", defaultValue = true)
    public boolean fixAdaptationBonusList = true;

    @Config.Comment("Adaptable parasites will adapt to the players mainhand weapon when hit by indirect dmgs (harming splash potions, arrows, modded indirect dmg sources). Enable this to make them adapt to the indirect dmg source instead (so magic, arrow etc). Moved from RLMixins (thanks Kotlin!)")
    @Config.Name("Fix Adaptation to Indirect Damages")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.adapttoindirect.json", defaultValue = true)
    public boolean fixAdaptationToIndirect = true;

    @Config.Comment("Wearing Living or Sentient Armor is supposed to apply the SRP config \"Mob Fire Damage Multiplier\" to any fire (inFire/onFire) dmg the player takes, as well as any dmg when the player is burning (isBurning). In those cases, adapting to the dmg is also supposed to fail. Fire dmg doing that didn't work in base SRP due to a bug. \n" +
            "Use this list to modify how it works when \"Overhaul Adaptation\" is enabled. \n" +
            "Originally intended would be inFire, onFire, isBurning, actual SRP behavior is just isBurning. \n" +
            "Possible additions would be lava, hotFloor and fireworks, or just fully disabling the feature by clearing the list.")
    @Config.Name("Fire Multiplier Dmg Types")
    public String[] fireMultiDmgTypes = {
            "isBurning"
    };

    @Config.Comment("SRPMixins adds a recipe to reset adaptation on Living/Sentient Armor. This needs you to surround the armor with four of the named item. To disable the recipe, clear this config and restart the game.\n" +
            "For CraftTweaker users there is also a method to use for custom recipes, using srpmixins.StackHelper.removeAdaptation(IItemStack stack);")
    @Config.Name("Adaptation Reset Item")
    public String adaptationResetItem = "contenttweaker:blood_tear";

    @Config.Comment("Optional item in the corner spots of the crafting table for the adaptation reset recipe. Keep empty for air/empty")
    @Config.Name("Adaptation Second Reset Item")
    public String adaptationResetItemTwo = "";
}
