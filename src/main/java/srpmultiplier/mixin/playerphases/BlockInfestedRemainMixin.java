package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.block.BlockInfestedRemain;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.util.SRPSaveDataInterface;

import java.util.Random;

@Mixin(BlockInfestedRemain.class)
public abstract class BlockInfestedRemainMixin {

    @Unique BlockPos blockPos;
    @Unique private static SRPSaveData saveData;

    @Inject(
            method = "func_180650_b",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    void getBlockPosMixin(World worldIn, BlockPos pos, IBlockState state, Random rand, CallbackInfo ci) {
        this.blockPos = pos;
    }

    @Redirect(
            method = "func_180650_b",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    public SRPSaveData getPlayerDataMixin(World world) {
        saveData = SRPSaveDataInterface.get(world,null,blockPos);
        return saveData;
    }

    @Redirect(
            method = "func_180650_b",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;getRSchance(Lnet/minecraft/world/World;)D"),
            remap = false
    )
    public double getRSChanceMixin(World world) {
        if (SRPMultiplierConfigHandler.phasepoints.playerPhases) {
            return getRSChance(world);
        } else
            return ParasiteEventEntity.getRSchance(world);
    }

    @Unique
    private static double getRSChance(World world) {
        switch(saveData.getEvolutionPhase(world.provider.getDimension())) {
            case 1: return SRPConfigSystems.reinforcementSystemChanceOne;
            case 2: return SRPConfigSystems.reinforcementSystemChanceTwo;
            case 3: return SRPConfigSystems.reinforcementSystemChanceThree;
            case 4: return SRPConfigSystems.reinforcementSystemChanceFour;
            case 5: return SRPConfigSystems.reinforcementSystemChanceFive;
            case 6: return SRPConfigSystems.reinforcementSystemChanceSix;
            case 7: return SRPConfigSystems.reinforcementSystemChanceSeven;
            case 8: return SRPConfigSystems.reinforcementSystemChanceEight;
            case 9: return SRPConfigSystems.reinforcementSystemChanceNine;
            case 10: return SRPConfigSystems.reinforcementSystemChanceTen;
            default: return 0.0;
        }
    }
}