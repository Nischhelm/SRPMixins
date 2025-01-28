package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectileLenciaBall;
import com.dhanantry.scapeandrunparasites.util.SRPAttributes;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.SRPMixins;
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(EntityProjectileLenciaBall.class)
public class BogleProjFix {
    @Redirect(
            method = "func_70227_a",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/SRPAttributes;LENCIA_ATTACK_DAMAGE:D"),
            remap = false
    )
    double fixProjDmg(@Local(argsOnly = true) RayTraceResult result){
        if(SRPMixinsConfigHandler.dmgfix.doDamageFixes && result.entityHit != null) {
            int dimension = result.entityHit.dimension;
            return SRPAttributes.LENCIA_ATTACK_DAMAGE * SRPMixins.dimensionDmgMultipliers.getOrDefault(dimension,1F);
        }
        return SRPAttributes.LENCIA_ATTACK_DAMAGE;
    }
}
