package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(SRPEventHandlerBus.class)
public abstract class SRPEventHandlerBusMixin {
    @Redirect(
            method = "cropGrow",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    public SRPSaveData srpmixins_getPlayerData(World world, int id, @Local(argsOnly = true) BlockEvent.CropGrowEvent.Pre event) {
        return SRPSaveDataInterface.get(world, null, event.getPos());
    }

    @Redirect(
            method = "playerFishing",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    public SRPSaveData srpmixins_getPlayerData2(World world, int id, @Local(argsOnly = true) ItemFishedEvent event) {
        return SRPSaveDataInterface.get(world, event.getEntityPlayer(), null);
    }

    @Redirect(
            method = "writeCOTHTag",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    public SRPSaveData srpmixins_getPlayerData7(World world, int id, @Local(argsOnly = true) EntityLivingBase entity) {
        return SRPSaveDataInterface.get(world, null, entity.getPosition());
    }

    @Redirect(
            method = "setNewParasiteTask",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    public SRPSaveData srpmixins_getPlayerData3(World world, int id, @Local(argsOnly = true) EntityParasiteBase entity) {
        return SRPSaveDataInterface.get(world, null, entity.getPosition());
    }

    @Redirect(
            method = "setLoot",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    public SRPSaveData srpmixins_getPlayerData4(World world, int id, @Local(argsOnly = true) LivingDropsEvent event) {
        return SRPSaveDataInterface.get(world, null, event.getEntity().getPosition());
    }

    @Redirect(
            method = "playerUp",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    public SRPSaveData srpmixins_getPlayerData5(World world, int id, @Local(argsOnly = true) PlayerWakeUpEvent event) {
        return SRPSaveDataInterface.get(world, event.getEntityPlayer(), null);
    }

    @Redirect(
            method = "entityHeal",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    public SRPSaveData srpmixins_getPlayerData6(World world, int id, @Local(argsOnly = true) LivingHealEvent event) {
        EntityPlayer playerHeal = null;
        BlockPos blockPosHeal = null;
        if (event.getEntityLiving() instanceof EntityPlayer)
            playerHeal = (EntityPlayer) event.getEntityLiving();
        else blockPosHeal = event.getEntityLiving().getPosition();

        return SRPSaveDataInterface.get(world, playerHeal, blockPosHeal);
    }
}