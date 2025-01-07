package srpmultiplier.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(SRPSaveData.class)
public abstract class PointReductionPhaseLimit {

    @Shadow(remap = false) public abstract byte getEvolutionPhase(int id);
    @Shadow(remap = false) public abstract int getTotalKills(int id);
    @Shadow(remap = false) public abstract boolean setTotalKills(int id, int in, boolean plus, World worldIn, boolean canChangePhase);

    @Inject(
            method = "setTotalKills",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;func_76185_a()V"),
            remap = false
    )
    void limitPointReduction(int dim, int points, boolean isAdding, World worldIn, boolean canChangePhase, CallbackInfoReturnable<Boolean> cir) {
        if(!SRPMultiplierConfigHandler.server.limitPointReduction) return;
        if (points > 0) return;
        if (!isAdding) return;
        if(canChangePhase) return;
        byte evolutionPhase = this.getEvolutionPhase(dim);
        int pointsMin = getPhaseMinPoints(evolutionPhase);
        int pointsCurr = this.getTotalKills(dim);
        if (pointsCurr < pointsMin) {
            this.setTotalKills(dim,pointsMin,false,worldIn,false);
        }
    }

    @Unique
    public int getPhaseMinPoints(byte evolutionPhase) {
        switch (evolutionPhase) {
            case -2:
                return -10000;
            case -1:
                return -10;
            case 0:
                return 0;
            case 1:
                return SRPConfigSystems.phaseKillsOne;
            case 2:
                return SRPConfigSystems.phaseKillsTwo;
            case 3:
                return SRPConfigSystems.phaseKillsThree;
            case 4:
                return SRPConfigSystems.phaseKillsFour;
            case 5:
                return SRPConfigSystems.phaseKillsFive;
            case 6:
                return SRPConfigSystems.phaseKillsSix;
            case 7:
                return SRPConfigSystems.phaseKillsSeven;
            case 8:
                return SRPConfigSystems.phaseKillsEight;
            case 9:
                return SRPConfigSystems.phaseKillsNine;
            case 10:
                return SRPConfigSystems.phaseKillsTen;
        }
        return 0;
    }
}