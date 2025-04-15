package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class SRPSpawningDimensionMixin {
    @Redirect(
            method="onSpawn",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    private static SRPSaveData srpmixins_getPlayerData(World world, @Local(argsOnly = true) LivingSpawnEvent.CheckSpawn event){
        return SRPSaveDataInterface.get(world,null,event.getEntity().getPosition());
    }
}