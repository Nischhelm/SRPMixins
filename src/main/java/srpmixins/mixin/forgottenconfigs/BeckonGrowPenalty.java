package srpmixins.mixin.forgottenconfigs;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAINexusGrow;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Mixin(value = EntityAINexusGrow.class, priority = 900)
public abstract class BeckonGrowPenalty {
    @Shadow(remap = false) private boolean canGrow;
    @Shadow(remap = false) private byte venkrolCurrentStage;
    @Unique private static Map<Integer, Map<Byte, Double>> srpmixins$beckonUpgradeChances = null;
    @Unique
    private static double srpmixins$getUpgradeChance(int beckonStage, byte phase) {
        if(srpmixins$beckonUpgradeChances == null) {
            srpmixins$beckonUpgradeChances = new HashMap<>();
            for (int stage = 1; stage <= 3; stage++) srpmixins$beckonUpgradeChances.put(stage, new HashMap<>());

            // bruh
            srpmixins$beckonUpgradeChances.get(1).put((byte) 1, SRPConfigSystems.beckonStageIGrowPenaltyOne);
            srpmixins$beckonUpgradeChances.get(1).put((byte) 2, SRPConfigSystems.beckonStageIGrowPenaltyTwo);
            srpmixins$beckonUpgradeChances.get(1).put((byte) 3, SRPConfigSystems.beckonStageIGrowPenaltyThree);
            srpmixins$beckonUpgradeChances.get(1).put((byte) 4, SRPConfigSystems.beckonStageIGrowPenaltyFour);
            srpmixins$beckonUpgradeChances.get(1).put((byte) 5, SRPConfigSystems.beckonStageIGrowPenaltyFive);
            srpmixins$beckonUpgradeChances.get(1).put((byte) 6, SRPConfigSystems.beckonStageIGrowPenaltySix);
            srpmixins$beckonUpgradeChances.get(1).put((byte) 7, SRPConfigSystems.beckonStageIGrowPenaltySeven);
            srpmixins$beckonUpgradeChances.get(1).put((byte) 8, SRPConfigSystems.beckonStageIGrowPenaltyEight);
            srpmixins$beckonUpgradeChances.get(1).put((byte) 9, SRPConfigSystems.beckonStageIGrowPenaltyNine);
            srpmixins$beckonUpgradeChances.get(1).put((byte) 10, SRPConfigSystems.beckonStageIGrowPenaltyTen);

            srpmixins$beckonUpgradeChances.get(2).put((byte) 1, SRPConfigSystems.beckonStageIIGrowPenaltyOne);
            srpmixins$beckonUpgradeChances.get(2).put((byte) 2, SRPConfigSystems.beckonStageIIGrowPenaltyTwo);
            srpmixins$beckonUpgradeChances.get(2).put((byte) 3, SRPConfigSystems.beckonStageIIGrowPenaltyThree);
            srpmixins$beckonUpgradeChances.get(2).put((byte) 4, SRPConfigSystems.beckonStageIIGrowPenaltyFour);
            srpmixins$beckonUpgradeChances.get(2).put((byte) 5, SRPConfigSystems.beckonStageIIGrowPenaltyFive);
            srpmixins$beckonUpgradeChances.get(2).put((byte) 6, SRPConfigSystems.beckonStageIIGrowPenaltySix);
            srpmixins$beckonUpgradeChances.get(2).put((byte) 7, SRPConfigSystems.beckonStageIIGrowPenaltySeven);
            srpmixins$beckonUpgradeChances.get(2).put((byte) 8, SRPConfigSystems.beckonStageIIGrowPenaltyEight);
            srpmixins$beckonUpgradeChances.get(2).put((byte) 9, SRPConfigSystems.beckonStageIIGrowPenaltyNine);
            srpmixins$beckonUpgradeChances.get(2).put((byte) 10, SRPConfigSystems.beckonStageIIGrowPenaltyTen);

            srpmixins$beckonUpgradeChances.get(3).put((byte) 1, SRPConfigSystems.beckonStageIIIGrowPenaltyOne);
            srpmixins$beckonUpgradeChances.get(3).put((byte) 2, SRPConfigSystems.beckonStageIIIGrowPenaltyTwo);
            srpmixins$beckonUpgradeChances.get(3).put((byte) 3, SRPConfigSystems.beckonStageIIIGrowPenaltyThree);
            srpmixins$beckonUpgradeChances.get(3).put((byte) 4, SRPConfigSystems.beckonStageIIIGrowPenaltyFour);
            srpmixins$beckonUpgradeChances.get(3).put((byte) 5, SRPConfigSystems.beckonStageIIIGrowPenaltyFive);
            srpmixins$beckonUpgradeChances.get(3).put((byte) 6, SRPConfigSystems.beckonStageIIIGrowPenaltySix);
            srpmixins$beckonUpgradeChances.get(3).put((byte) 7, SRPConfigSystems.beckonStageIIIGrowPenaltySeven);
            srpmixins$beckonUpgradeChances.get(3).put((byte) 8, SRPConfigSystems.beckonStageIIIGrowPenaltyEight);
            srpmixins$beckonUpgradeChances.get(3).put((byte) 9, SRPConfigSystems.beckonStageIIIGrowPenaltyNine);
            srpmixins$beckonUpgradeChances.get(3).put((byte) 10, SRPConfigSystems.beckonStageIIIGrowPenaltyTen);
        }
        return srpmixins$beckonUpgradeChances.getOrDefault(beckonStage, Collections.emptyMap()).getOrDefault(phase, 1.0);
    }

    @ModifyExpressionValue(
            method = "checkPhase",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getEvolutionPhase(I)B"),
            remap = false
    )
    private byte srpmixins_useAllBeckonGrowPenaltyConfigs(byte phase, @Cancellable CallbackInfo ci, @Local Random rand) {
        this.canGrow = phase > 0 && rand.nextDouble() >= srpmixins$getUpgradeChance(this.venkrolCurrentStage, phase);
        ci.cancel();
        return phase;
    }
}