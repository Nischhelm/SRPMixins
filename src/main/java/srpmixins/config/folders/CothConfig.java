package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class CothConfig {
    @Config.Comment("Fixes the srpcothimmunity tag (basically counting coth lvls) getting incremented for coth immune mobs, making them not immune anymore")
    @Config.Name("Fix srpcothimmunity tag")
    @Config.RequiresMcRestart
    public boolean fixSrpCothImmunity = true;
}
