package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import scala.tools.asm.Opcodes;
import srpmixins.util.Pair;

import java.util.HashMap;
import java.util.Map;

@Mixin(SRPEventHandlerBus.class)
public abstract class SRPEventHandlerBusMixin_CamouflageItems {
    @Unique private static Map<String, Pair<Double, Integer>> srpmixins$camouflageItems = null;
    @Unique
    private static Pair<Double, Integer> srpmixins$getCamouflageProperties(String itemName){
        if(srpmixins$camouflageItems == null){
            srpmixins$camouflageItems = new HashMap<>();
            for (String s : SRPConfigSystems.COTHItemPrevent){
                String[] split = s.split(";");
                String item = split[0];
                double chance = Double.parseDouble(split[1]);
                int duration = Integer.parseInt(split[2]);
                srpmixins$camouflageItems.put(item, new Pair<>(chance, duration));
            }
        }

        return srpmixins$camouflageItems.getOrDefault(itemName, null);
    }

    @Redirect(
            method = "itemEntity",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;COTHItemPrevent:[Ljava/lang/String;", opcode = Opcodes.ARRAYLENGTH, args = "array=length"),
            remap = false
    )
    private int srpmixins_onlyLoopOnce(String[] array){
        return Math.min(array.length, 1);
    }

    @WrapOperation(
            method = "itemEntity",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private String[] srpmixins_dontSplit(String instance, String regex, Operation<String[]> original, @Local String itemName){
        if(srpmixins$getCamouflageProperties(itemName) != null)
            return new String[]{itemName};
        else
            return new String[]{"SRPMixins:TryingNotToBeIntrusive"};
    }

    @WrapOperation(
            method = "itemEntity",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I"),
            remap = false
    )
    private int srpmixins_dontParseDuration(String s, Operation<Integer> original, @Local String itemName){
        return srpmixins$getCamouflageProperties(itemName).getRight();
    }

    @WrapOperation(
            method = "itemEntity",
            at = @At(value = "INVOKE", target = "Ljava/lang/Double;parseDouble(Ljava/lang/String;)D"),
            remap = false
    )
    private double srpmixins_dontParseChance(String s, Operation<Double> original, @Local String itemName){
        return srpmixins$getCamouflageProperties(itemName).getLeft();
    }
}
