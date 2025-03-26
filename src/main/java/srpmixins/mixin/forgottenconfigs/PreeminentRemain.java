package srpmixins.mixin.forgottenconfigs;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPPreeminent;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPPreeminent.class)
public class PreeminentRemain {
    @ModifyExpressionValue(
            method = "spawnGore",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;pureRemainValue:I"),
            remap = false
    )
    private int srpmixins_usePreeminentRemainValue(int original){
        return SRPConfig.preeminentRemainValue;
    }
}
