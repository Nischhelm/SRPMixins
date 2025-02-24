package srpmixins.mixin.simenderman;

import com.dhanantry.scapeandrunparasites.entity.monster.feral.EntityFerEnderman;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityFerEnderman.class)
public abstract class FeralEndermanTp {

    @ModifyConstant(
            method = "teleportAllies",
            constant = @Constant(doubleValue = 64.0),
            remap = false
    )
    private double changeSimmermanTPDistance(double constant){
        return SRPMixinsConfigHandler.simmermen.simmermenTpDistance;
    }
}