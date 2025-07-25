package srpmixins.mixin.modcompat.srpextra.customphases;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import energon.srpextra.events.EntityInject;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(EntityInject.spawnUtil.class)
public abstract class EntityInjectMixin {
    @Redirect(
            method = "test",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;")
    )
    private SRPSaveData srpmixins_useCustomPhases(World world, @Local(argsOnly = true) BlockPos pos){
        return SRPSaveDataInterface.get(world, null, pos);
    }
}
