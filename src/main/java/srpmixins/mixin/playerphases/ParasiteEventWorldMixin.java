package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(ParasiteEventWorld.class)
public abstract class ParasiteEventWorldMixin {
    @Redirect(
            method="placeHeartInWorld",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData srpmixins_getPlayerData(World world, @Local(argsOnly = true) BlockPos blockPos){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }

    @Redirect(
            method="canInfestBlock",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData srpmixins_getPlayerData2(World world, @Local(argsOnly = true) BlockPos blockPos){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }

    @Redirect(
            method="spreadBiomeBlockStain",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData srpmixins_getPlayerData3(World world, @Local(argsOnly = true) BlockPos blockPos){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }

    @Redirect(
            method="spreadBiomeBlockTrunk",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData srpmixins_getPlayerData4(World world, @Local(argsOnly = true) BlockPos blockPos){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }

    @Redirect(
            method="placeColonyInWorld",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData srpmixins_getPlayerData5(World world, @Local(argsOnly = true) BlockPos blockPos){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }
}