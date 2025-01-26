package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectileAngedball;
import com.dhanantry.scapeandrunparasites.util.SRPAttributes;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.RayTraceResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.SRPMixins;
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(EntityProjectileAngedball.class)
public class VigilanteProjFix {
    @Redirect(
            method = "func_70227_a",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/SRPAttributes;ANGED_RANGED_ATTACK_DAMAGE:D"),
            remap = false
    )
    double fixProjDmg(@Local(argsOnly = true) RayTraceResult result){
        if(SRPMixinsConfigHandler.dmgfix.doDamageFixes) {
            int dimension = result.entityHit.dimension;
            return SRPAttributes.ANGED_RANGED_ATTACK_DAMAGE * SRPMixins.dimensionDmgMultipliers.getOrDefault(dimension,1F);
        }
        return SRPAttributes.ANGED_RANGED_ATTACK_DAMAGE;
    }
}
