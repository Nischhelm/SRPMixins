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

    @Config.Comment("Worlds saved using player phases in SRPMixins 2.8.5 and earlier have their phase+point data accidentally spread out over multiple data files (one per dimension).\n" +
            "This patch loads old data and puts it into the global file in /data.\n" +
            "You can disable this patch to save a minimal amount of performance if you don't have worlds that were saved in SRPMixins 2.8.5 or older using player phases.\n" +
            "In an existing world it's enough to enter all dimensions once to have the patch be fully applied and allowing to disable this patch.")
    @Config.Name("Player Phases Legacy Patch")
    public boolean playerPhaseLegacyPatch = true;

    @Config.Comment("Allows to define dimension and targeted player name in all /srpevolution subcommands. Usage: /srpevolution xyz [optional:dimid] [optional:playername]")
    @Config.Name("Modify srpevolution Command")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.extendedevocommand.json", defaultValue = true)
    public boolean extendedEvoCommand = true;
}
