package srpmixins.mixin.phaseresetfix.srpsavedata;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPAdapted;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPAdapted.class)
public abstract class EntityPAdaptedMixin {
    @WrapOperation(
            method = "despawnEntity",
            at = @At(value= "INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private SRPSaveData srpmixins_fixPhaseReset(World world, Operation<SRPSaveData> original){
        return world.isRemote ? null : original.call(world);
    }

    @WrapOperation(
            method = "despawnEntity",
            at = @At(value= "INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setTotalKills(IIZLnet/minecraft/world/World;Z)Z"),
            remap = false
    )
    private boolean srpmixins_fixPhaseReset(SRPSaveData instance, int id, int in, boolean plus, World worldIn, boolean canChangePhase, Operation<Boolean> original){
        return !worldIn.isRemote && original.call(instance, id, in, plus, worldIn, canChangePhase);
    }
}