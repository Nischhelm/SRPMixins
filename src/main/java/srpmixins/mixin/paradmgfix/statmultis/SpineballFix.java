package srpmixins.mixin.paradmgfix.statmultis;

import com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectileSpineball;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.SRPMixins;
import srpmixins.config.providers.DimensionMultiConfigProvider;

@Mixin(EntityProjectileSpineball.class)
public abstract class SpineballFix {
    @Shadow(remap = false) private float damage;

    @Inject(
            method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;DDDF)V",
            at = @At(value = "TAIL"),
            remap = false
    )
    private void srpmixins_fixProjDmg(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ, float projDamage, CallbackInfo ci){
        //Used by Prim Yelloweye, Ada Yelloweye, Herd, Vermin, Sentry
        int dimension = worldIn.provider.getDimension();
        if(shooter != null) {
            IAttributeInstance atkAttr = shooter.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
            double baseValue = atkAttr.getBaseValue();
            if (baseValue != 0) {
                //good old switcheroo
                atkAttr.setBaseValue(projDamage);
                this.damage = (float) atkAttr.getAttributeValue();
                atkAttr.setBaseValue(baseValue);
                return;
            }
        }

        //else ignore stat increase rules
        this.damage = projDamage * DimensionMultiConfigProvider.getDmgMap().getOrDefault(dimension,1F);
    }
}
