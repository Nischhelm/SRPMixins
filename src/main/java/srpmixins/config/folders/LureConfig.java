package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class LureConfig {
    @Config.Comment("Change Carcass Point Reduction based on Phase")
    @Config.Name("Phase dependent Carcass Values")
    @Config.RequiresMcRestart
    public boolean variableCarcassValues = true;

    @Config.Comment("Phase multiplier on carcass values (0 to 10). Default values are balanced against Carcasses having values of 1,3,10,40,100,1000 for the 6 available Carcass variants in SRPSystems cfg.")
    @Config.Name("Carcass Phase Multipliers")
    public int[] carcassPhaseMultis = {
            40,
            40,
            80,
            1000,
            6000,
            50000,
            200000,
            200000,
            200000,
            400000,
            400000
    };

    @Config.Comment("Make Carcasses reduce points while cooldown is active")
    @Config.Name("Fix Carcasses not working during cooldown")
    @Config.RequiresMcRestart
    public boolean fixCarcassDuringCooldown = true;

    @Config.Comment("Make using Lures add their cooldown to current cooldown instead of setting it to a fixed value, possibly even reducing the cooldown by doing that")
    @Config.Name("Lures stack cooldown")
    @Config.RequiresMcRestart
    public boolean lureCooldownStacking = true;

    @Config.Comment("Only allow carcasses built from 5 lures of the same type. Without this fix, only the level of the center lure matters")
    @Config.Name("Force carcass all same lure type")
    @Config.RequiresMcRestart
    public boolean forceCarcassSameLureVariant = true;

    @Config.Comment("When using faint lures, SRP also calls setCooldown for lures 9 and 10 (they forgot to set a break). This fixes it.")
    @Config.Name("Fix Cooldown Overflow")
    @Config.RequiresMcRestart
    public boolean fixCooldownOverflow = true;
}
