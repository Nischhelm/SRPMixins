package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.projectile.EntityProjectilePullball;
import com.dhanantry.scapeandrunparasites.entity.projectile.EntitySRPProjectile;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.List;

@Mixin(EntityProjectilePullball.class)
public abstract class ArachnidaProjectileHitRange extends EntitySRPProjectile {
    public ArachnidaProjectileHitRange(World worldIn) {
        super(worldIn);
    }

    @WrapOperation(
            method = "pRanrac",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;")
    )
    private List srpmixins_primArachPullballHitscanSize(World instance, Class classEntity, AxisAlignedBB bb, Operation<List> original){
        return original.call(instance, classEntity, this.getEntityBoundingBox().grow(SRPMixinsConfigHandler.dmgfix.primArachPullBallHit));
    }

    @WrapOperation(
            method = "aRanrac",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/util/List;")
    )
    private List srpmixins_adaArachPullballHitscanSize(World instance, Class classEntity, AxisAlignedBB bb, Operation<List> original){
        return original.call(instance, classEntity, this.getEntityBoundingBox().grow(SRPMixinsConfigHandler.dmgfix.adaArachPullBallHit));
    }
}
