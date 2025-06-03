package srpmixins.mixin.mobcapmulti;

import com.dhanantry.scapeandrunparasites.network.SRPCommandEvolution;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.command.ICommandSender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(SRPCommandEvolution.class)
public abstract class SRPCommandEvolutionMixin {

    @ModifyExpressionValue(
            method = "execute",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I", remap = false)
    )
    private int srpmixins_increaseParasiteMobCapPerPlayer(int original, @Share(value = "dimension") LocalIntRef dim, @Local(argsOnly = true) ICommandSender sender) {
        dim.set(sender.getEntityWorld().provider.getDimension());
        float dimensionMultiplier = SRPMixinsConfigProvider.dimensionMobCapMultipliers.getOrDefault(dim.get(),1.0F);
        if (dimensionMultiplier != 1.0F)
            return (int) (original * dimensionMultiplier);
        return original;
    }

    @ModifyExpressionValue(
            method = "execute",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I", remap = false)
    )
    private int srpmixins_increaseParasiteMobCap(int original, @Share(value = "dimension") LocalIntRef dim) {
        float dimensionMultiplier = SRPMixinsConfigProvider.dimensionMobCapMultipliers.getOrDefault(dim.get(),1.0F);
        if (dimensionMultiplier != 1.0F)
            return (int) (original * dimensionMultiplier);
        return original;
    }
}