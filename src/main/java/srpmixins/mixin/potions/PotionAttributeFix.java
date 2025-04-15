package srpmixins.mixin.potions;

import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SRPPotions.class)
public class PotionAttributeFix {
    @Unique private static final String srpmixins$atkDmgUUID = "2f887c3e-f25c-4f37-bf9e-e4cca1a0d237";
    @Unique private static final String srpmixins$movSpeedUUID = "f9b296d4-d0b9-4329-b5da-4ff586379b3a";
    @Unique private static final String srpmixins$followRangeUUID = "8f75c318-6702-4087-b2b9-fdb1cdef1bd0";

    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;registerPotionAttributeModifier(Lnet/minecraft/entity/ai/attributes/IAttribute;Ljava/lang/String;DI)Lnet/minecraft/potion/Potion;", ordinal = 0)
    )
    private static String srpmixins_fixedUUIDsForPotionAttributes_atkDmg(String uuid) {
        return srpmixins$atkDmgUUID;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/potion/SRPEffectBase;registerPotionAttributeModifier(Lnet/minecraft/entity/ai/attributes/IAttribute;Ljava/lang/String;DI)Lnet/minecraft/potion/Potion;", ordinal = 0)
    )
    private static String srpmixins_fixedUUIDsForPotionAttributes_movSpeed(String uuid) {
        return srpmixins$movSpeedUUID;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/potion/SRPEffectBase;registerPotionAttributeModifier(Lnet/minecraft/entity/ai/attributes/IAttribute;Ljava/lang/String;DI)Lnet/minecraft/potion/Potion;", ordinal = 1)
    )
    private static String srpmixins_fixedUUIDsForPotionAttributes_flwRange(String uuid) {
        return srpmixins$followRangeUUID;
    }
}
