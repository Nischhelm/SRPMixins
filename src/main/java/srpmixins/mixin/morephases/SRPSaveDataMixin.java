package srpmixins.mixin.morephases;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(SRPSaveData.class)
public abstract class SRPSaveDataMixin {
    @Shadow(remap = false) public abstract byte getEvolutionPhase(int id);
    @Shadow(remap = false) public abstract boolean setEvolutionPhase(int id, byte in, boolean override, World worldIn, boolean canChangePhase);
    @Shadow(remap = false) public abstract boolean setTotalKills(int id, int in, boolean plus, World worldIn, boolean canChangePhase);

    @WrapMethod(method = "checkKills", remap = false)
    private boolean srpmixins_checkKills(int dimId, int currPoints, World worldIn, boolean canChangePhase, Operation<Boolean> original){
        byte currPhase = getEvolutionPhase(dimId);

        //Phase up
        if(currPhase >= -1 && currPhase < SRPMixinsConfigHandler.morephases.maxEvolutionPhase && currPoints >= SRPMixinsConfigHandler.morephases.phaseKills[currPhase+1]) {
            if(setEvolutionPhase(dimId, (byte) (currPhase + 1), false, worldIn, canChangePhase)) { //will always be true if increasing
                ParasiteEventEntity.alertAllPlayerDim(worldIn, SRPMixinsConfigHandler.morephases.phaseWarning[currPhase+1], 0);
                return false;
            }
        }
        //Phase down
        else if(currPhase > 0 && currPhase <= SRPMixinsConfigHandler.morephases.maxEvolutionPhase && currPoints < SRPMixinsConfigHandler.morephases.phaseKills[currPhase]) {
            if(setEvolutionPhase(dimId, (byte) (currPhase - 1), false, worldIn, canChangePhase)) {
                ParasiteEventEntity.alertAllPlayerDim(worldIn, "Phase decreased", -7); //TODO: localise
                return true;
            }
        }
        return false;
    }

    @WrapMethod(method = "checkPhase", remap = false)
    private void srpmixins_checkPhase(int dimId, byte phase, World worldIn, Operation<Void> original) {
        if (phase == -1) this.setTotalKills(dimId, -10, false, worldIn, true);
        else if(phase >= 0 && phase <= SRPMixinsConfigHandler.morephases.maxEvolutionPhase)
            this.setTotalKills(dimId, SRPMixinsConfigHandler.morephases.phaseKills[phase], false, worldIn, true);
    }

    @WrapMethod(method = "getDelay", remap = false)
    private int srpmixins_getDelay(byte phase, Operation<Integer> original) {
        if(phase < 0 || phase > SRPMixinsConfigHandler.morephases.maxEvolutionPhase) return 0;
        return SRPMixinsConfigHandler.morephases.phaseDelayTicks[phase];
    }
}
