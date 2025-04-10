package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigWorld;
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

@Mixin(EntityParasiteBase.class)
public abstract class EntityParasiteBaseMixin_NodeBonus {
    @Unique private static List<PotionEffect> srpmixins$configEffects = null;
    @Unique private static final String[] srpmixins$emptyList = {"","",""};

    @Unique
    private static PotionEffect srpmixins$getEffect(int i){
        if(srpmixins$configEffects == null){
            srpmixins$configEffects = new ArrayList<>();
            for(String s : SRPConfigWorld.potionEffectForNodes){
                String[] split = s.split(";");
                if(split.length < 3) {
                    SRPMixins.LOGGER.warn("SRPMixins unable to parse SRP node potion effect, expected pattern: node lvl; modid:potionname; amplifier, provided was: {}", s);
                    continue;
                }
                try {
                    int nodeLimit = Integer.parseInt(split[0].trim());
                    Potion potion = Potion.getPotionFromResourceLocation(split[1].trim());
                    int amp = Integer.parseInt(split[2].trim());
                    //Save nodeLimit in unused duration
                    if (potion != null) srpmixins$configEffects.add(new PotionEffect(potion, nodeLimit, amp));
                    else {
                        srpmixins$configEffects.add(null);
                        SRPMixins.LOGGER.warn("SRPMixins unable to parse SRP node potion effect, expected numbers in first and last line entry, provided was: {}", s);
                    }
                } catch (Exception e) {
                    srpmixins$configEffects.add(null);
                    SRPMixins.LOGGER.warn("SRPMixins unable to parse SRP node potion effect, potion doesn't exist, provided was: {}", s);
                }
            }
        }
        return srpmixins$configEffects.get(i);
    }

    @WrapOperation(
            method = "setNodeBonus",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private String[] srpmixins_dontSplitList(String instance, String regex, Operation<String[]> original){
        return srpmixins$emptyList;
    }

    @WrapOperation(
            method = "setNodeBonus",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 0),
            remap = false
    )
    private int srpmixins_dontParseNodeLvl(String s, Operation<Integer> original, @Local(ordinal = 2) int i){
        PotionEffect effect = srpmixins$getEffect(i);
        if(effect == null) return Integer.MAX_VALUE;
        return effect.getDuration();
    }

    @WrapOperation(
            method = "setNodeBonus",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 1),
            remap = false
    )
    private int srpmixins_dontParseAmplifier(String s, Operation<Integer> original, @Local(ordinal = 2) int i){
        PotionEffect effect = srpmixins$getEffect(i);
        if(effect == null) return 0;
        return effect.getAmplifier();
    }

    @WrapOperation(
            method = "setNodeBonus",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;getPotionFromResourceLocation(Ljava/lang/String;)Lnet/minecraft/potion/Potion;")
    )
    private Potion srpmixins_dontParsePotion(String s, Operation<Potion> original, @Local(ordinal = 2) int i){
        PotionEffect effect = srpmixins$getEffect(i);
        if(effect == null) return null;
        else return effect.getPotion();
    }
}
