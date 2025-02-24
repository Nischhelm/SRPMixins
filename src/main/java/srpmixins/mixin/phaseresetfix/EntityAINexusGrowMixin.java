package srpmixins.mixin.phaseresetfix;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAINexusGrow;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityAINexusGrow.class)
public abstract class EntityAINexusGrowMixin {
    @Shadow(remap = false) @Final private EntityPStationaryArchitect parent;

    @ModifyExpressionValue(
            method = "checkPhase",
            at = @At(value="FIELD",target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;useEvolution:Z"),
            remap = false
    )
    private boolean fixPhaseReset(boolean original){
        return !parent.getEntityWorld().isRemote && original;
    }
}