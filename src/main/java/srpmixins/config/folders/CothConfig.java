package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class CothConfig {
    @Config.Comment("Fixes the srpcothimmunity tag (basically counting coth lvls) getting incremented for coth immune mobs, making them not immune anymore")
    @Config.Name("Fix srpcothimmunity tag")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.cothimmunityfix.json", defaultValue = true)
    public boolean fixSrpCothImmunity = true;

    @Config.Comment("If setting SRP config min assimilations required to spawn for a specific parasite, setting it to 0 will still not auto allow that spawn. This fixes it.")
    @Config.Name("Fix Min Assimilations Zero")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.minassimilations.json", defaultValue = true)
    public boolean fixMinAssimilations = true;

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

    @Config.Comment("Feral Bears couldn't get created from assimilated ones upgrading or from gnats. This fixes it.")
    @Config.Name("Fix Feral Bear Creation")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.feraliseferbears.json", defaultValue = true)
    public boolean fixFeralBearCreation = true;

    @Config.Comment("If mobs have camouflage, another mob spreading COTH to nearby will not check if it can see that first mob and instead only check the camouflage chance to prevent COTH spread. This fixes it.")
    @Config.Name("Fix Infect Nearby")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.infectnearbyfix.json", defaultValue = true)
    public boolean fixInfectNearby = true;

    @Config.Comment("When mobs convert due to COTH without a parasite attacking them, vanilla onDeath is not called for the entitiy. \n" +
            "This for example leads to dead pets not notifying their owner about their death in chat and mobs with inventories (donkeys etc) not dropping the contents of their inventories. \n" +
            "This fix calls onDeath and cancels the normal mob drops that would result in calling onDeath.")
    @Config.Name("Call OnDeath when converting")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.cothondeath.json", defaultValue = true)
    public boolean fixCothOnDeath = true;

    @Config.Comment("Camouflage has a chance to protect against COTH (default: 70%). Since a lot of effects try to apply COTH every single tick, any chance to protect against COTH will fail in less than a second.\n" +
            "This fix makes Camouflage only check once whether it protects against COTH and then either protect or not protect for the entire duration.\n" +
            "This will also not apply Camouflage to mobs that already have COTH, which gives a small ability to check if a mob has low stage COTH (otherwise green particles)\n" +
            "NOTE: Camouflage only protects against COTH from some sources, not from all of them. Those sources are\n" +
            "- getting attacked by a parasite (except rupter)\n" +
            "- standing on gore/remain\n" +
            "- mobs & assimilated paras spreading COTH to nearby")
    @Config.Name("Fix Camouflage")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.camouflagefix.json", defaultValue = true)
    public boolean fixCamouflage = true;
}
