package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPMalleable;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import scala.tools.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

@Mixin(EntityPMalleable.class)
public abstract class EntityPMalleableMixin extends Entity {
    public EntityPMalleableMixin(World worldIn) {
        super(worldIn);
    }

    @Unique private static Map<Integer, Double> srpmixins$configValues = null;
    @Unique
    private static Double srpmixins$getAdaptationBonus(int dimension) {
        if(srpmixins$configValues == null){
            srpmixins$configValues = new HashMap<>();
            for(String s : SRPConfigSystems.maximumStageList){
                String[] split = s.split(";");
                int dim = Integer.parseInt(split[0]);
                double adaptationBonus = Double.parseDouble(split[1]);
                srpmixins$configValues.put(dim,adaptationBonus);
            }
        }
        return srpmixins$configValues.get(dimension);
    }

    @Redirect(
            method = "getChanceLearn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;adaptationDimStrong:[Ljava/lang/String;", opcode = Opcodes.ARRAYLENGTH, args = "array=length"),
            remap = false
    )
    private int srpmixins_overwriteLoop(String[] array, @Local(name = "bonus") LocalDoubleRef bonus){
        Double adaptationBonus = srpmixins$getAdaptationBonus(this.world.provider.getDimension());
        if(adaptationBonus != null) bonus.set(adaptationBonus);
        //Don't run original code
        return 0;
    }
}
