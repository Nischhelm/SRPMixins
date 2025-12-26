package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class LogConfig {

    @Config.Comment("Enable detailed logging for dispatcher storage.")
    @Config.Name("Enable Dispatcher Store Logging")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.logdispatcher.json", defaultValue = false)
    public boolean dispatcherEnabled = false;
}
