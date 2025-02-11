package srpmixins.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPConfigProvider;

@Mixin(SRPSaveData.class)
public abstract class PointReductionPhaseLimit {
    @Shadow(remap = false) public abstract byte getEvolutionPhase(int id);
    @Shadow(remap = false) protected abstract boolean checkKills(int id, int in, World worldIn, boolean canChangePhase);

    @Redirect(
            method = "setTotalKills",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;checkKills(IILnet/minecraft/world/World;Z)Z"),
            remap = false
    )
    private boolean limitPointReduction(SRPSaveData instance, int dim, int points, World worldIn, boolean canChangePhase, @Local(argsOnly = true, ordinal = 0) boolean isAdding) {
        //Default behavior if config disabled, increasing points, setting points, reducing points with carcasses
        if (!SRPMixinsConfigHandler.phasepoints.limitPointReduction || points > 0 || !isAdding || canChangePhase)
            return checkKills(dim, points, worldIn, canChangePhase);
        return checkKills(dim, getLimitedPoints(dim, points), worldIn, false);
    }

    @ModifyArgs(
            method = "setTotalKills",
            at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;set(ILjava/lang/Object;)Ljava/lang/Object;"),
            remap = false
    )
    private void limitPointReduction(Args args,
                                     @Local(argsOnly = true, ordinal = 0) int dim,
                                     @Local(argsOnly = true, ordinal = 1) int points,
                                     @Local(argsOnly = true, ordinal = 0) boolean isAdding,
                                     @Local(argsOnly = true, ordinal = 1) boolean canChangePhase
    ){
        //Default behavior if config disabled, increasing points, setting points, reducing points with carcasses
        if (!SRPMixinsConfigHandler.phasepoints.limitPointReduction || points > 0 || !isAdding || canChangePhase)
            return;
        args.set(1, getLimitedPoints(dim, args.get(1)));
    }

    @Unique
    private int getLimitedPoints(int dim, int currPoints){
        byte evolutionPhase = this.getEvolutionPhase(dim);
        int pointsMin = SRPConfigProvider.getPhaseMinPoints(evolutionPhase);
        //Limit to at least pointsMin
        return Math.max(currPoints, pointsMin);
    }
}