package srpmultiplier.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(BlockEvolutionLure.class)
public abstract class LureValuePerPhase {

    @Redirect(
            method="func_180639_a",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setTotalKills(IIZLnet/minecraft/world/World;Z)Z"),
            remap=false
    )
    public boolean lurePhaseMultiplierMixin(SRPSaveData data, int id, int in, boolean plus, World worldIn, boolean canChangePhase){
        int multi = 1;
        if(SRPMultiplierConfigHandler.server.variableCarcassValues && SRPMultiplierConfigHandler.server.carcassPhaseMultis.length==11) {
            byte evoPhase = data.getEvolutionPhase(id);
            switch (evoPhase){
                case 0: multi = SRPMultiplierConfigHandler.server.carcassPhaseMultis[0]; break;
                case 1: multi = SRPMultiplierConfigHandler.server.carcassPhaseMultis[1]; break;
                case 2: multi = SRPMultiplierConfigHandler.server.carcassPhaseMultis[2]; break;
                case 3: multi = SRPMultiplierConfigHandler.server.carcassPhaseMultis[3]; break;
                case 4: multi = SRPMultiplierConfigHandler.server.carcassPhaseMultis[4]; break;
                case 5: multi = SRPMultiplierConfigHandler.server.carcassPhaseMultis[5]; break;
                case 6: multi = SRPMultiplierConfigHandler.server.carcassPhaseMultis[6]; break;
                case 7: multi = SRPMultiplierConfigHandler.server.carcassPhaseMultis[7]; break;
                case 8: multi = SRPMultiplierConfigHandler.server.carcassPhaseMultis[8]; break;
                case 9: multi = SRPMultiplierConfigHandler.server.carcassPhaseMultis[9]; break;
                case 10: multi = SRPMultiplierConfigHandler.server.carcassPhaseMultis[10]; break;
            }
        }
        return data.setTotalKills(id, in*multi, plus, worldIn, canChangePhase);
    }
}