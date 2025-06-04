package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class WeaponConfig {
    @Config.Comment("Fully disable the sentient evolution mechanic where living weapons/armor/bow evolve to sentient after x kills/hits")
    @Config.Name("Disable Sentient Evolution Mechanic")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.sentevodisable.json")
    public boolean disableSentientEvolution = false;

    @Config.Comment("Make living weapons+armor evolving to sentient keep their NBT")
    @Config.Name("Fix parasite weapon+armor evolution NBT loss")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.sentnbtlossfix.json")
    public boolean fixSentientEvolutionNBT = true;

    @Config.Comment("Sentient weapons and armor keep counting parasite kills(/HP) even though it doesn't do anything for them. Set to true to remove this Tooltip")
    @Config.Name("Remove Parasite Kills tooltip from sentient weapons")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.senttooltipremove.json")
    public boolean removeSentientSRPKillsTooltip = true;

    @Config.Comment("Copy the same sentient evolution handling of living weapons to living bow")
    @Config.Name("Sentient Bow Evolution")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.bowevo.json")
    public boolean addBowEvolution = true;
}
