package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.entity.tile.TileEntityDod;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityDod.class)
public abstract class InfiniteDispatcherFix extends TileEntity {
    @Inject(
            method = "update",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)Z", shift = At.Shift.AFTER)
    )
    private void srpmixins_fixInfiniteDispatchers(CallbackInfo ci){
        invalidate();
    }
}
