package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class ChunkPhaseConfig {
    @Config.Comment("Do Evolution mechanic by chunk. World areas that are inhabited longer will have higher phases. Can't be used together with player phases.")
    @Config.Name("Use Chunk Phases")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.customphasemechanics.json")
    public boolean enabled = false;

    @Config.Comment("If using chunk phases, how many regions around the current region should get updated when points or lure cooldown of a chunk change? It's a radius, so it will update a square of (2 x radius + 1)² regions. Default 3, so 7x7 regions")
    @Config.Name("Region update radius")
    @Config.RangeInt(min = 0)
    public int regionUpdateRadius = 2;

    @Config.Comment("If using chunk phases, set the starting phase per biome id. All unset biomes will use the dimension default set in SRPSystems.cfg. Pattern: biomeId, startPhase")
    @Config.Name("Custom Biome Start Phases")
    public String[] biomeStartPhases = {
            "minecraft:mutated_forest, -2"
    };

    @Config.Comment("If using chunk phases, use regular dimension-wide phases for these dimensions to save performance")
    @Config.Name("Dimension blacklist")
    public int[] dimensionBlacklist = {
            -1,
            1,
            111
    };

    @Config.Comment("If using chunk phases, turn the dimension blacklist for chunk phases into a whitelist")
    @Config.Name("Dimension blacklist is whitelist")
    public boolean dimensionBlacklistIsWhitelist = false;

    @Config.Comment("If using chunk phases, a chunk region that gets points will be a square of this many chunks squared.\n" +
            "WARNING: never change this in an existing world, or you will lose your evolution progress")
    @Config.Name("Chunk spacing")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1)
    public int chunkSpacing = 3;
}
