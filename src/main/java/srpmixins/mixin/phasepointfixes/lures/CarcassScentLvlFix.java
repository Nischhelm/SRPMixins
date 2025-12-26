package srpmixins.mixin.phasepointfixes.lures;

import com.dhanantry.scapeandrunparasites.entity.EntityParasiticScent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPConfigProvider;

@Mixin(EntityParasiticScent.class)
public abstract class CarcassScentLvlFix {
    @Shadow(remap = false) public abstract void increaseDanger(int in, boolean plus);

    @Inject(method = "setScentLevel", at = @At(value = "TAIL"), remap = false)
    private void srpmixins_fixScentLvl(int scentLvl, CallbackInfo ci){
        this.increaseDanger(SRPConfigProvider.getScentPoints(scentLvl), false);
    }
}
