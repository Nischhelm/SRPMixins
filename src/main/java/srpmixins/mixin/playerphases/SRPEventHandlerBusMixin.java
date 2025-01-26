package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.util.SRPSaveDataInterface;

@Mixin(SRPEventHandlerBus.class)
public abstract class SRPEventHandlerBusMixin {

    @Unique BlockPos blockPosCrop;
    @Unique EntityPlayer playerHeal;
    @Unique EntityPlayer playerFish;
    @Unique BlockPos blockPosSpawn;
    @Unique BlockPos blockPosLoot;
    @Unique EntityPlayer playerWake;
    @Unique BlockPos blockPosHeal;

    @Inject(
            method = "cropGrow",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getEvolutionPhase(I)B"),
            remap = false
    )
    void saveBlockPosMixin(BlockEvent.CropGrowEvent.Pre event, CallbackInfo ci){
        this.blockPosCrop = event.getPos();
    }

    @Redirect(
            method="cropGrow",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    public SRPSaveData getPlayerDataMixin(World world){
        return SRPSaveDataInterface.get(world,null,blockPosCrop);
    }

    @Inject(
            method = "playerFishing",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    void savePlayerMixin2(ItemFishedEvent event, CallbackInfo ci){
        this.playerFish = event.getEntityPlayer();
    }

    @Redirect(
            method="playerFishing",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    public SRPSaveData getPlayerDataMixin2(World world){
        return SRPSaveDataInterface.get(world,playerFish,null);
    }

    @Inject(
            method = "onEntitySpawn",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/handlers/SRPEventHandlerBus;setNewParasiteTask(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;Ljava/lang/String;ZLcom/dhanantry/scapeandrunparasites/world/SRPWorldData;)V"),
            remap = false
    )
    void saveWorldMixin3(EntityJoinWorldEvent event, CallbackInfo ci){
        this.blockPosSpawn = event.getEntity().getPosition();
    }

    @Redirect(
            method="writeCOTHTag",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    public SRPSaveData getPlayerDataMixin7(World world){
        return SRPSaveDataInterface.get(world,null,blockPosSpawn);
    }

    @Redirect(
            method="setNewParasiteTask",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    public SRPSaveData getPlayerDataMixin3(World world){
        return SRPSaveDataInterface.get(world,null,blockPosSpawn);
    }

    @Inject(
            method = "setLoot",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    void saveBlockPosMixin4(LivingDropsEvent event, CallbackInfo ci){
        this.blockPosLoot = event.getEntity().getPosition();
    }

    @Redirect(
            method="setLoot",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    public SRPSaveData getPlayerDataMixin4(World world){
        return SRPSaveDataInterface.get(world,null,blockPosLoot);
    }

    @Inject(
            method = "playerUp",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    void savePlayerMixin5(PlayerWakeUpEvent event, CallbackInfo ci){
        this.playerWake = event.getEntityPlayer();
    }

    @Redirect(
            method="playerUp",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    public SRPSaveData getPlayerDataMixin5(World world){
        return SRPSaveDataInterface.get(world,playerWake,null);
    }

    @Inject(
            method = "entityHeal",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    void savePlayerMixin6(LivingHealEvent event, CallbackInfo ci){
        if(event.getEntityLiving() instanceof EntityPlayer) {
            this.playerHeal = (EntityPlayer) event.getEntityLiving();
            this.blockPosHeal = null;
        } else {
            this.playerHeal = null;
            this.blockPosHeal = event.getEntityLiving().getPosition();
        }
    }

    @Redirect(
            method="entityHeal",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    public SRPSaveData getPlayerDataMixin6(World world){
        return SRPSaveDataInterface.get(world,playerHeal,blockPosHeal);
    }
}