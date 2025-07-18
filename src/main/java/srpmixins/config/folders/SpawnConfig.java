package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class SpawnConfig {
    @Config.Comment("Parasites can't spawn from spawners in base SRP. This fixes it. Reintroduced after moving it to RLMixins")
    @Config.Name("Fix Spawner Block spawning")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.spawnerspawnfix.json", defaultValue = true)
    public boolean fixSpawnerSpawns = true;

    @Config.Comment("Blacklist of biomes and dimensions in which no parasites will spawn. \n" +
            "This works only for phases on + custom spawner on, except if \"Fix Spawning Entirely\" is enabled, in which case it always works.\n" +
            "Pattern: dimensionId, modid:biomename \n" +
            "Disable full mods with dimid, modid \n" +
            "Disable full dimensions with dimid, no biomes for that dimension in any line\n" +
            "For example: \n" +
            "0, minecraft:mutated_forest\n" +
            "3, biomesoplenty\n" +
            "-1")
    @Config.Name("Parasite Spawning Biome Blacklist per dimension")
    public String[] biomeBlacklist = {};

    @Config.Comment("Use Biome Blacklist as Whitelist")
    @Config.Name("Parasite Spawning Biome Blacklist per dimension is whitelist")
    public boolean biomeBlacklistIsWhitelist = false;

    @Config.Comment("SRP disables certain parasites until a colony is created. This also happens though if colonies aren't even activated, fully locking away certain parasites (preeminents by default). This makes SRP ignore the colony lock if colonies are disabled.\n" +
            "WARNING: SRP has preeminents in the default phase spawning lists for phase 3 and beyond. If you enable this fix, you will need to change your SRP defaults. Preeminents should only spawn in parasite biomes and/or late phases like 9/10.")
    @Config.Name("Fix Colony Lock - requires SRP config change")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.cololockfix.json", defaultValue = false)
    //TODO: maybe just delete that
    public boolean fixColonyLock = false;

    @Config.Comment("SRP allows to disable certain parasites until a certain phase in a certain dimension is reached. This also happens though if phases aren't even activated, fully locking away certain parasites. This makes SRP ignore the evolution lock if evolution is disabled.")
    @Config.Name("Fix Evolution Lock")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.evolockfix.json", defaultValue = true)
    public boolean fixEvolutionLock = true;

    @Config.Comment("If custom phase spawner is disabled, SRP checks for min and max type id of parasites per phase to spawn them. Different than with the custom spawner though, it also checks those ids for parasite biome spawns, which can make parasite biomes feel very empty. This makes the parasite biome ignore the min and max id and instead just spawn its config-set parasites no matter the phase.")
    @Config.Name("Fix Parasite Biome Spawns")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.parabiomespawns.json", defaultValue = true)
    public boolean fixBiomeSpawnsNoCustom = true;

    @Config.Comment("Colony Carrier has type id 31, making it equal to primitive mobs for phases enabled but custom phase spawner disabled. This will fix it to have a type id of 63, making it equal to other ground preeminents, making it spawn in phases 8-10 by default.")
    @Config.Name("Fix Colony Carrier Type Id")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.colocarriertypefix.json", defaultValue = true)
    public boolean fixColonyCarrierTypeId = true;

    @Config.Comment("Auto fill the conversion rules with existing conversion pathways when they happen. Will be updated on logout.")
    @Config.Name("Conversion Phase Lock Rules - Auto fill")
    public boolean autoFillConversionRules = false;

    @Config.Comment("A lot of parasites evolve into each other. Use this list to stop specific evolution pathways until a specific phase is reached. Pattern: paraIn, paraOut, minPhase")
    @Config.Name("Conversion Phase Lock Rules")
    public String[] conversionRules = {};
    
    @Config.Comment("In situations where parasite spawns should ignore light levels, they are still prevented by blocklight (torches and other light sources).\n" +
            "These situations are:\n" +
            "\t- when the phase is above the \"Phase Parasites Ignore Sunlight\"\n" +
            "\t- when inside a parasite biome\n" +
            "\t- or if \"Mobs Ignore Sun\" option is true (and evo phases off)\n" +
            "This config varies that behavior:\n" +
            "--- Parasites will only spawn if the blocklight is lower or equal to the given value. ---\n" +
            "Examples: Set to 16 to make light sources not affect spawning at all.\n" +
            "Or set to 0 to prevent any spawning if there is any blocklight on a block.\n" +
            "Default value of 7 is how SRP handles it.\n" +
            "NOTE: this config replaces the old RLMixins \"Parasite Light Level (SRParasites)\" mixin toggle (enabled = set to 16)")
    @Config.Name("Min Blocklight Threshold")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 0, max = 16)
    public int blockLightThresholdTwo = 7;

    @Config.Comment("Fully overhauls the SRP spawning system.\n" +
            "SRP spawns parasites just to cancel the spawns right afterwards in various situations.\n" +
            "This slows down hostile mob spawning and takes up performance for no reason, even in dimensions where parasites aren't even allowed to spawn.\n" +
            "It also does mob cap counts for every single spawn instead of doing what vanilla does and doing a mob cap check for every spawn attempt (which can include multiple spawn packs).\n" +
            "It's also just very hard to read and includes various oversights due to being so spread out throughout the SRP code.\n" +
            "This fix puts the whole spawning logic into one place, auto fixes some bugs and fixes the aforementioned issues.\n" +
            "Automatically includes the following fixes (and probably others i haven't even spotted):\n" +
            "\t- \"Fix Parasite Biome Spawns\"\n" +
            "\t- \"Fix Colony Lock in Para Biome\"\n" +
            "\t- Auto fixes Quark Soul Beads being effectively incompat with SRP\n" +
            "Nerd info: This is done by adding a whole new spawning group PARASITE on top of the existing HOSTILE, PASSIVE, AMBIENT and WATER_CREATURE spawning groups, \n" +
            "and modifying the potentialSpawnEvent instead of the checkSpawnEvent."
    )
    @Config.Name("Fix Spawning Entirely")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(earlyMixin = "mixins.srpmixins.vanilla.enumcreaturetype.json", lateMixin = "mixins.srpmixins.srp.fixspawning.json", defaultValue = true)
    public boolean fixSpawningEntirely = true;

    @Config.Comment("Requires \"Fix Spawning Entirely\"\n" +
            "Parasite Mob Cap multipliers per phase and dimension (and bloodmoon). \n" +
            "Format: [dim = xxx] [phase><= xxx] [bloodmoon = true/false] mobCapMulti\n" +
            "Where all [] entries are optional and are handled with an AND connection if multiple are present.\n" +
            "For phase the operator options are =, <, >, <= and >= \n" +
            "To define a specific area of phases, you can also use the \"phase\" keyword twice\n" +
            "All rules that fit to the current state will be applied as a multiplicator on the current mobCap\n" +
            "If phases are disabled, rules using phases will be ignored. The same happens if bloodmoon is disabled and a rule includes bloodmoons.")
    @Config.Name("Parasite Mob Cap Rules")
    public String[] mobCapRules = {
            "[dim = 0] [bloodmoon = true] 2",
            "[dim = 111] [bloodmoon = true] 4",
            "[phase < 0] 0",
            "[phase >= 0] [phase <= 10] 1"
    };

    @Config.Comment("If SRParasitesWorld.cfg \"Colony Parasite Values Biome\" is enabled, spawns in Parasite Biome would NEVER allow colony-locked parasites (default: preeminents) no matter how many colonies have been established in the world.\n" +
            "This fix makes it then use the actual colony point requirements set in SRParasitesWorld.cfg \"Colony Parasite Values\" instead.")
    @Config.Name("Fix Colony Lock in Para Biome")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.colofixinbiome.json", defaultValue = true)
    public boolean fixColoLockInParaBiome = true;
}
