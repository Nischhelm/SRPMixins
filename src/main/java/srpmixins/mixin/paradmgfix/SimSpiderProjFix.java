package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectileWebball;
import com.dhanantry.scapeandrunparasites.util.SRPAttributes;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(EntityProjectileWebball.class)
public class SimSpiderProjFix {
    @Redirect(
            method = "onImpact",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/SRPAttributes;DORPA_RANGED_DAMAGE:F", remap = false)
    )
    float fixProjDmg(@Local(argsOnly = true) RayTraceResult result){
        if(SRPMixinsConfigHandler.dmgfix.doDamageFixes && result.entityHit != null) {
            int dimension = result.entityHit.dimension;
            return SRPAttributes.DORPA_RANGED_DAMAGE * SRPMixinsConfigProvider.dimensionDmgMultipliers.getOrDefault(dimension,1F);
        }
        return SRPAttributes.DORPA_RANGED_DAMAGE;
    }
}
