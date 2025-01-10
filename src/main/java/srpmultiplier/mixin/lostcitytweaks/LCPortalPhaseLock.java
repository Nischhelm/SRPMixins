package srpmultiplier.mixin.lostcitytweaks;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import mcjty.lostcities.ForgeEventHandlers;
import mcjty.lostcities.config.LostCityConfiguration;
import mcjty.lostcities.varia.CustomTeleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;
import srpmultiplier.util.SRPSaveDataInterface;

@Mixin(ForgeEventHandlers.class)
public abstract class LCPortalPhaseLock {

    @Redirect(
            method = "onPlayerSleepInBedEvent",
            at = @At(value = "INVOKE", target = "Lmcjty/lostcities/varia/CustomTeleporter;teleportToDimension(Lnet/minecraft/entity/player/EntityPlayer;ILnet/minecraft/util/math/BlockPos;)V"),
            remap = false
    )
    void lockPortalBehindPhase(EntityPlayer player, int dimension, BlockPos pos){
        if(!SRPConfigSystems.useEvolution) return;
        if(SRPMultiplierConfigHandler.modcompat.portalLClockedPhase > -1) {
            if (dimension == LostCityConfiguration.DIMENSION_ID) {
                byte evoPhase;
                World world = player.getEntityWorld();
                SRPSaveData data = SRPSaveData.get(world);
                int dimensionPlayer = world.provider.getDimension();
                if (SRPMultiplierConfigHandler.phasepoints.playerPhases)
                    evoPhase = ((SRPSaveDataInterface) data).getByPlayer(world, player.getUniqueID()).getEvolutionPhase(dimensionPlayer);
                else
                    evoPhase = data.getEvolutionPhase(dimensionPlayer);

                if (evoPhase >= SRPMultiplierConfigHandler.modcompat.portalLClockedPhase) {
                    CustomTeleporter.teleportToDimension(player, LostCityConfiguration.DIMENSION_ID, pos);
                } else {
                    player.sendStatusMessage(new TextComponentString(TextFormatting.RED + "Fear incapacitates you"), true);
                    Potion fearEffect = Potion.getPotionFromResourceLocation("lycanitesmobs:fear");
                    if(fearEffect != null)
                        player.addPotionEffect(new PotionEffect(fearEffect, 100, 0));
                }
            }
        }
    }

    @Inject(
            method = "onPlayerSleepInBedEvent",
            at = @At(value = "INVOKE",target = "Lmcjty/lostcities/varia/CustomTeleporter;teleportToDimension(Lnet/minecraft/entity/player/EntityPlayer;ILnet/minecraft/util/math/BlockPos;)V"),
            remap = false
    )
    void stopSleepingMixin(PlayerSleepInBedEvent event, CallbackInfo ci) {
        if(!SRPConfigSystems.useEvolution) return;
        if (event.getResult() == Event.Result.DENY)
            event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
    }
}