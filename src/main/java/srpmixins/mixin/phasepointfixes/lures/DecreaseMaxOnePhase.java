package srpmixins.mixin.phasepointfixes.lures;

import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPConfigProvider;

@Debug(export = true)
@Mixin(value = BlockEvolutionLure.class, priority = 100)
public class DecreaseMaxOnePhase {
    @WrapOperation(
            method = "onBlockActivated",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setTotalKills(IIZLnet/minecraft/world/World;Z)Z", remap = false)
    )
    private boolean srpmixins_onlyDecreaseByOne(
            SRPSaveData instance,
            int id, int addedPoints, boolean plus, World worldIn, boolean canChangePhase,
            Operation<Boolean> original,
            @Local(name = "data") SRPSaveData data
    ) {
        byte evoPhase = data.getEvolutionPhase(worldIn.provider.getDimension());
        //cap addedPoints with max the distance to lowest points of next lower phase (phase5 99% --> phase4 0%)
        if(evoPhase > 1) addedPoints = Math.max(addedPoints, SRPConfigProvider.getPhaseMinPoints((byte)(evoPhase-1)) - data.getTotalKills(worldIn.provider.getDimension()));
        return original.call(instance, id, addedPoints, plus, worldIn, canChangePhase);
    }
}
