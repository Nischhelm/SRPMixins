package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class CothConfig {
    @Config.Comment("Makes mobs getting converted to their assimilated version respect coth immunity")
    @Config.Name("Stop assimilating COTH immune mobs")
    public boolean stopCothImmuneAssim = true;

    @Config.Comment("Makes mobs getting converted to their feral version respect coth immunity")
    @Config.Name("Stop feralizing COTH immune mobs")
    public boolean stopCothImmuneFeral = true;

    @Config.Comment("Fixes the srpcothimmunity tag (basically counting coth lvls) getting incremented for coth immune mobs, making them not immune anymore")
    @Config.Name("Fix srpcothimmunity tag")
    public boolean fixSrpCothImmunity = true;
}
