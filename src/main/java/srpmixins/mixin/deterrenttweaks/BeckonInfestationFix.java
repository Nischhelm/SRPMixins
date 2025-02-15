package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityPStationaryArchitect.class)
public abstract class BeckonInfestationFix {
    @Redirect(
            method = "freeDead",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPStationaryArchitect;getRSChance(B)D"),
            remap = false
    )
    private double stopBlockReversion(EntityPStationaryArchitect beckon, byte phase) {
        double defaultValue = beckon.getRSChance(phase);
        if(!SRPMixinsConfigHandler.deterrents.fixInfestedBlockReversion) return defaultValue;
        if(SRPConfigSystems.useEvolution) return defaultValue;
        //Use non-evolution chance if evolution is disabled so beckons upgrading don't make the blocks around them turn back
        //Also fixes higher stage infested blocks turning back even though they shouldn't
        //https://discord.com/channels/717978802407342102/717984219321597972/1340159236981919754
        return SRPConfigSystems.rschance;
    }
}