package srpmixins.mixin.lostcitytweaks;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import mcjty.lostcities.ForgeEventHandlers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(ForgeEventHandlers.class)
public abstract class LCPortalPhaseLock {

    @Inject(
            method = "onPlayerSleepInBedEvent",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/entity/player/PlayerSleepInBedEvent;setResult(Lnet/minecraftforge/fml/common/eventhandler/Event$Result;)V", ordinal = 1),
            remap = false,
            cancellable = true
    )
    private void srpmixins_lockPortalBehindPhase(PlayerSleepInBedEvent event, CallbackInfo ci) {
        if (!SRPConfigSystems.useEvolution) return;
        if (SRPMixinsConfigHandler.modcompat.portalLClockedPhase < 0) return;

        EntityPlayer player = event.getEntityPlayer();
        byte evoPhase = SRPSaveDataInterface.get(player.getEntityWorld(), player, null).getEvolutionPhase(player.dimension);
        if (evoPhase >= SRPMixinsConfigHandler.modcompat.portalLClockedPhase) return;

        //Fear incapacitates
        player.sendStatusMessage(new TextComponentTranslation("srpmixins.msg.fearincapacitates").setStyle(new Style().setColor(TextFormatting.RED)), true);

        //Fear effect
        Potion fearEffect = Potion.getPotionFromResourceLocation("lycanitesmobs:fear");
        if (fearEffect != null) player.addPotionEffect(new PotionEffect(fearEffect, 100, 0));

        //No "can't sleep" msg
        event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);    //Otherwise the fear msg gets overwritten with cant sleep msg

        ci.cancel();
    }
}