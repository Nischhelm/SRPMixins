package srpmixins.mixin.features.loot;

import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SRPEventHandlerBus.class)
public abstract class CancelOriginalLootHandling {
    @WrapWithCondition(
            method = "setLoot",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/handlers/SRPEventHandlerBus;loot(Lnet/minecraftforge/event/entity/living/LivingDropsEvent;[Ljava/lang/String;)V"),
            remap = false
    )
    private boolean srpmixins_overwriteLootPoolLogic(SRPEventHandlerBus instance, LivingDropsEvent j, String[] item) {
        return false;
    }
}
