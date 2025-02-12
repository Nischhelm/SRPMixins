package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class ModCompatConfig {
    @Config.RequiresMcRestart
    @Config.Comment("Enable BloodMoon tweaks (don't set this to true if your modpack doesn't have BloodMoon, otherwise it will crash)")
    @Config.Name("Compat: Modpack has Bloodmoon mod")
    public boolean hasBloodmoon = false;

    @Config.RequiresMcRestart
    @Config.Comment("Enable LostCities tweaks (don't set this to true if your modpack doesn't have LostCities, otherwise it will crash)")
    @Config.Name("Compat: Modpack has LostCities mod")
    public boolean hasLostCities = false;

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
