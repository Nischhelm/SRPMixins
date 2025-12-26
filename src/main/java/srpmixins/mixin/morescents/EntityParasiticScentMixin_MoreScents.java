package srpmixins.mixin.morescents;

import com.dhanantry.scapeandrunparasites.entity.EntityParasiticScent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.providers.MoreScentsConfigProvider;
import srpmixins.config.providers.SRPMobConfigProvider;

@Mixin(EntityParasiticScent.class)
public abstract class EntityParasiticScentMixin_MoreScents {
    @Shadow(remap = false) private int dangerToUs;
    @Shadow(remap = false) private byte scentLevel;
    @Shadow(remap = false) private int maxmob;
    @Shadow(remap = false) private int minmob;
    @Shadow(remap = false) private int maxwave;
    @Shadow(remap = false) private int minwave;

    // After SRP computes its default wave/mob numbers, override from More Scents if enabled
    @Inject(method = "updateScentOLevel", at = @At("HEAD"), cancellable = true, remap = false)
    private void srpmixins_overrideWaveSizes(CallbackInfo ci) {
        for (byte scentPhase = SRPMixinsConfigHandler.morescents.maxScentLevel; scentPhase >= 0; scentPhase--) {
            if (this.dangerToUs >= SRPMixinsConfigHandler.morescents.pointsRequired[scentPhase]) {
                this.scentLevel = scentPhase;
                this.maxmob = MoreScentsConfigProvider.mobCountMax.get(scentPhase);
                this.minmob = MoreScentsConfigProvider.mobCountMin.get(scentPhase);
                this.maxwave = MoreScentsConfigProvider.waveCountMax.get(scentPhase);
                this.minwave = MoreScentsConfigProvider.waveCountMin.get(scentPhase);

                ci.cancel();
                return;
            }
        }
    }
}
