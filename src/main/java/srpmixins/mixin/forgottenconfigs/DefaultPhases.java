package srpmixins.mixin.forgottenconfigs;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import srpmixins.config.SRPConfigProvider;

@Mixin(SRPSaveData.class)
public abstract class DefaultPhases {
    @WrapOperation(
            method = "createData",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setEvolutionPhase(IBZLnet/minecraft/world/World;Z)Z"),
            remap = false
    )
    private static boolean srpmixins_fixDefault_Gaining_Loss_noPlayerPhases(SRPSaveData data, int dim, byte phase, boolean override, World world, boolean canChangePhase, Operation<Boolean> original) {
        //This would be overwritten later in createData for phase -2, so don't bother
        if(phase != -2) {
            //Set canGain
            data.setGaining(true, dim);
            //is in blacklist
            boolean dimIsInList = SRPConfigProvider.dimensionCanGainPointsBlacklist.contains(dim);
            //if (found and blacklist) or (not found and whitelist)
            if (SRPConfigSystems.evolutionDimGainInverted != dimIsInList)
                data.setGaining(false, dim);

            //Set canLoss (should be cantLose)
            data.setLoss(false, dim);
            //is in blacklist
            dimIsInList = SRPConfigProvider.dimensionCantLosePointsBlacklist.contains(dim);
            //if (found and blacklist) or (not found and whitelist)
            if (SRPConfigSystems.evolutionDimLossInverted != dimIsInList)
                data.setLoss(true, dim);
        }

        //Default behavior SRPSaveData.setEvolutionPhase
        return original.call(data, dim, phase, override, world, canChangePhase);
    }

    @ModifyConstant(
            method = "addDim",
            constant = @Constant(intValue = 0, ordinal = 1),
            remap = false
    )
    private static int srpmixins_fixDefaultPhase(int constant){
        return SRPConfigSystems.defaultEvoPhase;
    }

    @ModifyConstant(
            method = "addDim",
            constant = @Constant(intValue = 0, ordinal = 3),
            remap = false
    )
    private static int srpmixins_fixDefaultPoints(int constant){
        return SRPConfigSystems.defaultEvoPoints;
    }
}