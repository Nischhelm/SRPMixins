package srpmixins.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPAdapted;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityPAdapted.class)
public abstract class AdaptedPenaltyPhaseLock extends Entity {
    public AdaptedPenaltyPhaseLock(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = "despawnEntity",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;valueEvolutionDespawn:I", remap = false)
    )
    private int srpmixins_phaseLockMixin(int original, @Local SRPSaveData data) {
        int startPhase = SRPMixinsConfigHandler.phasepoints.adaptedDespawnPenaltyPhase;
        if (data.getEvolutionPhase(this.world.provider.getDimension()) < startPhase)
            return 0;

        //Default behavior
        return original;
    }
}