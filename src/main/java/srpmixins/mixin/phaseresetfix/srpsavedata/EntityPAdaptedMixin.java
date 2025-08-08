package srpmixins.mixin.phaseresetfix.srpsavedata;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPAdapted;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPAdapted.class)
public abstract class EntityPAdaptedMixin extends Entity {
    public EntityPAdaptedMixin(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = "despawnEntity",
            at = @At(value= "FIELD",target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;useEvolution:Z", remap = false)
    )
    private boolean srpmixins_fixPhaseReset(boolean original){
        return !this.world.isRemote && original;
    }
}