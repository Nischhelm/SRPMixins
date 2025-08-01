package srpmixins.mixin.paradmgfix.statmultis;

import com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectileAlafhaBall;
import com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectileDragonE;
import com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectileWebball;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import srpmixins.config.providers.DimensionMultiConfigProvider;

@Mixin(value = {
        EntityProjectileAlafhaBall.class,
        EntityProjectileWebball.class,
        EntityProjectileDragonE.class
})
public abstract class EntityLivingBaseProjFix extends EntityFireball {
    public EntityLivingBaseProjFix(World worldIn) {
        super(worldIn);
    }

    @ModifyArg(
            method = "onImpact",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z")
    )
    private float srpmixins_fixProjDmg(float original, @Local EntityLivingBase target) {
        if(this.shootingEntity != null){
            //Automatically has dimension multis and stat multis
            return (float) this.shootingEntity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        } else
            return original * DimensionMultiConfigProvider.getDmgMap().getOrDefault(target.dimension,1F);
    }
}
