package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class ChunkPhaseConfig {
    @Config.Comment("Do Evolution mechanic by chunk. World areas that are inhabited longer will have higher phases. Can't be used together with player phases.")
    @Config.Name("Use Chunk Phases")
    @Config.RequiresMcRestart
    public boolean enabled = false;

    @Config.Comment("If using chunk phases, how many regions around the current region should get updated when points or lure cooldown of a chunk change? It's a radius, so it will update a square of (2 x radius + 1)² regions. Default 3, so 7x7 regions")
    @Config.Name("Chunk Phases: Region update radius")
    @Config.RangeInt(min = 0)
    public int chunkPhasesRegionRadius = 3;

    @Config.Comment("If using chunk phases, set the starting phase per biome id. All unset biomes will use the dimension default set in SRPSystems.cfg. Pattern: biomeId, startPhase")
    @Config.Name("Chunk Phases: Custom Biome Start Phases")
    public String[] chunkPhasesBiomeStartPhases = {
            "minecraft:mutated_forest, -2"
    };

    @Config.Comment("If using chunk phases, use regular dimension-wide phases for these dimensions to save performance")
    @Config.Name("Chunk Phases: Dimension blacklist")
    public int[] chunkPhasesDimensionBlacklist = {
            -1,
            1,
            111
    };

    @Config.Comment("If using chunk phases, turn the dimension blacklist for chunk phases into a whitelist")
    @Config.Name("Chunk Phases: Dimension blacklist is whitelist")
    public boolean chunkPhasesDimensionBlacklistIsWhitelist = false;

    @Config.Comment("If using chunk phases, a chunk region that gets points will be a square of this many chunks squared. WARNING: never change this in an existing world")
    @Config.Name("Chunk Phases: Chunk spacing")
    @Config.RangeInt(min = 1)
    public int chunkPhasesSpacing = 4;
}
