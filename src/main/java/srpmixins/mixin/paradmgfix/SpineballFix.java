package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectileSpineball;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.SRPMixins;
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(EntityProjectileSpineball.class)
public class SpineballFix {
    @Shadow(remap = false) private float damage;

    @Inject(
            method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;DDDF)V",
            at = @At(value = "TAIL"),
            remap = false
    )
    public void fixProjDmg(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ, float projDamage, CallbackInfo ci, @Local(argsOnly = true) World world){
        //Used by Prim Yelloweye, Ada Yelloweye, Herd, Vermin, Sentry
        if(SRPMixinsConfigHandler.dmgfix.doDamageFixes) {
            int dimension = world.provider.getDimension();
            this.damage = projDamage * SRPMixins.dimensionDmgMultipliers.getOrDefault(dimension,1F);
        }
    }
}
