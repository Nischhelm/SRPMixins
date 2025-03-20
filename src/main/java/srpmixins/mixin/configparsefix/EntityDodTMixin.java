package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityDodT;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(EntityDodT.class)
public abstract class EntityDodTMixin {
    @Shadow(remap = false) private int stage;
    //Maps Dispatcher Stage to Effect List
    @Unique private static Map<Integer, List<PotionEffect>> effectsFromConfig = null;

    @Unique
    private static List<PotionEffect> getEffect(int stage){
        if(effectsFromConfig == null){
            effectsFromConfig = new HashMap<>();
            Map<Integer, String[]> toParse = new HashMap<>();
            toParse.put(1, SRPConfigMobs.dodsiEffects);
            toParse.put(2, SRPConfigMobs.dodsiiEffects);
            toParse.put(3, SRPConfigMobs.dodsiiiEffects);
            toParse.put(4, SRPConfigMobs.dodsivEffects);

            for(Map.Entry<Integer,String[]> configList : toParse.entrySet()) {
                for (String s : configList.getValue()) {
                    String[] split = s.split(";");
                    Potion potion = Potion.getPotionFromResourceLocation(split[1]);
                    if (potion != null) {
                        int duration = Integer.parseInt(split[0]);
                        int amp = Integer.parseInt(split[2]);
                        effectsFromConfig.get(configList.getKey()).add(new PotionEffect(potion, duration, amp));
                    } else effectsFromConfig.get(configList.getKey()).add(null);
                }
            }
        }

        return effectsFromConfig.get(stage);
    }

    @WrapOperation(
            method = "applyEffectsDod",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private String[] dontSplitList(String instance, String regex, Operation<String[]> original){
        List<PotionEffect> effects = getEffect(this.stage);
        if(effects == null) return original.call(instance, regex);
        return null;
    }

    @WrapOperation(
            method = "applyEffectsDod",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 0),
            remap = false
    )
    private int dontParseDuration(String s, Operation<Integer> original, @Local(ordinal = 0) int i){
        List<PotionEffect> effects = getEffect(this.stage);
        if(effects == null) return original.call(s);
        return effects.get(i).getDuration();
    }

    @WrapOperation(
            method = "applyEffectsDod",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 1),
            remap = false
    )
    private int dontParseAmplifier(String s, Operation<Integer> original, @Local(ordinal = 0) int i){
        List<PotionEffect> effects = getEffect(this.stage);
        if(effects == null) return original.call(s);
        return effects.get(i).getAmplifier();
    }

    @WrapOperation(
            method = "applyEffectsDod",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;getPotionFromResourceLocation(Ljava/lang/String;)Lnet/minecraft/potion/Potion;")
    )
    private Potion dontParsePotion(String s, Operation<Potion> original, @Local(ordinal = 0) int i){
        List<PotionEffect> effects = getEffect(this.stage);
        //if there's no list for the current dispatcher stage for whatever reason, we pass back to original handling
        if(effects == null) return original.call(s);
        return effects.get(i).getPotion();
    }
}
