package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class PlayerPhaseConfig {
    @Config.Comment("Do Phase+Point functionalities per player, allowing better Multiplayer")
    @Config.Name("Use Player Phases")
    @Config.RequiresMcRestart
    public boolean enabled = true;

    @Config.Comment("Send logs when methods try to find a player to do player phase stuff with and not finding one")
    @Config.Name("Player Phases debug mode")
    public boolean playerPhaseDebugMode = false;
}
