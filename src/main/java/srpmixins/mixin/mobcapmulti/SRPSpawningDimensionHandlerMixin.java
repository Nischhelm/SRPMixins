package srpmixins.mixin.mobcapmulti;

import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class SRPSpawningDimensionHandlerMixin {

    @ModifyExpressionValue(
            method = "onSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I"),
            remap = false
    )
    private static int srpmixins_increaseParasiteMobCapPerPlayer(int original, @Share(value = "dimension") LocalIntRef dim, @Local(argsOnly = true) ICommandSender sender) {
        dim.set(sender.getEntityWorld().provider.getDimension());
        float dimensionMultiplier = SRPMixinsConfigProvider.dimensionMobCapMultipliers.getOrDefault(dim.get(),1.0F);
        if (dimensionMultiplier != 1.0F)
            return (int) (original * dimensionMultiplier);
        return original;
    }

    @ModifyExpressionValue(
            method = "onSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldSpawningMobCap:I"),
            remap = false
    )
    private static int srpmixins_increaseParasiteMobCap(int original, @Share(value = "dimension") LocalIntRef dim) {
        float dimensionMultiplier = SRPMixinsConfigProvider.dimensionMobCapMultipliers.getOrDefault(dim.get(),1.0F);
        if (dimensionMultiplier != 1.0F)
            return (int) (original * dimensionMultiplier);
        return original;
    }

    //TODO: gnat cap + water cap?
}