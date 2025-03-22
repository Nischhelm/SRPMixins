package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import scala.tools.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

@Mixin(SRPPotions.class)
public abstract class SRPPotionsMixin {
    @Unique private static Map<String, Integer> srpmixins$maxAmpMap = null;
    @Unique private static final String[] srpmixins$emptyList = {"",""};

    @Unique
    private static Integer srpmixins$getMaxAmp(String potionName){
        if(srpmixins$maxAmpMap == null){
            srpmixins$maxAmpMap = new HashMap<>();
            for(String s : SRPConfig.stackablePotionsLimit){
                String[] split = s.split(";");
                srpmixins$maxAmpMap.put(split[0], Integer.parseInt(split[1]));
            }
        }
        return srpmixins$maxAmpMap.get(potionName);
    }

    @Redirect(
            method = "applyStackPotion",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;stackablePotionsLimit:[Ljava/lang/String;", opcode = Opcodes.ARRAYLENGTH, args = "array=length"),
            remap = false
    )
    private static int srpmixins_onlyLoopOnce(String[] array){
        return Math.min(array.length,1);
    }

    @WrapOperation(
            method = "applyStackPotion",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private static String[] srpmixins_dontSplitList(String instance, String regex, Operation<String[]> original, @Local String potionName){
        Integer maxAmp = srpmixins$getMaxAmp(potionName);
        if(maxAmp != null) return new String[]{potionName,""};
        return srpmixins$emptyList;
    }

    @WrapOperation(
            method = "applyStackPotion",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I"),
            remap = false
    )
    private static int srpmixins_dontParseMaxAmp(String instance, Operation<Integer> original, @Local String potionName){
        //return the current dimension to pass the next check for every parsed line
        return srpmixins$getMaxAmp(potionName);
    }
}
