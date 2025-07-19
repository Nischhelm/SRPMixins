package srpmixins.mixin.mobcapmulti;

import com.dhanantry.scapeandrunparasites.network.SRPCommandEvolution;
import com.dhanantry.scapeandrunparasites.network.SRPCommandRoot;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.command.ICommandSender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.providers.DimensionMultiConfigProvider;

@Mixin(value = {SRPCommandEvolution.class, SRPCommandRoot.class})
public abstract class SRPCommandsMixin {
    @ModifyExpressionValue(
            method = "execute",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I", remap = false)
    )
    private int srpmixins_increaseParasiteMobCap(int original, @Local(argsOnly = true) ICommandSender sender) {
        float dimensionMultiplier = DimensionMultiConfigProvider.dimensionMobCapMultipliers.getOrDefault(sender.getEntityWorld().provider.getDimension(),1.0F);
        if (dimensionMultiplier != 1.0F)
            return (int) (original * dimensionMultiplier);
        return original;
    }

    @ModifyExpressionValue(
            method = "execute",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I", remap = false)
    )
    private int srpmixins_increaseParasiteMobCapPerPlayer(int original, @Local(argsOnly = true) ICommandSender sender) {
        float dimensionMultiplier = DimensionMultiConfigProvider.dimensionMobCapMultipliers.getOrDefault(sender.getEntityWorld().provider.getDimension(),1.0F);
        if (dimensionMultiplier != 1.0F)
            return (int) (original * dimensionMultiplier);
        return original;
    }
}