package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(ParasiteEventEntity.class)
public abstract class ParasiteEventEntityMixin {
    @Redirect(
            method="merge",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData srpmixins_getPlayerData(World world, int id, @Local(argsOnly = true) EntityParasiteBase entityin){
        return SRPSaveDataInterface.get(world,null,entityin.getPosition());
    }

    @Redirect(
            method="spawnBeckonE",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData srpmixins_getPlayerData2(World world, int id, @Local(argsOnly = true) EntityParasiteBase in){
        return SRPSaveDataInterface.get(world,null,in.getPosition());
    }

    @Redirect(
            method="hijackEntity",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData srpmixins_getPlayerData3(World world, int id, @Local(argsOnly = true) EntityLivingBase entityIn){
        return SRPSaveDataInterface.get(world,null,entityIn.getPosition());
    }

    @Redirect(
            method="convertEntity",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData srpmixins_getPlayerData4(World world, int id, @Local(argsOnly = true) EntityLivingBase entityIn){
        return SRPSaveDataInterface.get(world,null,entityIn.getPosition());
    }

    @Redirect(
            method="convertEntityFeral",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData srpmixins_getPlayerData5(World world, int id, @Local(argsOnly = true) EntityLivingBase entityIn){
        return SRPSaveDataInterface.get(world,null,entityIn.getPosition());
    }
}