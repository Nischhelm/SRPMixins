package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityJinjo;
import com.dhanantry.scapeandrunparasites.entity.projectile.EntityBomb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;

import javax.annotation.Nullable;

@Mixin(EntityBomb.class)
public class BombFix {
    @Shadow(remap = false) @Nullable private EntityParasiteBase tntPlacedBy;

    @ModifyVariable(
            method = "setDamage",
            at = @At(value = "HEAD"),
            remap = false,
            argsOnly = true
    )
    float fixProjDmg(float in){
        if(SRPMixinsConfigHandler.dmgfix.doDamageFixes) {
            if(tntPlacedBy == null) return in;
            if(tntPlacedBy instanceof EntityJinjo) return in;
            int dimension = tntPlacedBy.dimension;
            return in * SRPMixinsConfigProvider.dimensionDmgMultipliers.getOrDefault(dimension,1F);
        }
        return in;
    }
}
