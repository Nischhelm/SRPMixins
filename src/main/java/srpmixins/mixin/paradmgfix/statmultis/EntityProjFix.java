package srpmixins.mixin.paradmgfix.statmultis;

import com.dhanantry.scapeandrunparasites.entity.projectile.*;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import srpmixins.config.providers.DimensionMultiConfigProvider;

@Mixin(value = {
        EntityProjectileElviaBall.class,
        EntityProjectileLenciaBall.class,
        EntityProjectileAngedball.class
})
public abstract class EntityProjFix extends EntityFireball {
    public EntityProjFix(World worldIn) {
        super(worldIn);
    }

    @ModifyArg(
            method = "onImpact",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z")
    )
    private float srpmixins_fixProjDmg(float original, @Local(argsOnly = true) RayTraceResult result){
        if(result.entityHit == null) return original; //idk why we need to test this but apparently i had crashes a long time ago?

        if(this.shootingEntity != null) {
            //Automatically has dimension multis and stat increase rule multis
            return (float) this.shootingEntity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        } else {
            int dimension = result.entityHit.dimension;
            return original * DimensionMultiConfigProvider.getDmgMap().getOrDefault(dimension, 1F);
        }
    }
}
