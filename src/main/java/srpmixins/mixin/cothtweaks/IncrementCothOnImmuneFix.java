package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.potion.SRPEffectBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SRPEffectBase.class)
public class IncrementCothOnImmuneFix {
    @ModifyArg(
            method = "effectCOTH",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;setInteger(Ljava/lang/String;I)V")
    )
    private int dontIncrementImmune(int original) {
        //In both cases srp first does original++, then writes to nbt. so if it was 0 (=immune), it will be 1 now, and we have to put it back to 0
        if (original == 1)
            return 0;
        return original;
    }
}
