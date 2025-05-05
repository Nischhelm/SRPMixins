package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class SRPMobConfig {
    @Config.Comment("List for SRP Mob configs, gathered from SRParasitesMobs.cfg for easier and ingame access. Pattern: paraname; enabled; healthMulti; dmgMulti; armorMulti; knockbackResistanceMulti; spawnWeight; [loot]\n" +
            "use true/false for enabled\n" +
            "spawnWeight is only used if evolution custom spawner is disabled")
    @Config.Name("SRP Mob Config")
    public String[] mobConfig = {};
}
