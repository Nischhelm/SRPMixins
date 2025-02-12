package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class PointConfig {
    @Config.Comment("Bloody Clock also displays progress to next phase in percent")
    @Config.Name("Bloody Clock percentage")
    public boolean modifyBloodyClock = true;

    @Config.Comment("If Bloody CLock percentage is true, also show point cooldown when using the clock")
    @Config.Name("Bloody Clock shows cooldown")
    public boolean bloodyClockShowsCooldown = true;

    @Config.Comment("Only give one penalty of evolution phase points when players sleep instead of a penalty per sleeping player (if player phases off)")
    @Config.Name("Flat sleep point penalty")
    public boolean flatSleepPenalty = true;

    @Config.Comment("Players can only get point penalty from adapted mobs despawning from this phase onwards (disable with -1)")
    @Config.Name("Adapted Despawn Penalty First Phase")
    public int adaptedDespawnPenaltyPhase = 4;

    @Config.Comment("Players can only get point penalty from parasitic biome spreading (disable with -1)")
    @Config.Name("Biome Spreading Penalty First Phase")
    public byte biomeSpreadingPenaltyPhase = 5;

    @Config.Comment("Send logs when phase would get accidentally reset (gets prevented by SRPMixins, but should still be fixed directly)")
    @Config.Name("Phases reset debug mode")
    public boolean phaseResetDebugMode = true;

    @Config.Comment("Limit point reduction from parasite kills to the min point value for each phase, stopping unintended phase decreases")
    @Config.Name("Fix phase point reduction")
    public boolean limitPointReduction = true;
}
