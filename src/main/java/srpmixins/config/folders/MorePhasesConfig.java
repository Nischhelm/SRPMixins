package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class MorePhasesConfig {
    @Config.Comment("SRP uses a fixed max phase of 10. Increase or decrease it with this value. If using this for the first time, the SRPMixins config will look awful on first start, fixes itself on restart. Using this will disable the base SRP configs for the values that are listed here.")
    @Config.Name("Enable More Phases")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.morephases.json", defaultValue = false)
    public boolean enableMorePhases = false;

    @Config.Comment("SRP uses a fixed max phase of 10. Increase or decrease it with this value.")
    @Config.Name("Max Phase")
    public byte maxEvolutionPhase = 10;
    
    //Points
    @Config.Comment("Number of points required to reach each phase (first entry for phase 0). Clear this list if you want SRPMixins to re-copy over your SRP config values.")
    @Config.Name("Phase Points")
    public int[] phaseKills = {};

    @Config.Comment("Each second the killcount will go up by this amount for each phase (first entry for phase 0)")
    @Config.Name("Phase Killcount Plus")
    public double[] phaseKillCountPlus = {};

    @Config.Comment("Message sent to all players in the current world when parasites reach each Phase (first entry for phase 0)")
    @Config.Name("Phase Warning Message")
    public String[] phaseWarning = {};

    @Config.Comment("Parasites will not be able to earn points until this time (seconds) has passed after a phase has started (first entry for phase 0)")
    @Config.Name("Phase Delay")
    public int[] phaseDelayTicks = {};

    @Config.Comment("Number of points gained when skipping the night (first entry for phase 0)")
    @Config.Name("Sleep Penalty")
    public int[] sleepPenalty = {};
    
    //Spawning
    @Config.Comment("Only used if Custom Phase Spawner is disabled, otherwise the spawn list is used. If a parasite type ID is less than or equal to this number, the parasite will not spawn (first entry for phase 0)")
    @Config.Name("Spawning - Minimum Parasite Type ID")
    public int[] phaseMinParasiteID = {};

    @Config.Comment("Only used if Custom Phase Spawner is disabled, otherwise the spawn list is used. If a parasite type ID is equal to or greater than this number, the parasite will not spawn (first entry for phase 0)")
    @Config.Name("Spawning - Maximum Parasite Type ID")
    public int[] phaseMaxParasiteID = {};

    @Config.Comment("List for parasites that will spawn at each phase. Pattern: [startPhase - endPhase, otherPhase, anotherPhase, etc]; modid:mobname; minGroupCount; maxGroupCount; weight")
    @Config.Name("Spawning - Phase Spawn List")
    public String[] phaseSpawnList = {};

    //Beckons
    @Config.Comment("Chance to spawn a Beckon when a parasite is killed (first entry for phase 0)")
    @Config.Name("Reinforcement System Chance")
    public double[] reinforcementSystemChance = {};

    @Config.Comment("Chance for nexus parasites to fail to grow (first entry for phase 0). Each line has pattern chanceStage1; chanceStage2; chanceStage3")
    @Config.Name("Nexus Grow Stun Chance")
    public String[] nexusGrowPenalty = {};

    @Config.Comment("One in X to spawn a Beckon on Infested Block Residue (first entry for phase 0)")
    @Config.Name("Odds Residue Spawns Beckon")
    public int[] phaseResidue = {};

    //Scents
    @Config.Comment("Death bonus value used if Collective Consciousness is enabled (first entry for phase 0)")
    @Config.Name("Scent Death Bonus")
    public int[] phaseScentBonus = {};

    @Config.Comment("Reaction bonus value used for a scent to go active (first entry for phase 0)")
    @Config.Name("Scent Reaction Bonus")
    public int[] phaseScentReaction = {};

    //Special effects
    @Config.Comment("Chance for an entity to spawn with COTH I (first entry for phase 0)")
    @Config.Name("Mobs Spawn With COTH Chance")
    public double[] mobSpawningCOTHChance = {};

    @Config.Comment("Chance for crop grow to be stunned (first entry for phase 0)")
    @Config.Name("Crop Grow Stunned")
    public double[] cropGrowStunned = {};
}
