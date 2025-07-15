package srpmixins.mixin.spawning.entirefix;

import com.dhanantry.scapeandrunparasites.init.SRPBiomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.handlers.SpawnPotentialsHandler;

@Mixin(SRPBiomes.class)
public class SRPBiomesMixin {
    @Inject(
            method = "clearMobSpawnList",
            at = @At("HEAD"),
            remap = false
    )
    private static void srpmixins_clearParaBiomeSpawnList(CallbackInfo ci){
        SpawnPotentialsHandler.biomeSpawns.clear();
    }
}
