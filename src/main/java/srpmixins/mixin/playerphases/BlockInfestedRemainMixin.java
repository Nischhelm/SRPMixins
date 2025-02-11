package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.block.BlockInfestedRemain;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.config.SRPConfigProvider;
import srpmixins.util.SRPSaveDataInterface;

@Mixin(BlockInfestedRemain.class)
public abstract class BlockInfestedRemainMixin {
    @Redirect(
            method = "updateTick",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;getRSchance(Lnet/minecraft/world/World;)D", remap = false)
    )
    public double getRSChanceMixin(World world, @Local(argsOnly = true) BlockPos blockPos) {
        //ParasiteEventEntity.getRSChance(world) only gets called from here, but can't mixin there bc i don't have the blockpos there
        SRPSaveData saveData = SRPSaveDataInterface.get(world,null,blockPos);
        byte evoPhase = saveData.getEvolutionPhase(world.provider.getDimension());
        return SRPConfigProvider.reinForcementChancePerPhase.get(MathHelper.clamp(evoPhase, 0, 10));
    }
}