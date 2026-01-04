package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class WeaponConfig {
    @Config.Comment("Fully disable the sentient evolution mechanic where living weapons/armor/bow evolve to sentient after x kills")
    @Config.Name("Disable Sentient Evolution Mechanic")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.sentevodisable.json", defaultValue = false)
    public boolean disableSentientEvolution = false;

    @Config.Comment("Fix parasites getting hit by sentient weapons not doing the correct things")
    @Config.Name("Fix parasite weapon damage")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.weapondamage.json", defaultValue = true)
    public boolean fixParasiteDmg = true;

    @Config.Comment("Make living weapons evolving to sentient keep their NBT")
    @Config.Name("Fix parasite weapon evolution NBT loss")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.sentnbtlossfix.json", defaultValue = true)
    public boolean fixSentientEvolutionNBT = true;

    @Config.Comment("Sentient weapons keep counting parasite kills(/HP) even though it doesn't do anything for them. Set to true to remove this Tooltip")
    @Config.Name("Remove Parasite Kills tooltip from sentient weapons")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.senttooltipremove.json", defaultValue = true)
    public boolean removeSentientSRPKillsTooltip = true;

    @Config.Comment("Copy the same sentient evolution handling of living weapons to living armor and living bow")
    @Config.Name("Sentient Armor+Bow Evolution")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.armorbowevo.json", defaultValue = true)
    public boolean addArmorBowEvolution = true;

    @Config.Comment({
            "When living gear evolves to sentient, by default that gear is dropped on the ground. Set to true to keep in inventory.",
            "(If for whatever reason the stack has more than one item, it will always drop the evolved gear)",
            "Note: for armor and bow this only applies if \"Sentient Armor+Bow Evolution\" is enabled"
    })
    @Config.Name("Keep Evolved Gear")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.keepevolvedgear.json", defaultValue = false)
    public boolean keepEvolvedGear = false;

    @Config.Comment("Will allow living and sentient gear to be repaired by the items they are created with. Vile Shell for Armor, Infectious Blade Fragment for Weapons, Dried Tendons for Bows.")
    @Config.Name("Repairable Gear")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.repairableweapons.json", defaultValue = true)
    public boolean repairableGear = true;

    @Config.Comment("Change how living armor evolves to sentient. \n" +
            "DEAL_DAMAGE: gets points when players kill mobs, depending how much health they have, same as living weapons. \n" +
            "TAKE_DAMAGE: gets points when players take damage.")
    @Config.Name("Armor Evolution System")
    public EnumArmorEvolution armorEvoType = EnumArmorEvolution.DEAL_DAMAGE;

    @Config.Comment("If enabled, living gear will only get points when interacting (killing/taking dmg from) parasites, not just any mob.")
    @Config.Name("Evolution Only From Parasites")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.weaponevoonlyonparas.json", defaultValue = false)
    public boolean onlyParasites = false;

    public enum EnumArmorEvolution{DEAL_DAMAGE, TAKE_DAMAGE}
}
