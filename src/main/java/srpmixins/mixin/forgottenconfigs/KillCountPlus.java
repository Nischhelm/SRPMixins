package srpmixins.mixin.forgottenconfigs;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityParasiteBase.class)
public abstract class KillCountPlus {
    @Shadow(remap = false) protected double killcount;

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;phaseCreated:B", remap = false)
    )
    private byte srpmixins_useAllKillCountConfigs(byte phaseCreated){
        switch (phaseCreated){
            case 9: this.killcount += SRPConfigSystems.phaseKillCountPlusNine; break;
            case 10: this.killcount += SRPConfigSystems.phaseKillCountPlusTen; break;
        }
        return phaseCreated;
    }
}
