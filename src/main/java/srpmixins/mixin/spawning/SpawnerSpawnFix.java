package srpmixins.mixin.spawning;

import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//By nischhelm / RLMixins, modified
@Mixin(SRPSpawning.DimensionHandler.class)
public abstract class SpawnerSpawnFix {

    @Inject(
            method = "onSpawn",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;loadedEntityList:Ljava/util/List;"),
            cancellable = true
    )
    private static void srpmixins_forceSpawnerSpawns(LivingSpawnEvent.CheckSpawn event, CallbackInfo ci){
        if(event.isSpawner()){
            //Skip all the mob cap etc checks if it's a spawner block
            event.setResult(Event.Result.DEFAULT);
            ci.cancel();
        }
    }
}