package srpmixins.mixin.mobcapmulti;

import com.dhanantry.scapeandrunparasites.entity.EntityParasiticScent;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.providers.DimensionMultiConfigProvider;

@Mixin(EntityParasiticScent.class)
public abstract class EntityParasiticScentMixin extends Entity {
    public EntityParasiticScentMixin(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = {"checkNearby","placeWaves"},
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I"),
            remap = false
    )
    private int srpmixins_increaseParasiteMobCap_checkNearby(int original) {
        int dimension = this.world.provider.getDimension();
        float dimensionMultiplier = DimensionMultiConfigProvider.dimensionMobCapMultipliers.getOrDefault(dimension,1.0F);
        if (dimensionMultiplier != 1.0F)
            return (int) (original * dimensionMultiplier);
        return original;
    }

    @ModifyExpressionValue(
            method = {"checkNearby","placeWaves"},
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I"),
            remap = false
    )
    private int srpmixins_increaseParasiteMobCapPerPlayer_checkNearby(int original) {
        int dimension = this.world.provider.getDimension();
        float dimensionMultiplier = DimensionMultiConfigProvider.dimensionMobCapMultipliers.getOrDefault(dimension,1.0F);
        if (dimensionMultiplier != 1.0F)
            return (int) (original * dimensionMultiplier);
        return original;
    }
}