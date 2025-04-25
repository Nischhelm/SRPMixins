package srpmixins.mixin.simenderman;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.head.EntityInfEndermanHead;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityInfEndermanHead.class)
public abstract class SimEndermanHeadTpTargetDistance extends EntityLivingBase {

    public SimEndermanHeadTpTargetDistance(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "setCoordTarget",
            at = @At(value = "HEAD"),
            remap = false
    )
    private void srpmixins_changeSimmermanTPTargetPosition(double x, double y, double z, CallbackInfo ci, @Local(argsOnly = true, ordinal = 0) LocalDoubleRef xRef, @Local(argsOnly = true, ordinal = 2) LocalDoubleRef zRef) {
        double radius = SRPMixinsConfigHandler.simmermen.simmermenTpDistanceFromTargetMin + this.rand.nextDouble() * (SRPMixinsConfigHandler.simmermen.simmermenTpDistanceFromTargetMax - SRPMixinsConfigHandler.simmermen.simmermenTpDistanceFromTargetMin);
        double angle = this.rand.nextDouble() * Math.PI * 2.;

        xRef.set(x + radius * Math.cos(angle));
        zRef.set(z + radius * Math.sin(angle));
    }
}