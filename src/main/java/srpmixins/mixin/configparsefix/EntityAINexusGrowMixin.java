package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAINexusGrow;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import scala.tools.asm.Opcodes;
import srpmixins.SRPMixins;

import java.util.HashMap;
import java.util.Map;

@Mixin(EntityAINexusGrow.class)
public abstract class EntityAINexusGrowMixin {
    @Shadow(remap = false) @Final private EntityPStationaryArchitect parent;
    @Unique private static Map<Integer, Integer> srpmixins$configValues = null;
    @Unique private static final String[] srpmixins$emptyList = {"",""};
    @Unique
    private static Integer srpmixins$getMaxStage(int dimension) {
        if(srpmixins$configValues == null){
            srpmixins$configValues = new HashMap<>();
            for(String s : SRPConfigSystems.maximumStageList){
                String[] split = s.split(";");
                if(split.length < 2){
                    SRPMixins.LOGGER.warn("SRPMixins unable to parse SRP Reinforcement System Maximum Stage entry, expected pattern: dimension; max stage, provided was: {}", s);
                    continue;
                }
                try {
                    int dim = Integer.parseInt(split[0].trim());
                    int maxStage = Integer.parseInt(split[1].trim());
                    srpmixins$configValues.put(dim,maxStage);
                } catch (Exception e){
                    SRPMixins.LOGGER.warn("SRPMixins unable to parse SRP Reinforcement System Maximum Stage entry, expected semicolon separated numbers, provided was: {}", s);
                }
            }
        }
        return srpmixins$configValues.get(dimension);
    }

    @Redirect(
            method = "<init>(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPStationaryArchitect;I)V",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;maximumStageList:[Ljava/lang/String;", opcode = Opcodes.ARRAYLENGTH, args = "array=length"),
            remap = false
    )
    private int srpmixins_loopOnlyOnce(String[] array){
        return Math.min(array.length,1);
    }

    @WrapOperation(
            method = "<init>(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPStationaryArchitect;I)V",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;"),
            remap = false
    )
    private String[] srpmixins_dontSplitList(String instance, String regex, Operation<String[]> original){
        return srpmixins$emptyList;
    }

    @WrapOperation(
            method = "<init>(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPStationaryArchitect;I)V",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;length()I"),
            remap = false
    )
    private int srpmixins_dontCheckLength(String instance, Operation<Integer> original){
        return 1;
    }

    @WrapOperation(
            method = "<init>(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPStationaryArchitect;I)V",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 0),
            remap = false
    )
    private int srpmixins_dontParseDimension(String instance, Operation<Integer> original){
        //return the current dimension to pass the next check for every parsed line
        return this.parent.world.provider.getDimension();
    }

    @WrapOperation(
            method = "<init>(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityPStationaryArchitect;I)V",
            at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 1),
            remap = false
    )
    private int srpmixins_dontParseStage(String instance, Operation<Integer> original){
        Integer maxStage = srpmixins$getMaxStage(this.parent.world.provider.getDimension());
        if(maxStage == null) return -1;
        else return maxStage;
    }
}
