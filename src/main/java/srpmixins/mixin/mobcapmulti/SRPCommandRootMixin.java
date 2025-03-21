package srpmixins.mixin.mobcapmulti;

import com.dhanantry.scapeandrunparasites.network.SRPCommandRoot;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(SRPCommandRoot.class)
public abstract class SRPCommandRootMixin {
    @Unique private static int srpmixins$dimension;

    @Inject(
            method = "execute",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I", remap = false)
    )
    private void srpmixins_saveDimension(MinecraftServer server, ICommandSender sender, String[] argString, CallbackInfo ci) {
        srpmixins$dimension = sender.getEntityWorld().provider.getDimension();
    }

    @ModifyExpressionValue(
            method = "execute",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I", remap = false)
    )
    private int srpmixins_increaseParasiteMobCap(int original) {
        float dimensionMultiplier = SRPMixinsConfigProvider.dimensionMobCapMultipliers.getOrDefault(srpmixins$dimension,1.0F);
        if (dimensionMultiplier != 1.0F)
            return (int) (original * dimensionMultiplier);
        return original;
    }

    @ModifyExpressionValue(
            method = "execute",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I", remap = false)
    )
    private int srpmixins_increaseParasiteMobCapPerPlayer(int original) {
        float dimensionMultiplier = SRPMixinsConfigProvider.dimensionMobCapMultipliers.getOrDefault(srpmixins$dimension,1.0F);
        if (dimensionMultiplier != 1.0F)
            return (int) (original * dimensionMultiplier);
        return original;
    }
}