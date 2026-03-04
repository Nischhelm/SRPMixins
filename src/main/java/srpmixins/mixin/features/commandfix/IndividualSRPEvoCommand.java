package srpmixins.mixin.features.commandfix;

import com.dhanantry.scapeandrunparasites.network.SRPCommandEvolution;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

import java.util.Arrays;
import java.util.HashSet;

@Mixin(value = SRPCommandEvolution.class, priority = 1001)
public abstract class IndividualSRPEvoCommand {
    @Unique private static final HashSet<String> srpmixins$incCmds = new HashSet<>(Arrays.asList("addpoints", "setcooldown", "setphase"));

    @Inject(
            method = "execute",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;useEvolution:Z", remap = false)
    )
    private void captureDimAndPlayer(
            MinecraftServer server, ICommandSender sender, String[] args,
            CallbackInfo ci,
            @Share("dimension") LocalRef<Integer> dimId,
            @Share("playerName") LocalRef<String> playerName
    ){
        if(args.length <= 1) return;
        int ind = srpmixins$incCmds.contains(args[0]) ? 2 : 1;
        if(args.length > ind){
            try {
                dimId.set(Integer.parseInt(args[ind]));
            } catch (NumberFormatException e) {
                playerName.set(args[ind]);
            }
        }
        if(args.length > ind + 1){
            playerName.set(args[ind + 1]);
        }
    }

    @WrapOperation(method = "execute", at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;", remap = false))
    private SRPSaveData srpmixins_modifyTargetedPlayer(
            World world, Operation<SRPSaveData> original,
            @Share("playerName") LocalRef<String> playerName
    ){
        if(!SRPMixinsConfigHandler.playerphases.enabled) return original.call(world);
        String player =  playerName.get();
        if(player == null || player.isEmpty()) return original.call(world);
        EntityPlayer entityPlayer = world.getPlayerEntityByName(player);
        if(entityPlayer == null) return original.call(world); //could maybe instead complain, like this it will use the command senders SRPSaveData
        return SRPSaveDataInterface.get(world, entityPlayer, null);
    }

    @ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getEvolutionPhase(I)B", remap = false))
    private int srpmixins_modifyDimension_getPhase(int dim, @Share("dimension") LocalRef<Integer> dimId){
        return dimId.get() == null ? dim : dimId.get();
    }

    @ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getCanLoss(I)Z", remap = false))
    private int srpmixins_modifyDimension_getCanLose(int dim, @Share("dimension") LocalRef<Integer> dimId){
        return dimId.get() == null ? dim : dimId.get();
    }

    @ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getCanGain(I)Z", remap = false))
    private int srpmixins_modifyDimension_getCanGain(int dim, @Share("dimension") LocalRef<Integer> dimId){
        return dimId.get() == null ? dim : dimId.get();
    }

    @ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getTotalKills(I)I", remap = false))
    private int srpmixins_modifyDimension_getTotalKills(int dim, @Share("dimension") LocalRef<Integer> dimId){
        return dimId.get() == null ? dim : dimId.get();
    }

    @ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getCooldown(Lnet/minecraft/world/World;I)I", remap = false))
    private int srpmixins_modifyDimension_getCooldown(int dim, @Share("dimension") LocalRef<Integer> dimId){
        return dimId.get() == null ? dim : dimId.get();
    }

    @ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setGaining(ZI)V", remap = false))
    private int srpmixins_modifyDimension_setCanGain(int dim, @Share("dimension") LocalRef<Integer> dimId){
        return dimId.get() == null ? dim : dimId.get();
    }

    @ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setLoss(ZI)V", remap = false))
    private int srpmixins_modifyDimension_setCanLose(int dim, @Share("dimension") LocalRef<Integer> dimId){
        return dimId.get() == null ? dim : dimId.get();
    }

    @ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setEvolutionPhase(IBZLnet/minecraft/world/World;Z)Z", remap = false))
    private int srpmixins_modifyDimension_setPhase(int dim, @Share("dimension") LocalRef<Integer> dimId){
        return dimId.get() == null ? dim : dimId.get();
    }

    @ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setTotalKills(IIZLnet/minecraft/world/World;Z)Z", remap = false), index = 0)
    private int srpmixins_modifyDimension_setTotalKills(int dim, @Share("dimension") LocalRef<Integer> dimId){
        return dimId.get() == null ? dim : dimId.get();
    }

    @ModifyArg(method = "execute", at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setCooldown(ILnet/minecraft/world/World;I)V", remap = false), index = 0)
    private int srpmixins_modifyDimension_setCooldown(int dim, @Share("dimension") LocalRef<Integer> dimId){
        return dimId.get() == null ? dim : dimId.get();
    }
}
