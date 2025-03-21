package srpmixins.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPAdapted;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPAdapted.class)
public abstract class AdaptedPenaltyInstantDespawn extends Entity {

    public AdaptedPenaltyInstantDespawn(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = "despawnEntity",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;valueEvolutionDespawn:I", remap = false)
    )
    private int srpmixins_phaseLockMixin(int original, @Local SRPSaveData data) {
        //Fix for fast penalty points from adapteds despawning instantly after spawn
        if(this.ticksExisted < 5) return 0;

        //Default behavior
        return original;
    }
}