package srpmixins.mixin.phasepointfixes.lures;

import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(BlockEvolutionLure.class)
public abstract class CarcassValuePerPhase {
    @Redirect(
            method="onBlockActivated",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setTotalKills(IIZLnet/minecraft/world/World;Z)Z", remap = false)
    )
    public boolean lurePhaseMultiplierMixin(SRPSaveData data, int id, int in, boolean plus, World worldIn, boolean canChangePhase){
        int multi = SRPMixinsConfigProvider.getLurePhaseMultiplier(data.getEvolutionPhase(id));
        return data.setTotalKills(id, in*multi, plus, worldIn, canChangePhase);
    }
}