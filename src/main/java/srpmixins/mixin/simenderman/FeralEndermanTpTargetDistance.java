package srpmixins.mixin.simenderman;

import com.dhanantry.scapeandrunparasites.entity.monster.feral.EntityFerEnderman;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityFerEnderman.class)
public abstract class FeralEndermanTpTargetDistance extends EntityLivingBase {

    public FeralEndermanTpTargetDistance(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "teleportToPos",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/monster/feral/EntityFerEnderman;getAttackTarget()Lnet/minecraft/entity/EntityLivingBase;", ordinal = 0)
    )
    private void srpmixins_changeSimmermanTPTargetPosition(double x, double y, double z, double dis, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 4) LocalDoubleRef xRef, @Local(ordinal = 5) LocalDoubleRef yRef, @Local(ordinal = 6) LocalDoubleRef zRef) {
        double radius = SRPMixinsConfigHandler.simmermen.simmermenTpDistanceFromTargetMin + this.rand.nextDouble() * (SRPMixinsConfigHandler.simmermen.simmermenTpDistanceFromTargetMax - SRPMixinsConfigHandler.simmermen.simmermenTpDistanceFromTargetMin);
        double angle = this.rand.nextDouble() * Math.PI * 2.;

        xRef.set(x + radius * Math.cos(angle));
        yRef.set(y);
        zRef.set(z + radius * Math.sin(angle));
    }

    @WrapOperation(
            method = "teleportToPos",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getDistance(DDD)D")
    )
    private double srpmixins_returnTrue(EntityLivingBase instance, double x, double y, double Z, Operation<Double> original){
        return 11.0;
    }
}