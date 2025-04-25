package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;

public class SimmermanConfig {
    @Config.Comment("Distance from which Assimilated and Feral Endermen search for mobs to tp, default 64 (performance)")
    @Config.Name("Assimilated/Feral Endermen tp radius")
    @Config.RangeDouble(min = 0.0)
    public double simmermenTpDistance = 40.0;

    @Config.Comment("Make Assimilated Endermen be able to despawn if they got converted in the end (performance)")
    @Config.Name("End Simmermen despawn")
    @Config.RequiresMcRestart
    @MixinConfig.LateMixin(name = "mixins.srpmixins.srp.simmermandespawn.json")
    public boolean despawnEndSimmermen = true;

    @Config.Comment("Max amount of Assimilated Endermen that can spawn via assimilation in the end (Disable with -1, requires MC restart for full disable)")
    @Config.Name("End Simmermen Conversion Cap")
    @Config.RangeInt(min = -1)
    public int endSimmermenCap = 40;

    /*@Config.Comment("Min Distance from target at which Assimilated and Feral Endermen will tp themselves and other mobs for mobs to, default 1. Set to 0 to disable")
    @Config.Name("Assimilated/Feral Endermen min target tp radius")
    @Config.RangeDouble(min = 0.0)*/
    public double simmermenTpDistanceFromTargetMin = 1.0;

    /*@Config.Comment("Max Distance from target at which Assimilated and Feral Endermen will tp themselves and other mobs for mobs to, default 4. Set to 0 to disable")
    @Config.Name("Assimilated/Feral Endermen max target tp radius")
    @Config.RangeDouble(min = 0.0)*/
    public double simmermenTpDistanceFromTargetMax = 4.0;
}
