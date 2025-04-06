package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class ModCompatConfig {
    @Config.Comment("Set to false to disable all bloodmoon related tweaks")
    @Config.Name("Enable Bloodmoon tweaks")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.bloodmoon.json")
    @MixinConfig.CompatHandling(modid = "bloodmoon")
    @MixinConfig.CompatHandling(modid = "lostcities")
    public boolean enableBloodmoon = false;

    @Config.Comment("Set to false to disable all lost cities related tweaks. This also disables the bloodmoon tweaks as they only work inside the lost cities")
    @Config.Name("Enable Lost Cities tweaks")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.lostcities.json")
    @MixinConfig.CompatHandling(modid = "lostcities")
    public boolean enableLostCities = false;

    @Config.Comment("Disable Lures in LC and instead spawn a Dispatcher Nidus")
    @Config.Name("Lures disabled in LC")
    public boolean disableLuresInLC = true;

    @Config.Comment("Blood moons happen in Lost Cities dimension (requires this mod on client to see red moon), with increased parasite mob cap")
    @Config.Name("Do Blood Moons in LC")
    public boolean bloodmoonInLC = true;

    @Config.Comment("Multiply Parasite Mob Cap by this much during Blood Moons (if using SRP custom spawner)")
    @Config.Name("Bloodmoon Parasite Cap Multiplier")
    @Config.RangeInt(min = 0)
    public float bloodmoonInLCmobCapMultiplier = 4;

    @Config.Comment("LC Portals are locked until reaching this phase. Disable with -1")
    @Config.Name("LC Portal Phase Lock")
    public int portalLClockedPhase = 6;
}
