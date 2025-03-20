package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import scala.tools.asm.Opcodes;
import srpmixins.util.Triple;

import java.util.HashMap;
import java.util.Map;

@Mixin(SRPEventHandlerBus.class)
public abstract class SRPEventHandlerBusMixin_ParasiteGriefing {
    @Unique private static Map<String, Triple<Float, Integer, Integer>> srpmixins$mobGriefTasks = null;
    @Unique private static final String[] srpmixins$emptyList = {"","","",""};
    @Unique
    private static Triple<Float, Integer, Integer> srpmixins$getGriefingProperties(String itemName){
        if(srpmixins$mobGriefTasks == null){
            srpmixins$mobGriefTasks = new HashMap<>();
            for (String s : SRPConfig.parasiteGriefing){
                String[] split = s.split(";");
                String mob = split[0];
                float hardness = Float.parseFloat(split[1]);
                int cooldown = Integer.parseInt(split[2]);
                int range = Integer.parseInt(split[3]);
                srpmixins$mobGriefTasks.put(mob, new Triple<>(hardness, cooldown, range));
            }
        }

        return srpmixins$mobGriefTasks.getOrDefault(itemName, null);
    }

    @Redirect(
            method = "setNewParasiteTask",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;parasiteGriefing:[Ljava/lang/String;", opcode = Opcodes.ARRAYLENGTH, args = "array=length"),
            remap = false
    )
    private int srpmixins_onlyLoopOnce(String[] array){
        return Math.min(array.length, 1);
    }

    @WrapOperation(
            method = "setNewParasiteTask",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private String[] srpmixins_dontSplit(String instance, String regex, Operation<String[]> original, @Local(argsOnly = true) String mobName){
        if(srpmixins$getGriefingProperties(mobName) != null)
            return new String[]{mobName,"","",""};
        else
            return srpmixins$emptyList;
    }

    @WrapOperation(
            method = "setNewParasiteTask",
            at = @At(value = "INVOKE", target = "Ljava/lang/Float;parseFloat(Ljava/lang/String;)F"),
            remap = false
    )
    private float srpmixins_dontParseHardness(String s, Operation<Float> original, @Local(argsOnly = true) String mobName){
        return srpmixins$getGriefingProperties(mobName).getLeft();
    }

    @WrapOperation(
            method = "setNewParasiteTask",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 0),
            remap = false
    )
    private int srpmixins_dontParseRange_1(String s, Operation<Integer> original, @Local(argsOnly = true) String mobName){
        return srpmixins$getGriefingProperties(mobName).getRight();
    }

    @WrapOperation(
            method = "setNewParasiteTask",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 1),
            remap = false
    )
    private int srpmixins_dontParseRange_2(String s, Operation<Integer> original, @Local(argsOnly = true) String mobName){
        return srpmixins$getGriefingProperties(mobName).getRight();
    }

    @WrapOperation(
            method = "setNewParasiteTask",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 2),
            remap = false
    )
    private int srpmixins_dontParseCooldown(String s, Operation<Integer> original, @Local(argsOnly = true) String mobName){
        return srpmixins$getGriefingProperties(mobName).getMiddle();
    }
}
