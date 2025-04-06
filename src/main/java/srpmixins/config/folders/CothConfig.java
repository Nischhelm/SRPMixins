package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class CothConfig {
    @Config.Comment("Fixes the srpcothimmunity tag (basically counting coth lvls) getting incremented for coth immune mobs, making them not immune anymore")
    @Config.Name("Fix srpcothimmunity tag")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.cothimmunityfix.json")
    public boolean fixSrpCothImmunity = true;

    @Config.Comment("If setting SRP config min assimilations required to spawn for a specific parasite, setting it to 0 will still not auto allow that spawn. This fixes it.")
    @Config.Name("Fix Min Assimilations Zero")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.minassimilations.json")
    public boolean fixMinAssimilations = true;

    @Config.Comment("SRP uses the min assimilation values of the corresponding assimilated mob to allow feral mobs to spawn naturally. Use this list to override that with custom values. Remove lines to use default assimilated values. Pattern: para id, min feralisations\n" +
            "93 Feral Cow\n" +
            "94 Feral Enderman\n" +
            "95 Feral Horse\n" +
            "96 Feral Human\n" +
            "97 Feral Pig\n" +
            "98 Feral Sheep\n" +
            "99 Feral Villager\n" +
            "300 Feral Wolf\n" +
            "306 Feral Bear")
    @Config.Name("Min Feralisations")
    public String[] minFeralisations = {
            "93, 4",
            "94, 9",
            "95, 3",
            "96, 5",
            "97, 4",
            "98, 3",
            "99, 6",
            "300, 2",
            "306, 2"
    };
}
