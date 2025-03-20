package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAIGiveEffectsArea;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(EntityAIGiveEffectsArea.class)
public abstract class EntityAIGiveEffectsAreaMixin {
    @Shadow(remap = false) EntityParasiteBase parent;
    //Maps Parasite Id to Effect List
    @Unique private static Map<Integer, List<PotionEffect>> srpmixins$effectsFromConfig = null;
    @Unique private static final String[] srpmixins$emptyList = {"","",""};

    @Unique
    private static List<PotionEffect> srpmixins$getEffect(int parasiteId){
        if(srpmixins$effectsFromConfig == null){
            srpmixins$effectsFromConfig = new HashMap<>();
            Map<Integer, String[]> toParse = new HashMap<>();
            toParse.put(88, SRPConfigMobs.vestaeffects);
            toParse.put(17, SRPConfigMobs.zetmoEffects);
            toParse.put(56, SRPConfigMobs.zetmoadaptedeffects);

            for(Map.Entry<Integer,String[]> configList : toParse.entrySet()) {
                srpmixins$effectsFromConfig.put(configList.getKey(), new ArrayList<>());
                for (String s : configList.getValue()) {
                    String[] split = s.split(";");
                    Potion potion = Potion.getPotionFromResourceLocation(split[2]);
                    if (potion != null) {
                        int duration = Integer.parseInt(split[0]);
                        int amp = Integer.parseInt(split[1]);
                        srpmixins$effectsFromConfig.get(configList.getKey()).add(new PotionEffect(potion, duration, amp));
                    } else srpmixins$effectsFromConfig.get(configList.getKey()).add(null);
                }
            }
        }

        return srpmixins$effectsFromConfig.get(parasiteId);
    }

    @WrapOperation(
            method = "BuffParasites",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private String[] dontSplitList(String instance, String regex, Operation<String[]> original){
        List<PotionEffect> effects = srpmixins$getEffect(this.parent.getParasiteIDRegister());
        if(effects == null) return original.call(instance, regex);
        return srpmixins$emptyList;
    }

    @WrapOperation(
            method = "BuffParasites",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;getPotionFromResourceLocation(Ljava/lang/String;)Lnet/minecraft/potion/Potion;")
    )
    private Potion dontParsePotion(String s, Operation<Potion> original, @Local(ordinal = 1) int i){
        List<PotionEffect> effects = srpmixins$getEffect(this.parent.getParasiteIDRegister());
        //if potion was not parseable, we don't run the amplifier and duration parse overwrites
        if(effects == null) return original.call(s);
        else if(effects.get(i) == null) return null;
        else return effects.get(i).getPotion();
    }

    @WrapOperation(
            method = "BuffParasites",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 0),
            remap = false
    )
    private int dontParseDuration(String s, Operation<Integer> original, @Local(ordinal = 1) int i){
        List<PotionEffect> effects = srpmixins$getEffect(this.parent.getParasiteIDRegister());
        if(effects == null) return original.call(s);
        else return effects.get(i).getDuration();
    }

    @WrapOperation(
            method = "BuffParasites",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 1),
            remap = false
    )
    private int dontParseAmplifier(String s, Operation<Integer> original, @Local(ordinal = 1) int i){
        List<PotionEffect> effects = srpmixins$getEffect(this.parent.getParasiteIDRegister());
        if(effects == null) return original.call(s);
        else return effects.get(i).getAmplifier();
    }
}
