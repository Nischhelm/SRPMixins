package srpmixins.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import srpmixins.config.SRPConfigProvider;

@Mixin(SRPSaveData.class)
public abstract class PointReductionPhaseLimit {
    @Shadow(remap = false) public abstract byte getEvolutionPhase(int id);

    @ModifyArg(
            method = "setTotalKills",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;checkKills(IILnet/minecraft/world/World;Z)Z"),
            remap = false,
            index = 1
    )
    private int srpmixins_limitPointReduction(
            int points,
            @Local(argsOnly = true, ordinal = 0) int dim,
            @Local(argsOnly = true, ordinal = 0) boolean isAdding,
            @Local(argsOnly = true, ordinal = 1) boolean canChangePhase
    ) {
        //Default behavior if config disabled, increasing points, setting points, reducing points with carcasses
        if (points > 0 || !isAdding || canChangePhase)
            return points;
        return srpmixins$getLimitedPoints(dim, points);
    }

    //Doesn't work with ModifyArg bc second argument is E, not int
    @ModifyArgs(
            method = "setTotalKills",
            at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;set(ILjava/lang/Object;)Ljava/lang/Object;", ordinal = 0),
            remap = false
    )
    private void srpmixins_limitPointReduction(Args args,
                                     @Local(argsOnly = true, ordinal = 0) int dim,
                                     @Local(argsOnly = true, ordinal = 1) int points,
                                     @Local(argsOnly = true, ordinal = 0) boolean isAdding,
                                     @Local(argsOnly = true, ordinal = 1) boolean canChangePhase
    ){
        //Default behavior if config disabled, increasing points, setting points, reducing points with carcasses
        if (points > 0 || !isAdding || canChangePhase)
            return;
        args.set(1, srpmixins$getLimitedPoints(dim, args.get(1)));
    }

    @Unique
    private int srpmixins$getLimitedPoints(int dim, int currPoints){
        byte evolutionPhase = this.getEvolutionPhase(dim);
        int pointsMin = SRPConfigProvider.getPhaseMinPoints(evolutionPhase);
        //Limit to at least pointsMin
        return Math.max(currPoints, pointsMin);
    }
}