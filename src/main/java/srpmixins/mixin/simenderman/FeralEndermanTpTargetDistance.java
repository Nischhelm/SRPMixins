package srpmixins.mixin.simenderman;

import com.dhanantry.scapeandrunparasites.entity.monster.feral.EntityFerEnderman;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityFerEnderman.class)
public abstract class FeralEndermanTpTargetDistance extends EntityLivingBase {
    @Shadow(remap = false) protected abstract boolean teleportTo(double x, double y, double z);

    public FeralEndermanTpTargetDistance(World worldIn) {
        super(worldIn);
    }

    @WrapMethod(method = "teleportToPos", remap = false)
    protected boolean srpmixins_teleportToPos(double x, double y, double z, double dis, Operation<Boolean> original) {
        double radius = MathHelper.nextDouble(this.rand, SRPMixinsConfigHandler.simmermen.fermenTpDistanceFromTargetMin, SRPMixinsConfigHandler.simmermen.fermenTpDistanceFromTargetMax);
        double angle = this.rand.nextDouble() * Math.PI * 2.;

        double xNew = x + radius * Math.cos(angle);
        double zNew = z + radius * Math.sin(angle);

        return this.teleportTo(xNew, y, zNew);
    }
}