package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class VariousConfig {
    @Config.Comment("Disables the automatic debug log spam for Scent Entities")
    @Config.Name("Disable Scent Debug")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.disablescentdebug.json", defaultValue = true)
    public boolean disableScentDebug = true;

    @Config.Comment("Make SRP Blacklists/Whitelists use wildcards to dis/enable whole mods (*). WARNING: this forces you to change all current SRP config lists that use full mod names without wildcards")
    @Config.Name("SRP Blacklists are Wildcard-able")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.wildcardablelists.json", defaultValue = false)
    //TODO: prob just delete
    public boolean blacklistsWildcardable = false;

    @Config.Comment("SRP parses its config list every single time instead of caching the result. For performance this stores the results instead.")
    @Config.Name("Fix Config List Parsing")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.configlistfix.json", defaultValue = true)
    public boolean fixConfigListParse = true;

    @Config.Comment("SRP has a bunch of configs that are not used. This toggle makes them all do what they are supposed to do. List of affected configs:\n" +
            "- Default Phase, Points, CanGain, Can(t)Lose\n" +
            "- Most Phase 9+10 configs (cooldown, killcount plus, scent bonus+reaction, beckon grow penalties, crop grow stunned, mobs spawning with COTH, beckon spawn from residue\n" +
            "- Spawning Rates of certain mobs making them not spawn at all with phases disabled (Monarch, Feral Bear, Light Carrier, Thrall)\n" +
            "- Merging flesh not spawning primitives with reduced health defined via merge config\n" +
            "- Mob Health/Dmg/Armor/KBres multipliers (Gnat, Hijacked Golem, Light Carrier, Worker, Prim Vermin, Feral Bear)\n" +
            "- XP value of sentries and kyphosis not using the deterrent XP value\n" +
            "- Preeminents incorrectly using the Pure value for remain value\n" +
            "- Points over time dimension blacklist\n" +
            "- Min phase for Beckons ignoring summoning cooldown")
    @Config.Name("Use Forgotten Configs")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.forgottenconfigs.json", defaultValue = true)
    public boolean useForgottenConfigs = true;

    @Config.Comment("Adaptable mobs can steal food from the players inventory via attacks or scary orbs. Use this blacklist to disable them from stealing certain foods. Pattern: modid:itemname, optional metadata")
    @Config.Name("Food Steal Item Blacklist")
    public String[] foodBlacklist = {};

    @Config.Comment("Set to true to make the food steal item blacklist a whitelist instead")
    @Config.Name("Food Steal Item Blacklist is Whitelist")
    public boolean foodBlacklistIsWhitelist = false;

    @Config.Comment("Parasites regularly break blocks. Use this list to blacklist some blocks for certain parasites or parasite groups.\n" +
            "Special group names: PRIMITIVE, ADAPTED, PURE, PREEMINENT, ANCIENT, NEXUS, DETERRENT, ASSIMILATED, FERAL, INBORN, HIJACKED, CRUDE\n" +
            "For specific parasites use their ingame mobid so for example ada_longarms\n" +
            "Expected pattern: modid:blockname, list of parasite mobids and parasite groups separated by comma")
    @Config.Name("Block Break Blacklist")
    public String[] blockBreakBlacklist = {};

    @Config.Comment("All crafting ingredient parasite drops stack to 16 except for strange bones. This makes them also stack to 16. Reintroduced after moving it to RLMixins")
    @Config.Name("Make Strange Bones stack to 16")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.strangebonestacking.json", defaultValue = true)
    public boolean strangeBoneStacking = true;

    @Config.Comment("SRP Commands don't have proper autocompletion. This fixes it. Also fixes the /help command for them.")
    @Config.Name("Fix SRP Command Autocompletion")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.commandfix.json", defaultValue = true)
    public boolean fixCommandOverrides = true;

    @Config.Comment("SRP always uses the same method to treat its various config blacklists. In the case of an empty whitelist (list empty & treatAsWhitelist = true) the list is incorrectly treated as an empty BLACKlist, so instead of \"deny all\" its treated as \"allow all\". This fixes it.")
    @Config.Name("Fix Empty Whitelists read as Blacklists")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.emptywhitelistfix.json", defaultValue = true)
    public boolean fixEmptyWhitelist = true;

    @Config.Comment("Some parasites have broken right click handling. When looking at them, all right click actions such as raising shields and drinking potions will get cancelled. This fixes it.")
    @Config.Name("Fix Right Clicking Parasite Entities")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.parasiterightclickfix.json", defaultValue = true)
    public boolean fixParasiteMobRightClick = true;

    @Config.Comment("Waves and Shockwaves are normal Parasite Entities and thus get lots of normal parasite behavior, for example the ability to target any mob once the phase is above phase total slaughter. This makes the wave instantly die though. This fixes it.")
    @Config.Name("Fix Wave Entities Early Death")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.wavefix.json", defaultValue = true)
    public boolean fixWaveRetargeting = true;
    
    @Config.Comment("Disables non-SRP armor models from rendering in SRPLayerBipedArmor to avoid crashes")
    @Config.Name("SRPModelBiped Render Crash Fix")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.armorcrash.json", defaultValue = true)
    public boolean srpModelBipedRenderCrashFix = true;

    @Config.Comment("Will make scent entities unable to push players and other entities.")
    @Config.Name("Scents NoClip")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.scentsnoclip.json", defaultValue = true)
    public boolean scentsDontClip = true;

    @Config.Comment("Max distance to a node of stage 1,2,3 to be found by the node compass. Will be disabled if its not exactly three entries, so clear the list if you want to disable it.")
    @Config.Name("Node Compass max distance")
    public int[] nodeCompassMaxDist = {
            Integer.MAX_VALUE,
            Integer.MAX_VALUE,
            Integer.MAX_VALUE
    };
}
