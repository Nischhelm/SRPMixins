package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashMap;
import java.util.Map;

@Mixin(ParasiteEventEntity.class)
public abstract class ParasiteEventEntityMixin_Conversion {
    @Unique private static Map<String, String> srpmixins$cothConversions = null;
    @Unique private static Map<String, String> srpmixins$hijackConversions = null;
    @Unique private static String srpmixins$getConvertedEntity(String victim, boolean isHijacking){
        if(!isHijacking && srpmixins$cothConversions == null){
            srpmixins$cothConversions = new HashMap<>();
            for(String s : SRPConfigSystems.COTHVictimParasite){
                String[] split = s.split(";");
                srpmixins$cothConversions.put(split[0], split[1]);
            }
        }
        else if(isHijacking && srpmixins$hijackConversions == null){
            srpmixins$hijackConversions = new HashMap<>();
            for(String s : SRPConfigSystems.HIJACKVictimParasite){
                String[] split = s.split(";");
                srpmixins$hijackConversions.put(split[0], split[1]);
            }
        }
        return isHijacking ? srpmixins$hijackConversions.get(victim) : srpmixins$cothConversions.get(victim);
    }

    @WrapOperation(
            method = "convertEntity",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private static String[] srpmixins_dontSplitInAssim(String instance, String regex, Operation<String[]> original, @Local(ordinal = 0) String currTargetId) {
        String convertedId = srpmixins$getConvertedEntity(currTargetId, false);
        if(convertedId != null) return new String[]{currTargetId, convertedId}; //will succeed on first try

        return new String[]{null, null}; //will fail on next test and spawn insider
    }

    @WrapOperation(
            method = "convertEntityFeral",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private static String[] srpmixins_dontSplitInFeral(String instance, String regex, Operation<String[]> original, @Local(ordinal = 0) String currTargetId) {
        String convertedId = srpmixins$getConvertedEntity(currTargetId, false);
        if(convertedId != null) return new String[]{currTargetId, convertedId}; //will succeed on first try

        return new String[]{null, null}; //will fail on next test and spawn insider
    }

    @ModifyVariable(
            method = "hijackEntity",
            at = @At(value = "HEAD"),
            argsOnly = true,
            remap = false
    )
    private static String[] srpmixins_makeListSingleEntry(String[] list){
        //hijackEntity doesn't have an early return after succeeding the conversion, so we force it like that
        return new String[]{""};
    }

    @WrapOperation(
            method = "hijackEntity",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private static String[] srpmixins_dontSplitInHijack(String instance, String regex, Operation<String[]> original, @Local(ordinal = 0) String currTargetId) {
        String convertedId = srpmixins$getConvertedEntity(currTargetId, true);
        if(convertedId != null) return new String[]{currTargetId, convertedId}; //will succeed on first try

        return new String[]{null, null}; //will fail on next test and return
    }



}
