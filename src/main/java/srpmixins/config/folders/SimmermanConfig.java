package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class SimmermanConfig {
    @Config.Comment("Distance from which Assimilated and Feral Endermen search for mobs to tp, default 64 (performance)")
    @Config.Name("Assimilated/Feral Endermen tp radius")
    @Config.RangeDouble(min = 0.0)
    public double simmermenTpDistance = 40.0;

    @Config.Comment("Make Assimilated Endermen be able to despawn if they got converted in the end (performance)")
    @Config.Name("End Simmermen despawn")
    @Config.RequiresMcRestart
    public boolean despawnEndSimmermen = true;

    @Config.Comment("Max amount of Assimilated Endermen that can spawn via assimilation in the end (Disable with -1, requires MC restart for full disable)")
    @Config.Name("End Simmermen Conversion Cap")
    @Config.RangeInt(min = -1)
    public int endSimmermenCap = 40;
}
