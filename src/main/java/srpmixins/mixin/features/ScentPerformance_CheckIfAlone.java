package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.EntityParasiticScent;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(EntityParasiticScent.class)
public abstract class ScentPerformance_CheckIfAlone extends Entity {
    public ScentPerformance_CheckIfAlone(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = "checkIfScentAlone",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;loadedEntityList:Ljava/util/List;")
    )
    private List<Entity> srpmixins_dontCount(List<Entity> original){
        return this.world.getEntitiesWithinAABB(EntityParasiticScent.class, new AxisAlignedBB(this.getPosition()).grow(SRPConfigSystems.scentSpacing));
    }
}
