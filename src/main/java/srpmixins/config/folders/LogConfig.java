package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class LogConfig {

    @Config.Comment("Enable detailed logging for dispatcher storage.")
    @Config.Name("Enable Dispatcher Store Logging")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.logscent.dispatcher.json", defaultValue = false)
    public boolean dispatcherEnabled = false;

    @Config.Comment("Enable detailed logging/telemetry for SRP scent entities.")
    @Config.Name("Enable Scent Logging")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.logscent.json", defaultValue = false)
    public boolean scentEnabled = false;

    @Config.Comment("How often to emit per-scent tick snapshots, in ticks. Higher values reduce spam. Typical: 20 (1s)")
    @Config.Name("Scent Log Tick Interval")
    public int tickInterval = 20;
}
