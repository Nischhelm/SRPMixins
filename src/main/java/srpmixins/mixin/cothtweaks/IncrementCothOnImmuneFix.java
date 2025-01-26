package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.potion.SRPEffectBase;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(SRPEffectBase.class)
public class IncrementCothOnImmuneFix {
    @Redirect(
            method = "effectCOTH",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;func_74768_a(Ljava/lang/String;I)V"),
            remap = false
    )
    void dontIncrementImmune(NBTTagCompound instance, String s, int i) {
        if (i == 1 && SRPMixinsConfigHandler.coth.fixSrpCothImmunity) {
            instance.setInteger(s, 0);
            return;
        }
        instance.setInteger(s, i);
    }
}
