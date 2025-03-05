package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.potion.SRPEffectBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SRPEffectBase.class)
public abstract class SRPEffectBaseMixin {
    @Inject(
            method = "effectCOTH",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private void secureImmunity(EntityLivingBase entity, int amplifier, CallbackInfo ci){
        NBTTagCompound tags = entity.getEntityData();
        if (tags.hasKey("srpcothimmunity") && tags.getInteger("srpcothimmunity") == 0){
            entity.removeActivePotionEffect(SRPPotions.COTH_E);
            ci.cancel();
        }
    }
}