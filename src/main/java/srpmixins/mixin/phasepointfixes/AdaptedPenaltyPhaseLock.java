package srpmixins.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPAdapted;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(EntityPAdapted.class)
public abstract class AdaptedPenaltyPhaseLock {

    @Redirect(
            method="func_70623_bb",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setTotalKills(IIZLnet/minecraft/world/World;Z)Z"),
            remap=false
    )
    boolean phaseLockMixin(SRPSaveData data, int id, int in, boolean plus, World worldIn, boolean canChangePhase){
        int startPhase = SRPMixinsConfigHandler.phasepoints.adaptedDespawnPenaltyPhase;
        if(startPhase>-1 && data.getEvolutionPhase(id)<startPhase)
            return false;
        return data.setTotalKills(id,in,plus,worldIn,canChangePhase);
    }
}