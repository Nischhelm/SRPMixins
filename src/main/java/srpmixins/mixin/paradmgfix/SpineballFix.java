package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectileSpineball;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import srpmixins.SRPMixins;
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(EntityProjectileSpineball.class)
public class SpineballFix {
    @ModifyVariable(
            method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;DDDF)V",
            at = @At(value = "HEAD"),
            remap = false,
            argsOnly = true
    )
    private static float fixProjDmg(float projDamage, @Local(argsOnly = true) World world){
        //Used by Prim Yelloweye, Ada Yelloweye, Herd, Vermin, Sentry
        if(SRPMixinsConfigHandler.dmgfix.doDamageFixes) {
            int dimension = world.provider.getDimension();
            return projDamage * SRPMixins.dimensionDmgMultipliers.getOrDefault(dimension,1F);
        }
        return projDamage;
    }
}
