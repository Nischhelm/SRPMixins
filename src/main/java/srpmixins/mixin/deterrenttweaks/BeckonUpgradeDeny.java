package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAINexusGrow;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.nexus.EntityVenkrolSIV;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityAINexusGrow.class)
public abstract class BeckonUpgradeDeny {
    @WrapOperation(
            method = "upgradeV",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventWorld;canBiomeStillExist(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Z)I"),
            remap = false
    )
    private int blockNewStage4Beckon(World world, BlockPos pos, boolean spread, Operation<Integer> original){
        boolean stage4BeckonIsNearby = !world.getEntitiesWithinAABB(EntityVenkrolSIV.class, new AxisAlignedBB(pos).grow(20)).isEmpty();
        if(stage4BeckonIsNearby) return 999;
        return original.call(world, pos, spread); //ParasiteEventWorld.canBiomeStillExist(world, pos, spread)
    }
}