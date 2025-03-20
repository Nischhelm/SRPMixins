package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class VariousConfig {
    @Config.Comment("Disables the automatic debug log spam for Scent Entities")
    @Config.Name("Disable Scent Debug")
    @Config.RequiresMcRestart
    public boolean disableScentDebug = true;

    @Config.Comment("Blacklist of biomes and dimensions in which no parasites will spawn. Pattern: dimension id, biome registry name. Disable full mods by dimid, modid. Disable full dimensions by only naming dimid, no biomes for that dimension in any line")
    @Config.Name("Parasite Spawning Biome Blacklist per dimension")
    public String[] biomeBlacklist = {
            "0, minecraft:mutated_forest",
            "3, otg",
            "271"
    };

    @Config.Comment("Use Biome Blacklist as Whitelist")
    @Config.Name("Parasite Spawning Biome Blacklist per dimension is whitelist")
    public boolean biomeBlacklistIsWhitelist = false;

    @Config.Comment("Make SRP Blacklists/Whitelists use wildcards to dis/enable whole mods (*). WARNING: this forces you to change all current SRP config lists that use full mod names without wildcards")
    @Config.Name("SRP Blacklists are Wildcard-able")
    @Config.RequiresMcRestart
    public boolean blacklistsWildcardable = false;

    @Config.Comment("SRParasites.cfg has two options for para biome spreading speed (cooldown+block limit), but those don't get applied. Set to true to fix that")
    @Config.Name("Fix Parasitic Biome spreading limit")
    @Config.RequiresMcRestart
    public boolean fixBiomeSpreadingLimit = true;

    @Config.Comment("SRP soft crashes whenever Needler tries to apply dmg to players. This is fixed if \"Needler Fix\" is enabled. Enable this config to finally make players suffer the fixed Needler effect.")
    @Config.Name("Needler Fix - Allow on Players")
    public boolean allowPlayerNeedler = false;

    @Config.Comment("SRP always incorrectly applies max dmg on mobs getting Needler, no matter the potion effect lvl. It also never applies it on players. This fixes both.")
    @Config.Name("Needler Fix")
    @Config.RequiresMcRestart
    public boolean fixNeedler = true;

    @Config.Comment("SRP provides a configable maximum dmg for Needler applied on players, but the base percentage of max health which Needler does as dmg is the same for mobs and players. Use this value to customise this for players. Use any negative value to copy from SRPSystems value \"Needler Damage\" (default 0.4=40%)\n" +
            "Warning: Needler uses a fully custom damaging system via setHealth, which will ignore all other mods attempting to reduce/ignore dmg or cancel attacks/deaths. Only totems will protect players here.")
    @Config.Name("Needler Fix - Dmg Multi for Players")
    public float playerNeedlerMulti = 0.4F;

    @Config.Comment("Potions should always be applied serverside, otherwise there can be desyncs. This fixes a few spots where SRP applies potions on clientside")
    @Config.Name("Fix clientside potions")
    @Config.RequiresMcRestart
    public boolean fixClientPotions = true;

    @Config.Comment("SRP Potions Rage and Heightened Senses use random UUIDs, making them stack on every restart. This fixes it.")
    @Config.Name("Fix attribute potions")
    @Config.RequiresMcRestart
    public boolean fixPotionUUIDs = true;

    @Config.Comment("SRP parses its config list every single time instead of caching the result. For performance this stores the results instead.")
    @Config.Name("Fix Config List Parsing")
    @Config.RequiresMcRestart
    public boolean fixConfigListParse = true;
}
