package srpmixins.config.folders;

import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import srpmixins.SRPMixins;

import java.util.HashMap;
import java.util.Map;

@MixinConfig(name = SRPMixins.MODID)
public class PointConfig {
    @Config.Comment("Bloody Clock also displays progress to next phase in percent")
    @Config.Name("Bloody Clock percentage")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.modifiedclock.json", defaultValue = true)
    public boolean modifyBloodyClock = true;

    @Config.Comment("If Bloody Clock percentage is true, also show point cooldown when using the clock")
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

    @Config.Comment("Players can only get point penalty from parasitic biome spreading from this phase on (disable with -1, needs MC restart for full disable)")
    @Config.Name("Biome Spreading Penalty First Phase")
    public byte biomeSpreadingPenaltyPhase = 5;

    @Config.Comment("Players can only get point penalty from beckon infestation spreading from this phase on (disable with -1, needs MC restart for full disable)")
    @Config.Name("Infestation Penalty First Phase")
    public byte infestationPenaltyPhase = 4;

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

    @Config.Comment("Allows using parasite names instead of ids for the SRParasitesSystems.cfg \"Evolution Parasite Lock List\".\n" +
            "Also checks for currphase >= lockphase instead of only currphase == lockphase as SRP does (only matters when using commands or high dim starting phases).")
    @Config.Name("Fix Parasites Unlocking")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.fixparaunlocking.json", defaultValue = true)
    public boolean unlockOnHigherPhases = true;

    @Config.Comment("Allows using the parasite name inside the unlocking message (default: \"...\"), so it can for example be \"Unlocked Assimilated Enderdragon\".\n" +
            "Use the usual printf flags for it, so %s or %1$s.\n" +
            "This also automatically allows using a lang key instead. Add the %s flag inside the lang key translation then.")
    @Config.Name("Allow Parasite Name in Unlock Message")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.srp.unlockmessagewithname.json", defaultValue = true)
    public boolean unlockWithParaName = true;

    @Config.Comment("Define custom parasite unlock messages here. These will only be used if the specified parasite is unlocked\n" +
            "Requires \"Allow Parasite Name in Unlock Message\"")
    @Config.Name("Custom Unlock Messages")
    public Map<String, String> customUnlockMessages = new HashMap<String,String>() {{
            put("sim_dragone","You hear distant wings flapping... A great beast has awoken");
    }};

    @Config.Comment("SRP has an option for parasites to stop dropping XP from a certain phase onwards. \n" +
            "To make this option a bit less intrusive, this list can be used to slowly decrease (or increase) the amount of xp dropped.\n" +
            "Starts with phase 0, can be used beyond phase 10 if \"More Phases\" is enabled.\n" +
            "Leave this list empty to fully disable the handler.\n" +
            "WARNING: This does not disable the original SRP option, so if that is enabled and phase is high enough, parasites will drop 0 xp no matter what")
    @Config.Name("XP Phase Multipliers")
    public float[] xpMultis = {
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1
    };

    @Config.Comment("SRP gives parasites potion effects depending on how many nodes and/or colonies exist in the world. \n" +
            "This tweak only applies those bonuses if the current phase is equal or higher than the phase from which on nodes or colonies are allowed.\n" +
            "Intended for custom phases where otherwise players/chunks with low phase would still experience stronger parasites from nodes/colonies at other spots in the world.")
    @Config.Name("Phaselocked Node/Colony Bonuses")
    @Config.RequiresMcRestart
    @MixinConfig.MixinToggle(lateMixin = "mixins.srpmixins.customphasemechanics_nodecolonyphaselock.json", defaultValue = false)
    public boolean nodeColonyPhaseLock = false;
}
