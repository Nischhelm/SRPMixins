package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(SRPWorldData.class)
public abstract class SRPWorldDataMixin {
    //TODO: prob need to do it for all of them

/*    @Redirect(
            method = "updateDays",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private SRPSaveData srpmixins_getPlayerData(World world, int id, @Local(argsOnly = true) LivingSpawnEvent.CheckSpawn event) {
        return SRPSaveDataInterface.get(world, null, event.getEntity().getPosition());
    }*/

    @Redirect(
            method = "setOriginHealth",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private SRPSaveData srpmixins_getPlayerData2(World world, int id, @Local(argsOnly = true) BlockPos pos) {
        return SRPSaveDataInterface.get(world, null, pos);
    }

    @Redirect(
            method = {"setOrigin", "removeOrigin"},
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private SRPSaveData srpmixins_getPlayerData3(World world, int id, @Local(argsOnly = true, ordinal = 0) int x, @Local(argsOnly = true, ordinal = 1) int y, @Local(argsOnly = true, ordinal = 2) int z) {
        return SRPSaveDataInterface.get(world, null, new BlockPos(x,y,z));
    }
}