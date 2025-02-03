package srpmixins.mixin.phasepointfixes.lures;

import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(BlockEvolutionLure.class)
public abstract class CarcassValuePerPhase {

    @Redirect(
            method="onBlockActivated",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setTotalKills(IIZLnet/minecraft/world/World;Z)Z"),
            remap = false
    )
    public boolean lurePhaseMultiplierMixin(SRPSaveData data, int id, int in, boolean plus, World worldIn, boolean canChangePhase){
        int multi = 1;
        if(SRPMixinsConfigHandler.lures.variableCarcassValues && SRPMixinsConfigHandler.lures.carcassPhaseMultis.length==11) {
            byte evoPhase = data.getEvolutionPhase(id);
            switch (evoPhase){
                case 0: multi = SRPMixinsConfigHandler.lures.carcassPhaseMultis[0]; break;
                case 1: multi = SRPMixinsConfigHandler.lures.carcassPhaseMultis[1]; break;
                case 2: multi = SRPMixinsConfigHandler.lures.carcassPhaseMultis[2]; break;
                case 3: multi = SRPMixinsConfigHandler.lures.carcassPhaseMultis[3]; break;
                case 4: multi = SRPMixinsConfigHandler.lures.carcassPhaseMultis[4]; break;
                case 5: multi = SRPMixinsConfigHandler.lures.carcassPhaseMultis[5]; break;
                case 6: multi = SRPMixinsConfigHandler.lures.carcassPhaseMultis[6]; break;
                case 7: multi = SRPMixinsConfigHandler.lures.carcassPhaseMultis[7]; break;
                case 8: multi = SRPMixinsConfigHandler.lures.carcassPhaseMultis[8]; break;
                case 9: multi = SRPMixinsConfigHandler.lures.carcassPhaseMultis[9]; break;
                case 10: multi = SRPMixinsConfigHandler.lures.carcassPhaseMultis[10]; break;
            }
        }
        return data.setTotalKills(id, in*multi, plus, worldIn, canChangePhase);
    }
}