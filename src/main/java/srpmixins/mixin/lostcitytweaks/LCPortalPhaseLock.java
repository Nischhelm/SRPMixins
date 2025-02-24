package srpmixins.mixin.lostcitytweaks;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import mcjty.lostcities.ForgeEventHandlers;
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
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.util.SRPSaveDataInterface;

@Mixin(ForgeEventHandlers.class)
public abstract class LCPortalPhaseLock {

    @WrapWithCondition(
            method = "onPlayerSleepInBedEvent",
            at = @At(value = "INVOKE", target = "Lmcjty/lostcities/varia/CustomTeleporter;teleportToDimension(Lnet/minecraft/entity/player/EntityPlayer;ILnet/minecraft/util/math/BlockPos;)V", ordinal = 1),
            remap = false
    )
    private boolean lockPortalBehindPhase(EntityPlayer player, int dimension, BlockPos pos, @Local(argsOnly = true) PlayerSleepInBedEvent event) {
        if (!SRPConfigSystems.useEvolution) return true;
        if (SRPMixinsConfigHandler.modcompat.portalLClockedPhase < 0) return true;

        byte evoPhase = SRPSaveDataInterface.get(player.getEntityWorld(), player, null).getEvolutionPhase(player.dimension);
        if (evoPhase >= SRPMixinsConfigHandler.modcompat.portalLClockedPhase) return true;

        //Fear incapacitates
        player.sendStatusMessage(new TextComponentTranslation("srpmixins.msg.fearincapacitates").setStyle(new Style().setColor(TextFormatting.RED)), true);

        //Fear effect
        Potion fearEffect = Potion.getPotionFromResourceLocation("lycanitesmobs:fear");
        if (fearEffect != null) player.addPotionEffect(new PotionEffect(fearEffect, 100, 0));

        //No "can't sleep" msg
        event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);    //Otherwise the fear msg gets overwritten with cant sleep msg

        return false;
    }
}