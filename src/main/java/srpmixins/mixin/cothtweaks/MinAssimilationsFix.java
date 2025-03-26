package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SRPSaveData.class)
public abstract class MinAssimilationsFix {
    @ModifyReturnValue(
            method = "getNumberIDDataSpawn",
            at = @At(value = "RETURN"),
            remap = false
    )
    private int srpmixins_fixMinAssimilations(int original) {
        //return 0 if not found, so setting min assimilation configs to 0 will actually work
        if(original == -1) return 0;
        return original;
    }
}