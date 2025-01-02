package srpmultiplier.mixin.mobcapmulti;

import com.dhanantry.scapeandrunparasites.network.SRPCommandEvolution;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.SRPMultiplier;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(SRPCommandEvolution.class)
public abstract class SRPCommandEvolutionMixin {

    @Unique private static int dimension;

    @Inject(
            method = "func_184881_a",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I"),
            remap = false
    )
    private void saveDimension(MinecraftServer server, ICommandSender sender, String[] argString, CallbackInfo ci) {
        dimension = sender.getEntityWorld().provider.getDimension();
    }

    @Redirect(
            method = "func_184881_a",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCap:I"),
            remap = false
    )
    private int increaseParasiteMobCap() {
        float dimensionMultiplier = SRPMultiplier.dimensionMobCapMultipliers.getOrDefault(dimension,1.0F);
        if (SRPMultiplierConfigHandler.server.doMultipliers && dimensionMultiplier != 1.0F)
            return (int) (SRPConfig.worldMobCap * dimensionMultiplier);
        return SRPConfig.worldMobCap;
    }

    @Redirect(
            method = "func_184881_a",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldMobCapPlusPlayer:I"),
            remap = false
    )
    private int increaseParasiteMobCapPerPlayer() {
        float dimensionMultiplier = SRPMultiplier.dimensionMobCapMultipliers.getOrDefault(dimension,1.0F);
        if (SRPMultiplierConfigHandler.server.doMultipliers && dimensionMultiplier != 1.0F)
            return (int) (SRPConfig.worldMobCapPlusPlayer * dimensionMultiplier);
        return SRPConfig.worldMobCapPlusPlayer;
    }
}