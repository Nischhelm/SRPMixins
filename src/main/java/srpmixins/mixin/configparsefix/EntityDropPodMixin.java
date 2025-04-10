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
import srpmixins.SRPMixins;

import java.util.ArrayList;
import java.util.List;

@Mixin(EntityDropPod.class)
public abstract class EntityDropPodMixin {
    @Unique private static List<PotionEffect> srpmixins$effectsFromConfig = null;
    @Unique private static final String[] srpmixins$emptyList = {"","",""};

    @Unique
    private static PotionEffect srpmixins$getEffect(int i){
        if(srpmixins$effectsFromConfig == null) {
            srpmixins$effectsFromConfig = new ArrayList<>();

            for (String s : SRPConfigMobs.pod1Effects) {
                String[] split = s.split(";");
                if(split.length < 3) {
                    SRPMixins.LOGGER.warn("SRPMixins unable to parse SRP Drop Pod potion effect, expected pattern: duration; amplifier; modid:potionname, provided was: {}", s);
                    continue;
                }
                try {
                    Potion potion = Potion.getPotionFromResourceLocation(split[2].trim());
                    if (potion != null) {
                        int duration = Integer.parseInt(split[0].trim());
                        int amp = Integer.parseInt(split[1].trim());
                        srpmixins$effectsFromConfig.add(new PotionEffect(potion, duration, amp));
                    } else{
                        srpmixins$effectsFromConfig.add(null);
                        SRPMixins.LOGGER.warn("SRPMixins unable to parse SRP Drop Pod potion effect, potion doesn't exist, provided was: {}", s);
                    }
                } catch (Exception e){
                    srpmixins$effectsFromConfig.add(null);
                    SRPMixins.LOGGER.warn("SRPMixins unable to parse SRP Drop Pod potion effect, expected numbers until the last semicolon, provided was: {}", s);
                }
            }
        }

        return srpmixins$effectsFromConfig.get(i);
    }

    @WrapOperation(
            method = "selfExplode",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private String[] dontSplitList(String instance, String regex, Operation<String[]> original){
        return srpmixins$emptyList;
    }

    @WrapOperation(
            method = "selfExplode",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;getPotionFromResourceLocation(Ljava/lang/String;)Lnet/minecraft/potion/Potion;")
    )
    private Potion dontParsePotion(String s, Operation<Potion> original, @Local(ordinal = 1) int i){
        PotionEffect effect = srpmixins$getEffect(i);
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
        return srpmixins$getEffect(i).getDuration();
    }

    @WrapOperation(
            method = "selfExplode",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 1),
            remap = false
    )
    private int dontParseAmplifier(String s, Operation<Integer> original, @Local(ordinal = 1) int i){
        return srpmixins$getEffect(i).getAmplifier();
    }
}
