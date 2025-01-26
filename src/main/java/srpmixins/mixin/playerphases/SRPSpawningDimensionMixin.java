package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.util.SRPSaveDataInterface;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class SRPSpawningDimensionMixin {

    @Unique private static BlockPos blockPos;

    @Inject(
            method = "onSpawn",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private static void saveBlockPosMixin(LivingSpawnEvent.CheckSpawn event, CallbackInfo ci){
        blockPos = event.getEntity().getPosition();
    }

    @Redirect(
            method="onSpawn",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData getPlayerDataMixin(World world){
        return SRPSaveDataInterface.get(world,null,blockPos);
    }
}