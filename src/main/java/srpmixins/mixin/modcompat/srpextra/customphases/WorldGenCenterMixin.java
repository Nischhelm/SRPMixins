package srpmixins.mixin.modcompat.srpextra.customphases;

import com.llamalad7.mixinextras.sugar.Local;
import energon.srpextra.world.WorldGenCenter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(WorldGenCenter.class)
public abstract class WorldGenCenterMixin {
    @ModifyVariable(
            method = "generate",
            name = "phase",
            at = @At(value = "FIELD", target = "Lenergon/srpextra/world/WorldGenCenter;tierTwo:I", ordinal = 0),
            remap = false
    )
    private int srpmixins_useCustomPhases(
            int value,
            @Local(name = "pos") BlockPos pos,
            @Local(argsOnly = true) World world,
            @Local(name = "dimId") int dimId
    ){
        return SRPSaveDataInterface.get(world, null, pos).getEvolutionPhase(dimId);
    }
}
