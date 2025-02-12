package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class VariousConfig {
    @Config.Comment("Disables the automatic debug log spam for Scent Entities")
    @Config.Name("Disable Scent Debug")
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
    public boolean blacklistsWildcardable = false;

    @Config.Comment("SRParasites.cfg has two options for para biome spreading speed (cooldown+block limit), but those don't get applied. Set to true to fix that")
    @Config.Name("Fix Parasitic Biome spreading limit")
    public boolean fixBiomeSpreadingLimit = true;
}
