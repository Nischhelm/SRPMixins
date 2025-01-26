package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.util.SRPSaveDataInterface;

import java.util.Random;

@Mixin(ParasiteEventWorld.class)
public abstract class ParasiteEventWorldMixin {

    @Unique
    private static BlockPos blockPos;

    @Inject(
            method = "placeHeartInWorld",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private static void saveBlockPosMixin(World worldIn, BlockPos pos, CallbackInfoReturnable<Integer> cir){
        blockPos = pos;
    }

    @Redirect(
            method="placeHeartInWorld",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData getPlayerDataMixin(World world){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }

    @Inject(
            method = "canInfestBlock",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private static void saveBlockPosMixin2(World worldIn, BlockPos pos, Random rand, int stage, boolean fromVenkrol, CallbackInfo ci){
        blockPos = pos;
    }

    @Redirect(
            method="canInfestBlock",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData getPlayerDataMixin2(World world){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }

    @Inject(
            method = "spreadBiomeBlockStain",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private static void saveBlockPosMixin3(World worldIn, BlockPos pos, Random rand, CallbackInfo ci){
        blockPos = pos;
    }

    @Redirect(
            method="spreadBiomeBlockStain",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData getPlayerDataMixin3(World world){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }

    @Inject(
            method = "spreadBiomeBlockTrunk",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private static void saveBlockPosMixin4(World worldIn, BlockPos pos, Random rand, CallbackInfo ci){
        blockPos = pos;
    }

    @Redirect(
            method="spreadBiomeBlockTrunk",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData getPlayerDataMixin4(World world){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }

    @Inject(
            method = "placeColonyInWorld",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private static void saveBlockPosMixin5(World worldIn, BlockPos pos, CallbackInfoReturnable<Integer> cir){
        blockPos = pos;
    }

    @Redirect(
            method="placeColonyInWorld",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData getPlayerDataMixin5(World world){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }
}