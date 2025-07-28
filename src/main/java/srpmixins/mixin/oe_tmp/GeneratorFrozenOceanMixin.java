package srpmixins.mixin.oe_tmp;

import com.sirsquidly.oe.world.GeneratorFrozenOcean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(GeneratorFrozenOcean.class)
public abstract class GeneratorFrozenOceanMixin {
    @ModifyConstant(
            method = "spawnFrozenOcean",
            constant = @Constant(doubleValue = 0.0, ordinal = 0),
            remap = false
    )
    private double srpmixins_reduceFrozenOceanCommonness(double constant){
        return 0.5;
    }
}
