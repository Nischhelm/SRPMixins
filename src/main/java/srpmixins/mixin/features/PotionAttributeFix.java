package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.potion.SRPEffectBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.SRPMixins;

@Mixin(SRPPotions.class)
public class PotionAttributeFix {
    @Unique private static final String atkDmgUUID = "2f887c3e-f25c-4f37-bf9e-e4cca1a0d237";
    @Unique private static final String movSpeedUUID = "f9b296d4-d0b9-4329-b5da-4ff586379b3a";
    @Unique private static final String followRangeUUID = "8f75c318-6702-4087-b2b9-fdb1cdef1bd0";

    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;registerPotionAttributeModifier(Lnet/minecraft/entity/ai/attributes/IAttribute;Ljava/lang/String;DI)Lnet/minecraft/potion/Potion;", ordinal = 0)
    )
    private static Potion fixedUUIDsForPotionAttributes(Potion instance, IAttribute iAttribute, String uuid, double amount, int operation) {
        SRPMixins.LOGGER.info("SRPMixins atk attr mixin {}", iAttribute.getName());
        return instance.registerPotionAttributeModifier(iAttribute, atkDmgUUID, amount, operation);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/potion/SRPEffectBase;registerPotionAttributeModifier(Lnet/minecraft/entity/ai/attributes/IAttribute;Ljava/lang/String;DI)Lnet/minecraft/potion/Potion;", ordinal = 0)
    )
    private static Potion fixedUUIDsForPotionAttributes(SRPEffectBase instance, IAttribute iAttribute, String uuid, double amount, int operation) {
        SRPMixins.LOGGER.info("SRPMixins atk speed mixin {}", iAttribute.getName());
        return instance.registerPotionAttributeModifier(iAttribute, movSpeedUUID, amount, operation);
    }

    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/potion/SRPEffectBase;registerPotionAttributeModifier(Lnet/minecraft/entity/ai/attributes/IAttribute;Ljava/lang/String;DI)Lnet/minecraft/potion/Potion;", ordinal = 1)
    )
    private static Potion fixedUUIDsForPotionAttributes3(SRPEffectBase instance, IAttribute iAttribute, String uuid, double amount, int operation) {
        SRPMixins.LOGGER.info("SRPMixins followrange mixin {}", iAttribute.getName());
        return instance.registerPotionAttributeModifier(iAttribute, followRangeUUID, amount, operation);
    }
}
