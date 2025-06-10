package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class SpawnConfig {
    @Config.Comment("Parasites can't spawn from spawners in base SRP. This fixes it. Reintroduced after moving it to RLMixins")
    @Config.Name("Fix Spawner Block spawning")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.spawnerspawnfix.json")
    public boolean fixSpawnerSpawns = true;

    @Config.Comment("Blacklist of biomes and dimensions in which no parasites will spawn. \n" +
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
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.cololockfix.json")
    public boolean fixColonyLock = false;

    @Config.Comment("SRP allows to disable certain parasites until a certain phase in a certain dimension is reached. This also happens though if phases aren't even activated, fully locking away certain parasites. This makes SRP ignore the evolution lock if evolution is disabled.")
    @Config.Name("Fix Evolution Lock")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.evolockfix.json")
    public boolean fixEvolutionLock = true;

    @Config.Comment("If custom phase spawner is disabled, SRP checks for min and max type id of parasites per phase to spawn them. Different than with the custom spawner though, it also checks those ids for parasite biome spawns, which can make parasite biomes feel very empty. This makes the parasite biome ignore the min and max id and instead just spawn its config-set parasites no matter the phase.")
    @Config.Name("Fix Parasite Biome Spawns")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.parabiomespawns.json")
    public boolean fixBiomeSpawnsNoCustom = true;

    @Config.Comment("Colony Carrier has type id 31, making it equal to primitive mobs for phases enabled but custom phase spawner disabled. This will fix it to have a type id of 63, making it equal to other ground preeminents, making it spawn in phases 8-10 by default.")
    @Config.Name("Fix Colony Carrier Type Id")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.colocarriertypefix.json")
    public boolean fixColonyCarrierTypeId = true;

    @Config.Comment("Auto fill the conversion rules with existing conversion pathways when they happen. Will be updated on logout.")
    @Config.Name("Conversion Phase Lock Rules - Auto fill")
    public boolean autoFillConversionRules = false;

    @Config.Comment("A lot of parasites evolve into each other. Use this list to stop specific evolution pathways until a specific phase is reached. Pattern: paraIn, paraOut, minPhase")
    @Config.Name("Conversion Phase Lock Rules")
    public String[] conversionRules = {};
}
