package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class WaterParaConfig {

    @Config.Comment("Make Primitive Devourer and Assimilated Squid spawn")
    @Config.Name("Enable Water Spawns")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.waterspawns.json")
    public boolean enableWaterSpawns = true;

    @Config.Comment("Make Water Parasites (Assim Squid + Prim Devourer) target Squids")
    @Config.Name("Water Parasites Target Squids")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.squidtargeting.json")
    public boolean parasTargetSquids = true;
}
