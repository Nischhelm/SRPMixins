package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class VariousConfig {
    @Config.Comment("Disables the automatic debug log spam for Scent Entities")
    @Config.Name("Disable Scent Debug")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.disablescentdebug.json")
    public boolean disableScentDebug = true;

    @Config.Comment("Make SRP Blacklists/Whitelists use wildcards to dis/enable whole mods (*). WARNING: this forces you to change all current SRP config lists that use full mod names without wildcards")
    @Config.Name("SRP Blacklists are Wildcard-able")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.wildcardablelists.json")
    public boolean blacklistsWildcardable = false;

    @Config.Comment("SRParasites.cfg has two options for para biome spreading speed (cooldown+block limit), but those don't get applied. Set to true to fix that")
    @Config.Name("Fix Parasitic Biome spreading limit")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.biomespreadlimit.json")
    public boolean fixBiomeSpreadingLimit = true;

    @Config.Comment("SRP parses its config list every single time instead of caching the result. For performance this stores the results instead.")
    @Config.Name("Fix Config List Parsing")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.configlistfix.json")
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
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.forgottenconfigs.json")
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
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.strangebonestacking.json")
    public boolean strangeBoneStacking = true;

    @Config.Comment("Parasite Bush and Vines will force load chunks when a parasite biome is growing. This stops the force loading. Moved from RLMixins (thanks fonny!)")
    @Config.Name("Fix Parasite Bush Generation Lag")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.parasitebushgen.json")
    public boolean fixBiomeBushLag = true;

    @Config.Comment("SRP Commands don't have proper autocompletion. This fixes it. Also fixes the /help command for them.")
    @Config.Name("Fix SRP Command Autocompletion")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.commandfix.json")
    public boolean fixCommandOverrides = true;

    @Config.Comment("SRP sends a new network packet for every individual block position that gets turned into parasitic biome or back to plains. This fix sends one bigger packet instead, for performance.")
    @Config.Name("Fix SRP Biome Packet")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.biomepacket.json")
    public boolean fixBiomePacket = true;

    @Config.Comment("SRP always uses the same method to treat its various config blacklists. In the case of an empty whitelist (list empty & treatAsWhitelist = true) the list is incorrectly treated as an empty BLACKlist, so instead of \"deny all\" its treated as \"allow all\". This fixes it.")
    @Config.Name("Fix Empty Whitelists read as Blacklists")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.emptywhitelistfix.json")
    public boolean fixEmptyWhitelist = true;
}
