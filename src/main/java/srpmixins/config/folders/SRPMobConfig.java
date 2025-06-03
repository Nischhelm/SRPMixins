package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class SRPMobConfig {
    @Config.Comment("SRP uses a very long config for its mobs. Enabling this will store the most common entries in a list here instead and read from there for easier access.\n" +
            "NOTE: You need to restart the game after first enable to have the ingame list be filled.")
    @Config.Name("Enable Mob Configs")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.mobconfigs.json")
    public boolean enableMobConfig = false;

    @Config.Comment("List for SRP Mob configs, gathered from SRParasitesMobs.cfg for easier and ingame access. \n" +
            "use true/false for enabled\n" +
            "spawnWeight is only used if evolution custom spawner is disabled\n" +
            "some values aren't used, keep those on \"---\"\n" +
            "changing spawnWeight or enabled requires restart to work\n" +
            "Pattern: enabled healthMulti dmgMulti armorMulti knockbackResistanceMulti spawnWeight paraname")
    @Config.Name("SRP Mob Config")
    public String[] mobConfig = {};
}