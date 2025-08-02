package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.item.ItemNodeColonyCompass;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.List;

@Mixin(ItemNodeColonyCompass.class)
public abstract class NodeCompassFix {
    @WrapOperation(
            method = "onUpdate",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;getHeartPocition(Lnet/minecraft/util/math/BlockPos;I)I", remap = false)
    )
    private int srpmixins_skipUselessCheck(SRPWorldData instance, BlockPos ageDistance, int currentage, Operation<BlockPos> original){
        return 0; //this doesnt need any check its always useless (returns blockpos null if player stands exactly on a stage 1 node, like what
    }

    @WrapOperation(
            method = "onUpdate",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPWorldData;nearestHeartAgePosition(Lnet/minecraft/util/math/BlockPos;I)Lnet/minecraft/util/math/BlockPos;", remap = false)
    )
    private BlockPos srpmixins_fixNodeCompass(SRPWorldData instance, BlockPos currentPos, int zeroAnyway, Operation<BlockPos> original){
        List<Integer> x = instance.getNodes("x");
        if(x.isEmpty()) return null;
        if(SRPMixinsConfigHandler.various.nodeCompassMaxDist.length != 3) return original.call(instance, currentPos, zeroAnyway);

        //This is so stupid
        List<Integer> y = instance.getNodes("y");
        List<Integer> z = instance.getNodes("z");
        List<Integer> a = instance.getNodes("a");

        double minDist = Integer.MAX_VALUE;
        BlockPos nearestNodePos = null;

        for(int i = 0; i < x.size(); i++) {
            BlockPos nodePos = new BlockPos(x.get(i), y.get(i), z.get(i));
            double currDist = Math.sqrt(nodePos.distanceSq(currentPos));
            int stage = ((SRPWorldDataAccessor) instance).invokeConvertDayToAgeNode(zeroAnyway,a.get(i));
            if(currDist <= SRPMixinsConfigHandler.various.nodeCompassMaxDist[stage-1] && currDist < minDist) {
                minDist = currDist;
                nearestNodePos = nodePos;
            }
        }

        return nearestNodePos;
    }
}
