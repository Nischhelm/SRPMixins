package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ParasiteEventWorld.class)
public class CircularInfestationArea {
    @WrapOperation(
            method = "canInfestBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/AxisAlignedBB;grow(DDD)Lnet/minecraft/util/math/AxisAlignedBB;"),
            remap = false
    )
    private static AxisAlignedBB srpmixins_growRangeByOne(AxisAlignedBB instance, double x, double y, double z, Operation<AxisAlignedBB> original) {
        return original.call(instance, x+1, y+1, z+1);
    }

    @Unique private static double srpmixins$distClosest = 1000D;

    @ModifyExpressionValue(
            method = "canInfestBlock",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPStationaryArchitect;getPosition()Lnet/minecraft/util/math/BlockPos;")
    )
    private static BlockPos srpmixins_saveClosestDistance(BlockPos original, @Local(argsOnly = true) BlockPos infestationPos) {
        srpmixins$distClosest = original.distanceSq(infestationPos);
        return original;
    }

    @Inject(
            method = "canInfestBlock",
            at = @At(value = "CONSTANT", args = "intValue=2", ordinal = 1),
            remap = false,
            cancellable = true
    )
    private static void srpmixins_returnIfDistanceHigh(World worldIn, BlockPos pos, Random rand, int stage, boolean fromVenkrol, CallbackInfo ci, @Local(argsOnly = true) BlockPos infestationPos, @Local(name = "range") int range) {
        boolean shouldReturn = srpmixins$distClosest > range*range;
        srpmixins$distClosest = 1000D;
        if(shouldReturn) ci.cancel();
    }
}