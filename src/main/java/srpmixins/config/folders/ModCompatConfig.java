package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class ModCompatConfig {
    @Config.Comment("Set to false to disable all bloodmoon related tweaks")
    @Config.Name("Enable Bloodmoon tweaks")
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.bloodmoon.json", defaultValue = false)
    @MixinConfig.CompatHandling(modid = "bloodmoon", desired = true, reason = "Mod Compat for Bloodmoon, requires Bloodmoon")
    @MixinConfig.CompatHandling(modid = "lostcities", desired = true, reason = "Mod Compat for Lost Cities, requires Lost Cities")
    @Config.RequiresMcRestart
    public boolean enableBloodmoon = false;

    @Config.Comment("Set to false to disable all lost cities related tweaks. This also disables the bloodmoon tweaks as they only work inside the lost cities")
    @Config.Name("Enable Lost Cities tweaks")
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.lostcities.json", defaultValue = false)
    @MixinConfig.CompatHandling(
            modid = "lostcities",
            desired = true,
            reason = "Mod Compat for Lost Cities, requires Lost Cities"
    )
    @Config.RequiresMcRestart
    public boolean enableLostCities = false;

    @Config.Comment("Disable Lures in LC and instead spawn a Dispatcher Nidus")
    @Config.Name("Lures disabled in LC")
    public boolean disableLuresInLC = true;

    @Config.Comment("Blood moons happen in Lost Cities dimension (requires this mod on client to see red moon), with increased parasite mob cap")
    @Config.Name("Do Blood Moons in LC")
    public boolean bloodmoonInLC = true;

    @Config.Comment("Multiply Parasite Mob Cap by this much during Blood Moons (if using SRP custom spawner). \n" +
            "Doesn't work if \"Fix Spawning Entirely\" is enabled, use \"Parasite Mob Cap Rules\" for that")
    @Config.Name("Bloodmoon Parasite Cap Multiplier")
    @Config.RangeInt(min = 0)
    public float bloodmoonInLCmobCapMultiplier = 4;

    @Config.Comment("LC Portals are locked until reaching this phase. Disable with -1")
    @Config.Name("LC Portal Phase Lock")
    public int portalLClockedPhase = 6;

    @Config.Comment("SRPMixins has compat with OverLast when using custom phases (player phases or chunk phases). \n" +
            "Use this to toggle off the compatibility.\n" +
            "It is auto disabled anyway if you dont use custom phases or OverLast.\n" +
            "Works with both OverLast and OverLastLite (only HUD)")
    @Config.Name("Enable OverLast custom phases")
    @Config.RequiresMcRestart
    public boolean enableOverLastCustomPhases = true;

    @Config.Comment("Set to true to make \"Scape and Spartan: Parasites\" weapons also ignore parasite damage caps.")
    @Config.Name("Scape and Spartan: Parasites Compat - Ignore Dmg Cap")
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.swparasites.json", defaultValue = false)
    @MixinConfig.CompatHandling(
            modid = "swparasites",
            desired = true,
            reason = "Mod Compat for Scape and Spartan: Parasites, requires Scape and Spartan: Parasites"
    )
    @Config.RequiresMcRestart
    public boolean enableSWParasitesCompat = false;

    @Config.Comment("This allows using some additional rules in InControl specifically for SRP\n" +
            "New Rules: \"srp_minphase\", \"srp_maxphase\", \"srp_minnodes\", \"srp_maxnodes\", \"srp_mincolos\", \"srp_maxcolos\"\n" +
            "All rules take a single integer number as comparison and can be used in any incontrol json.\n" +
            "Note: Nodes and Colonies are counted over all dimensions, thats just how SRP works")
    @Config.Name("In Control Compat - Add Rules")
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.incontrol.json", defaultValue = false)
    @MixinConfig.CompatHandling(
            modid = "incontrol",
            desired = true,
            reason = "Mod Compat for In Control, requires In Control"
    )
    @Config.RequiresMcRestart
    public boolean enableInControlCompat = false;

}
