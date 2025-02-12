package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class WeaponConfig {
    @Config.Comment("Fully disable the sentient evolution mechanic where living weapons/armor/bow evolve to sentient after x kills")
    @Config.Name("Disable Sentient Evolution Mechanic")
    public boolean disableSentientEvolution = false;

    @Config.Comment("Fix parasites getting hit by sentient weapons not doing the correct things")
    @Config.Name("Fix parasite weapon damage")
    public boolean fixParasiteDmg = true;

    @Config.Comment("Make living weapons evolving to sentient keep their NBT")
    @Config.Name("Fix parasite weapon evolution NBT loss")
    public boolean fixSentientEvolutionNBT = true;

    @Config.Comment("Sentient weapons keep counting parasite kills(/HP) even though it doesn't do anything for them. Set to true to remove this Tooltip")
    @Config.Name("Remove Parasite Kills tooltip from sentient weapons")
    public boolean removeSentientSRPKillsTooltip = true;

    @Config.Comment("Copy the same sentient evolution handling of living weapons to living armor and living bow")
    @Config.Name("Sentient Armor+Bow Evolution")
    public boolean addArmorBowEvolution = true;
}
