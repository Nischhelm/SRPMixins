package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAIGiveEffectsArea;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigProvider;

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
                    if(split.length < 3) {
                        SRPMixins.LOGGER.warn("SRPMixins unable to parse SRP AOE potion effect for parasite id {}, expected pattern: duration; amplifier; modid:potionname, provided was: {}", configList.getKey(), s);
                        continue;
                    }
                    try {
                        Potion potion = Potion.getPotionFromResourceLocation(split[2].trim());
                        if (potion != null) {
                            int duration = Integer.parseInt(split[0].trim());
                            int amp = Integer.parseInt(split[1].trim());
                            srpmixins$effectsFromConfig.get(configList.getKey()).add(new PotionEffect(potion, duration, amp));
                        } else{
                            srpmixins$effectsFromConfig.get(configList.getKey()).add(null);
                            SRPMixins.LOGGER.warn("SRPMixins unable to parse SRP AOE potion effect for parasite id {}, potion doesn't exist, provided was: {}", configList.getKey(), s);
                        }
                    } catch (Exception e){
                        srpmixins$effectsFromConfig.get(configList.getKey()).add(null);
                        SRPMixins.LOGGER.warn("SRPMixins unable to parse SRP AOE potion effect for parasite id {}, expected numbers until the last semicolon, provided was: {}", configList.getKey(), s);
                    }
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
    private String[] srpmixins_dontSplitList(String instance, String regex, Operation<String[]> original){
        List<PotionEffect> effects = srpmixins$getEffect(this.parent.getParasiteIDRegister());
        if(effects == null) return original.call(instance, regex);
        return srpmixins$emptyList;
    }

    @WrapOperation(
            method = "BuffParasites",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;getPotionFromResourceLocation(Ljava/lang/String;)Lnet/minecraft/potion/Potion;")
    )
    private Potion srpmixins_dontParsePotion(String s, Operation<Potion> original, @Local(ordinal = 1) int i, @Share("index") LocalIntRef index){
        index.set(i);
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
    private int srpmixins_dontParseDuration(String s, Operation<Integer> original, @Share("index") LocalIntRef index){
        List<PotionEffect> effects = srpmixins$getEffect(this.parent.getParasiteIDRegister());
        if(effects == null) return original.call(s);
        else return effects.get(index.get()).getDuration();
    }

    @WrapOperation(
            method = "BuffParasites",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 1),
            remap = false
    )
    private int srpmixins_dontParseAmplifier(String s, Operation<Integer> original, @Share("index") LocalIntRef index){
        List<PotionEffect> effects = srpmixins$getEffect(this.parent.getParasiteIDRegister());
        if(effects == null) return original.call(s);
        else return effects.get(index.get()).getAmplifier();
    }
}
