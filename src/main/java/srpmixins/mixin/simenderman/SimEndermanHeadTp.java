package srpmixins.mixin.simenderman;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.head.EntityInfEndermanHead;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityInfEndermanHead.class)
public abstract class SimEndermanHeadTp {

    @ModifyConstant(
            method = "teleportAllies",
            constant = @Constant(doubleValue = 64.0),
            remap = false
    )
    private double changeSimmermanTPDistance(double constant){
        return SRPMixinsConfigHandler.simmermen.simmermenTpDistance;
    }
}