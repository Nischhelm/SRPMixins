package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class PointConfig {
    @Config.Comment("Bloody Clock also displays progress to next phase in percent")
    @Config.Name("Bloody Clock percentage")
    @Config.RequiresMcRestart
    public boolean modifyBloodyClock = true;

    @Config.Comment("If Bloody CLock percentage is true, also show point cooldown when using the clock")
    @Config.Name("Bloody Clock shows cooldown")
    public boolean bloodyClockShowsCooldown = true;

    @Config.Comment("Only give one penalty of evolution phase points when players sleep instead of a penalty per sleeping player (if player phases off)")
    @Config.Name("Flat sleep point penalty")
    @Config.RequiresMcRestart
    public boolean flatSleepPenalty = true;

    @Config.Comment("Players can only get point penalty from adapted mobs despawning from this phase onwards (disable with -1, needs MC restart for full disable)")
    @Config.Name("Adapted Despawn Penalty First Phase")
    public int adaptedDespawnPenaltyPhase = 4;

    @Config.Comment("Players can only get point penalty from parasitic biome spreading (disable with -1, needs MC restart for full disable)")
    @Config.Name("Biome Spreading Penalty First Phase")
    public byte biomeSpreadingPenaltyPhase = 5;

    @Config.Comment("Send logs when phase would get accidentally reset (gets prevented by SRPMixins, but should still be fixed directly)")
    @Config.Name("Phases reset debug mode")
    public boolean phaseResetDebugMode = true;

    @Config.Comment("Limit point reduction from parasite kills to the min point value for each phase, stopping unintended phase decreases")
    @Config.Name("Fix phase point reduction")
    @Config.RequiresMcRestart
    public boolean limitPointReduction = true;

    @Config.Comment("SRP sometimes gets Evolution Data on clientside which overrides the current evolution phase in single player. Enable to stop this from happening")
    @Config.Name("Fix Phase Resets")
    @Config.RequiresMcRestart
    public boolean fixPhaseResets = true;

    @Config.Comment("If Adapted mobs spawn and instantly despawn again due to distance to a player, SRP still gives players point penalty. This fixes it.")
    @Config.Name("Fix Adapted Penalty on Instant Despawn")
    @Config.RequiresMcRestart
    public boolean fixAdaptedPenaltyInstantDespawn = true;

    @Config.Comment("SRP has a default value per dimension of whether it should be able to gain or lose points. However, those values are unused. This fixes it")
    @Config.Name("Fix default canGain/Lose")
    @Config.RequiresMcRestart
    public boolean fixDefaultGainLoss = true;
}
