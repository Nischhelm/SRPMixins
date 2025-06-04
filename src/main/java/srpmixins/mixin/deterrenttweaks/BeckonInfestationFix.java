package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPStationaryArchitect.class)
public abstract class BeckonInfestationFix {
    @WrapOperation(
            method = "freeDead",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPStationaryArchitect;getRSChance(B)D", ordinal = 1),
            remap = false
    )
    private double srpmixins_stopBlockReversion(EntityPStationaryArchitect beckon, byte phase, Operation<Double> original) {
        //Use non-evolution chance if evolution is disabled, so beckons upgrading don't make the blocks around them turn back
        //Also fixes higher stage infested blocks turning back even though they shouldn't
        //https://discord.com/channels/717978802407342102/717984219321597972/1340159236981919754
        if(!SRPConfigSystems.useEvolution) return SRPConfigSystems.rschance;
        return original.call(beckon, phase); //this.getRSChance(phase)
    }
}