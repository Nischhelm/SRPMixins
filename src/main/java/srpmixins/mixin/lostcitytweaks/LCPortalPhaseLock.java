package srpmixins.mixin.lostcitytweaks;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.sugar.Local;
import mcjty.lostcities.ForgeEventHandlers;
import mcjty.lostcities.config.LostCityConfiguration;
import mcjty.lostcities.varia.CustomTeleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.util.SRPSaveDataInterface;

@Mixin(ForgeEventHandlers.class)
public abstract class LCPortalPhaseLock {

    @Redirect(
            method = "onPlayerSleepInBedEvent",
            at = @At(value = "INVOKE", target = "Lmcjty/lostcities/varia/CustomTeleporter;teleportToDimension(Lnet/minecraft/entity/player/EntityPlayer;ILnet/minecraft/util/math/BlockPos;)V", ordinal = 1),
            remap = false
    )
    void lockPortalBehindPhase(EntityPlayer player, int dimension, BlockPos pos, @Local(argsOnly = true) PlayerSleepInBedEvent event) {
        if (!SRPConfigSystems.useEvolution) return;
        if (SRPMixinsConfigHandler.modcompat.portalLClockedPhase > -1) {
            byte evoPhase = SRPSaveDataInterface.get(player.getEntityWorld(), player, null).getEvolutionPhase(player.dimension);

            if (evoPhase < SRPMixinsConfigHandler.modcompat.portalLClockedPhase) {
                player.sendStatusMessage(new TextComponentTranslation("srpmixins.msg.fearincapacitates").setStyle(new Style().setColor(TextFormatting.RED)), true);
                Potion fearEffect = Potion.getPotionFromResourceLocation("lycanitesmobs:fear");
                if (fearEffect != null)
                    player.addPotionEffect(new PotionEffect(fearEffect, 100, 0));
                event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);    //Otherwise the fear msg gets overwritten with cant sleep msg
            } else
                CustomTeleporter.teleportToDimension(player, LostCityConfiguration.DIMENSION_ID, pos);
        }
    }
}