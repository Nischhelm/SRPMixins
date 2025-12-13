package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class SourceDimConfig {
    @Config.Comment("Global toggle for the source dimension (id 254) WIP")
    @Config.Name("Enable Source Dimension")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.sourcedim.json", defaultValue = false)
    public boolean isEnabled = false;
}
