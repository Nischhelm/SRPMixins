package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class CothConfig {

    @Config.Comment("SRP uses the min assimilation values of the corresponding assimilated mob to allow feral mobs to spawn naturally. Use this list to override that with custom values. Remove lines to use default assimilated values. Pattern: mob name, min feralisations")
    @Config.Name("Min Feralisations")
    public String[] minFeralisations = {
            "fer_cow, 4",
            "fer_enderman, 9",
            "fer_horse, 3",
            "fer_human, 5",
            "fer_pig, 4",
            "fer_sheep, 3",
            "fer_villager, 6",
            "fer_wolf, 2",
            "fer_bear, 2"
    };

    @Config.Comment("SRP has the min assimilation value for big spider be hardcoded at 0 which is fixed by SRPMixins \"Fix Min Assimilations Zero\" to actually mean 0. \n" +
            "Due to this, sim big spiders actually spawn naturally from the phase spawn lists they are in. \n" +
            "To make them again only spawn from some beckon spawn lists - how it behaves (incorrectly?) in base SRP - the default value here is 1, which can only be reached if using wyrmsofnyrus (since no other mobs are assimilated to big spiders). \n" +
            "To have them spawn naturally as i think its intended to (why else would the be in the phase spawn lists?), set this value back to 0.\n" +
            "Fully disable this mixin with -1 (requires restart).")
    @Config.Name("Sim Big Spider Min Assimilations")
    public int assimBigSpiderMinAssimilations = 1;
}
