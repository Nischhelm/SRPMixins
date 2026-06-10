package srpmixins.mixin.cothtweaks;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SRPEventHandlerBus.class)
public abstract class COTHBossDrops {
    @Inject(
            method = "setLoot",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private void srpmixins_dropBossLoot(LivingDropsEvent event, CallbackInfo ci) {
        EntityLivingBase entity = event.getEntityLiving();
        if(entity == null) return;
        if(entity instanceof EntityParasiteBase) return;
        if(entity instanceof EntityPlayer) return;
        if(!entity.isNonBoss())
            ci.cancel();
    }
}
