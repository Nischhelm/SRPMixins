package srpmixins.mixin.forgottenconfigs;

import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityTonro;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityTonro.class)
public class KyphosisXPValue {
    @ModifyExpressionValue(
            method = "<init>",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/SRPAttributes;XP_ADAPTED:I", remap = false)
    )
    private int srpmixins_usePreeminentRemainValue(int original){
        return SRPConfig.turretXPValue;
    }
}
