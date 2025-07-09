package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class PlayerPhaseConfig {
    @Config.Comment("Do Phase+Point functionalities per player, allowing better Multiplayer")
    @Config.Name("Use Player Phases")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.customphasemechanics.json", defaultValue = false)
    public boolean enabled = false;

    @Config.Comment("Send logs when methods try to find a player to do player phase stuff with and not finding one")
    @Config.Name("Player Phases debug mode")
    public boolean playerPhaseDebugMode = false;
}
