package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectileElviaBall;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(EntityProjectileElviaBall.class)
public abstract class WraithProjFix {
    @ModifyExpressionValue(
            method = "onImpact",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/SRPAttributes;ELVIA_ATTACK_DAMAGE:D", remap = false)
    )
    private double fixProjDmg(double original, @Local(argsOnly = true) RayTraceResult result){
        if(result.entityHit != null) {
            int dimension = result.entityHit.dimension;
            return original * SRPMixinsConfigProvider.dimensionDmgMultipliers.getOrDefault(dimension,1F);
        }
        return original;
    }
}
