package srpmixins.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPAdapted;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(EntityPAdapted.class)
public abstract class AdaptedPenaltyPhaseLock extends Entity {

    public AdaptedPenaltyPhaseLock(World worldIn) {
        super(worldIn);
    }

    @Redirect(
            method="func_70623_bb",
            at=@At(value= "FIELD",target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;valueEvolutionDespawn:I"),
            remap=false
    )
    private int phaseLockMixin(@Local SRPSaveData data){
        int startPhase = SRPMixinsConfigHandler.phasepoints.adaptedDespawnPenaltyPhase;
        if(startPhase>-1 && data.getEvolutionPhase(this.world.provider.getDimension())<startPhase)
            return 0;
        return SRPConfigSystems.valueEvolutionDespawn;
    }
}