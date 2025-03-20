package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.entity.projectile.EntityDropPod;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(EntityDropPod.class)
public abstract class EntityDropPodMixin {
    @Unique private static List<PotionEffect> effectsFromConfig = null;

    @Unique
    private static PotionEffect getEffect(int i){
        if(effectsFromConfig == null) {
            effectsFromConfig = new ArrayList<>();

            for (String s : SRPConfigMobs.pod1Effects) {
                String[] split = s.split(";");
                Potion potion = Potion.getPotionFromResourceLocation(split[2]);
                if (potion != null) {
                    int duration = Integer.parseInt(split[0]);
                    int amp = Integer.parseInt(split[1]);
                    effectsFromConfig.add(new PotionEffect(potion, duration, amp));
                } else effectsFromConfig.add(null);
            }
        }

        return effectsFromConfig.get(i);
    }

    @WrapOperation(
            method = "selfExplode",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private String[] dontSplitList(String instance, String regex, Operation<String[]> original){
        return null;
    }

    @WrapOperation(
            method = "selfExplode",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;getPotionFromResourceLocation(Ljava/lang/String;)Lnet/minecraft/potion/Potion;")
    )
    private Potion dontParsePotion(String s, Operation<Potion> original, @Local(ordinal = 1) int i){
        PotionEffect effect = getEffect(i);
        //if potion was not parseable, we don't run the amplifier and duration parse overwrites
        if(effect == null) return null;
        else return effect.getPotion();
    }

    @WrapOperation(
            method = "selfExplode",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 0),
            remap = false
    )
    private int dontParseDuration(String s, Operation<Integer> original, @Local(ordinal = 1) int i){
        return getEffect(i).getDuration();
    }

    @WrapOperation(
            method = "selfExplode",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 1),
            remap = false
    )
    private int dontParseAmplifier(String s, Operation<Integer> original, @Local(ordinal = 1) int i){
        return getEffect(i).getAmplifier();
    }
}
