package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SRPPotions.class)
public class PotionAttributeFix {
    @Unique private static final String atkDmgUUID = "2f887c3e-f25c-4f37-bf9e-e4cca1a0d237";
    @Unique private static final String movSpeedUUID = "f9b296d4-d0b9-4329-b5da-4ff586379b3a";
    @Unique private static final String followRangeUUID = "8f75c318-6702-4087-b2b9-fdb1cdef1bd0";

    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;func_111184_a(Lnet/minecraft/entity/ai/attributes/IAttribute;Ljava/lang/String;DI)Lnet/minecraft/potion/Potion;"),
            remap = false
    )
    //SRP using UUID.randomUUID() fucks shit up massively in the background
    private static Potion fixedUUIDsForPotionAttributes(Potion instance, IAttribute iAttribute, String uuid, double amount, int operation) {
        if (iAttribute == SharedMonsterAttributes.ATTACK_DAMAGE)
            return instance.registerPotionAttributeModifier(iAttribute, atkDmgUUID, amount, operation);
        if (iAttribute == SharedMonsterAttributes.MOVEMENT_SPEED)
            return instance.registerPotionAttributeModifier(iAttribute, movSpeedUUID, amount, operation);
        if (iAttribute == SharedMonsterAttributes.FOLLOW_RANGE)
            return instance.registerPotionAttributeModifier(iAttribute, followRangeUUID, amount, operation);
        return instance.registerPotionAttributeModifier(iAttribute, uuid, amount, operation);
    }
}
