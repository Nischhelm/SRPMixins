package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.block.BlockInfestedRemain;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.config.SRPConfigProvider;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(BlockInfestedRemain.class)
public abstract class BlockInfestedRemainMixin {
    @WrapOperation(
            method = "updateTick",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;getRSchance(Lnet/minecraft/world/World;)D", remap = false)
    )
    public double srpmixins_getRSChance(World world, Operation<Double> original, @Local(argsOnly = true) BlockPos blockPos) {
        //ParasiteEventEntity.getRSChance(world) only gets called from here, but can't mixin there bc i don't have the blockpos there
        SRPSaveData saveData = SRPSaveDataInterface.get(world,null,blockPos);
        byte evoPhase = saveData.getEvolutionPhase(world.provider.getDimension());
        return SRPConfigProvider.getReinforcementChance(evoPhase);
    }

    @Redirect(
            method="updateTick",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    public SRPSaveData srpmixins_getPlayerData(World world, int id, @Local(argsOnly = true) BlockPos pos){
        return SRPSaveDataInterface.get(world,null,pos);
    }
}