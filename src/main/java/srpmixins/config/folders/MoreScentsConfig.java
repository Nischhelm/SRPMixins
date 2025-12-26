package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class MoreScentsConfig {
    //TODO: ParasiteEventEntity.leaveScent could have the SRPConfigSystems.scentDeathSpawning be phase dependent
    //TODO: maybe remove check for blocklight if phase >= ignoresunlight
    //TODO: maybe change range in which scents check for stuff (currently 160x160x160 kinda big)
    //TODO: change less than how many paras need to be around the scent for it to start spawning (rn <=6)
    //TODO: remove mob cap check for scent placeWaves or at least make it a bit more lenient (+10)

    @Config.Comment({
            "Enable config overrides for Scent behavior.",
            "To have it all in one spot, or to modify the amount of scent levels (default: 0-8)",
            "When enabled and \"Points Required\" list is empty on first startup, SRPMixins will copy current SRP(System) configs into the lists."
    })
    @Config.Name("Enable More Scents")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.morescents.json", defaultValue = false)
    public boolean enableMoreScents = false;

    @Config.Comment({
            "Number of scent levels to be used.",
            "If you modify this value, you will have to adapt all the lists in this section (except spawn list) to have the correct length (N+1, so 9 with default 8 lvls), otherwise it will crash"
    })
    @Config.Name("Max Scent Level")
    @Config.RangeInt(min = 0)
    public byte maxScentLevel = 8;

    @Config.Comment({
            "Points required to increase scent level.",
            "Scent level decides what mobs are spawned (see Wave Spawn List)",
            "One entry per scent level (9 by default)"
    })
    @Config.Name("Points Required")
    public int[] pointsRequired = {};

    @Config.Comment({
            "Min to max number of waves to spawn in Builder state.",
            "One entry per scent level (9 by default)",
            "Pattern: min - max"
    })
    @Config.Name("Wave Count")
    public String[] waveCount = {};

    @Config.Comment({
            "Min to max number of mobs per wave.",
            "One entry per scent level (9 by default)",
            "Pattern: min - max"
    })
    @Config.Name("Mobs per Wave Count")
    public String[] mobPerWaveCount = {};

    @Config.Comment({
            "Wave spawn lists with scent level ranges and optional weights.",
            "Pattern: \"[startScentLvl-endScentLvl]; modid:entity; weight\". Missing weights default to 1.",
            "Like the phases in \"More Phases\" spawn lists you can arbitrarily mix and match levels, so [1] works just as well as [0-5, 8]",
            "If the mob is from SRP, you can leave out the modid",
            "Examples:",
            "  [0]; rupter",
            "  [4 - 6]; srparasites:mangler; 3",
            "  [0-1, 7-8]; srparasites:heed; 2",
    })
    @Config.Name("Wave Spawn List")
    public String[] waveSpawnList = {};

    @Config.Comment({
            "How many points a nearby scent is increased by when a dying parasite would have created a new scent, depending on parasite group or parasite",
            "Scent points are used to compare against the \"Points Required\" config to increase its level",
            "Pattern: group or name, value",
            "Exampes: ",
            " PURE, 5",
            " rupter, 2",
            " someaddonmodid:someaddonparaname, 420",
            "If a parasite has a group and is named separately here, the parasite specific value will be used, the group value will be ignored"
    })
    @Config.Name("Mob Death Values")
    public String[] deathValues = {};
}
