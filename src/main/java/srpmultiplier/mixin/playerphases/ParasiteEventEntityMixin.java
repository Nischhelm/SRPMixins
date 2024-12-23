package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmultiplier.util.SRPSaveDataInterface;

@Mixin(ParasiteEventEntity.class)
public abstract class ParasiteEventEntityMixin {

    @Unique private static BlockPos blockPos;

    @Inject(
            method = "merge",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private static void saveBlockPosMixin(EntityParasiteBase entityin, int code, String name, CallbackInfoReturnable<Boolean> cir){
        blockPos = entityin.getPosition();
    }

    @Redirect(
            method="merge",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData getPlayerDataMixin(World world){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }

    @Inject(
            method = "spawnBeckonE",
            at = @At("HEAD"),
            remap = false
    )
    private static void saveBlockPosMixin2(SRPWorldData data, World worldIn, EntityParasiteBase in, CallbackInfo ci){
        blockPos = in.getPosition();
    }

    @Redirect(
            method="spawnBeckonE",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData getPlayerDataMixin2(World world){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }

    @Inject(
            method = "hijackEntity",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private static void saveBlockPosMixin(EntityLivingBase entityIn, String[] list, CallbackInfoReturnable<Boolean> cir){
        blockPos = entityIn.getPosition();
    }

    @Redirect(
            method="hijackEntity",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData getPlayerDataMixin3(World world){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }

    @Inject(
            method = "convertEntity",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private static void saveBlockPosMixin(EntityLivingBase entityIn, NBTTagCompound tags, boolean ignoreKey, String[] list, CallbackInfo ci){
        blockPos = entityIn.getPosition();
    }

    @Redirect(
            method="convertEntity",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData getPlayerDataMixin4(World world){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }

    @Inject(
            method = "convertEntityFeral",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private static void saveBlockPosMixin(EntityLivingBase entityIn, NBTTagCompound tags, boolean ignoreKey, String[] list, CallbackInfoReturnable<Boolean> cir){
        blockPos = entityIn.getPosition();
    }

    @Redirect(
            method="convertEntityFeral",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData getPlayerDataMixin5(World world){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }
}