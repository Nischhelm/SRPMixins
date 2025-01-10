package srpmultiplier.mixin.mobcapmulti;

import com.dhanantry.scapeandrunparasites.entity.EntityParasiticScent;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmultiplier.SRPMultiplier;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(EntityParasiticScent.class)
public abstract class EntityParasiticScentMixin extends Entity {

    public EntityParasiticScentMixin(World worldIn) {
        super(worldIn);
    }

    @Redirect(
            method = "checkNearby",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I"),
            remap = false
    )
    private int increaseParasiteMobCap_checkNearby() {
        int dimension = this.world.provider.getDimension();
        float dimensionMultiplier = SRPMultiplier.dimensionMobCapMultipliers.getOrDefault(dimension,1.0F);
        if (SRPMultiplierConfigHandler.dimension.doMultipliers && dimensionMultiplier != 1.0F)
            return (int) (SRPConfig.worldMobCap * dimensionMultiplier);
        return SRPConfig.worldMobCap;
    }

    @Redirect(
            method = "checkNearby",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I"),
            remap = false
    )
    private int increaseParasiteMobCapPerPlayer_checkNearby() {
        int dimension = this.world.provider.getDimension();
        float dimensionMultiplier = SRPMultiplier.dimensionMobCapMultipliers.getOrDefault(dimension,1.0F);
        if (SRPMultiplierConfigHandler.dimension.doMultipliers && dimensionMultiplier != 1.0F)
            return (int) (SRPConfig.worldMobCapPlusPlayer * dimensionMultiplier);
        return SRPConfig.worldMobCapPlusPlayer;
    }

    @Redirect(
            method = "placeWaves",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I"),
            remap = false
    )
    private int increaseParasiteMobCap_placeWaves() {
        int dimension = this.world.provider.getDimension();
        float dimensionMultiplier = SRPMultiplier.dimensionMobCapMultipliers.getOrDefault(dimension,1.0F);
        if (SRPMultiplierConfigHandler.dimension.doMultipliers && dimensionMultiplier != 1.0F)
            return (int) (SRPConfig.worldMobCap * dimensionMultiplier);
        return SRPConfig.worldMobCap;
    }

    @Redirect(
            method = "placeWaves",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I"),
            remap = false
    )
    private int increaseParasiteMobCapPerPlayer_placeWaves() {
        int dimension = this.world.provider.getDimension();
        float dimensionMultiplier = SRPMultiplier.dimensionMobCapMultipliers.getOrDefault(dimension,1.0F);
        if (SRPMultiplierConfigHandler.dimension.doMultipliers && dimensionMultiplier != 1.0F)
            return (int) (SRPConfig.worldMobCapPlusPlayer * dimensionMultiplier);
        return SRPConfig.worldMobCapPlusPlayer;
    }
}