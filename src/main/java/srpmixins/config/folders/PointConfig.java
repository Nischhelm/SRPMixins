package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

@MixinConfig(name = SRPMixins.MODID)
public class PointConfig {
    @Config.Comment("Bloody Clock also displays progress to next phase in percent")
    @Config.Name("Bloody Clock percentage")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.modifiedclock.json", defaultValue = true)
    public boolean modifyBloodyClock = true;

    @Config.Comment("If Bloody CLock percentage is true, also show point cooldown when using the clock")
    @Config.Name("Bloody Clock shows cooldown")
    public boolean bloodyClockShowsCooldown = true;

    @Config.Comment("Only give one penalty of evolution phase points when players sleep instead of a penalty per sleeping player (if player phases off)")
    @Config.Name("Flat sleep point penalty")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.flatsleeppenalty.json", defaultValue = true)
    public boolean flatSleepPenalty = true;

    @Config.Comment("Players can only get point penalty from adapted mobs despawning from this phase onwards (disable with -1, needs MC restart for full disable)")
    @Config.Name("Adapted Despawn Penalty First Phase")
    public int adaptedDespawnPenaltyPhase = 4;

    @Config.Comment("Players can only get point penalty from parasitic biome spreading (disable with -1, needs MC restart for full disable)")
    @Config.Name("Biome Spreading Penalty First Phase")
    public byte biomeSpreadingPenaltyPhase = 5;

    @Config.Comment("Send logs when phase or nodes would get accidentally reset (gets prevented by SRPMixins, but should still be fixed directly)")
    @Config.Name("Phases reset debug mode")
    public boolean phaseResetDebugMode = true;

    @Config.Comment("Limit point reduction from parasite kills to the min point value for each phase, stopping unintended phase decreases")
    @Config.Name("Fix phase point reduction")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.pointreductionlimit.json", defaultValue = true)
    public boolean limitPointReduction = true;

    @Config.Comment("SRP sometimes gets Evolution Data on clientside which overrides the current evolution phase in single player. Enable to stop this from happening")
    @Config.Name("Fix Phase Resets")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.phaseresetfix.json", defaultValue = true)
    public boolean fixPhaseResets = true;

    @Config.Comment("SRP doesn't prevent getting Node Data on clientside which would override the current node/colony data in single player. Nothing in SRP code actually does that, but SRP addon mods might. Enable to prevent this from happening.")
    @Config.Name("Fix Node Resets")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.noderesetfix.json", defaultValue = true)
    public boolean fixNodeResets = true;

    @Config.Comment("If Adapted mobs spawn and instantly despawn again due to distance to a player, SRP still gives players point penalty. This fixes it.")
    @Config.Name("Fix Adapted Penalty on Instant Despawn")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.adaptedinstadespawnpenalty.json", defaultValue = true)
    public boolean fixAdaptedPenaltyInstantDespawn = true;
}
