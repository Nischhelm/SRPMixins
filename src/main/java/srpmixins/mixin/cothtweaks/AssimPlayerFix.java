package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(SRPEventHandlerBus.class)
public abstract class AssimPlayerFix {
    @Inject(
            method = "playerDeath",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z")
    )
    private void srpmixins_increaseAssimilatedCountForSimAdventurer(LivingDeathEvent event, CallbackInfo ci){
        SRPSaveDataInterface.get(event.getEntity().world, (EntityPlayer) event.getEntityLiving(), null).addNumberIDDataSpawn(SRPMobConfigProvider.mobNameToParaIdMap.get("sim_adventurer"));
    }
}
