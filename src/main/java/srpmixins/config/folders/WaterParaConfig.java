package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class WaterParaConfig {

    @Config.Comment("Make Primitive Devourer and Assimilated Squid spawn correctly in water")
    @Config.Name("Enable Water Spawns")
    @Config.RequiresMcRestart
    public boolean enableWaterSpawns = true;

    @Config.Comment("Make Water Parasites (Assim Squid + Prim Devourer) target Squids")
    @Config.Name("Water Parasites Target Squids")
    @Config.RequiresMcRestart
    public boolean parasTargetSquids = true;

    @Config.Comment("After there is this many Assimilated Squid or Primitive Devourers loaded, no more will spawn from conversion or spawning. Disable with -1, needs restart. Only works using SRP Phase Custom Spawner + Evo Phases")
    @Config.Name("Water Parasite Mob Cap")
    public int waterParasiteCap = 10;
}
