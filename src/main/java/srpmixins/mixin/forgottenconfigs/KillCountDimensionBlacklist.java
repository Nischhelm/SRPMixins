package srpmixins.mixin.forgottenconfigs;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityParasiteBase.class)
public abstract class KillCountDimensionBlacklist extends EntityMob {
    public KillCountDimensionBlacklist(World worldIn) {
        super(worldIn);
    }

    @ModifyExpressionValue(
            method = "onLivingUpdate",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;useEvolution:Z", remap = false)
    )
    private boolean srpmixins_usePreeminentRemainValue(boolean original){
        boolean isInList = false;
        for(int whitelistedDim : SRPConfigSystems.blackListedDimensionsEPP) //its a whitelist
            if (whitelistedDim == this.dimension) {
                isInList = true;
                break;
            }

        if(!isInList)
            return false;
        return original;
    }
}
