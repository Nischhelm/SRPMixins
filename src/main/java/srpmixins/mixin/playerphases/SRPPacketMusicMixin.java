package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.network.SRPPacketMusic;
import com.dhanantry.scapeandrunparasites.network.SRPPacketMusicTwo;
import com.google.common.util.concurrent.ListenableFuture;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(SRPPacketMusic.Handler.class)
public class SRPPacketMusicMixin {
    @WrapOperation(
            method = "onMessage(Lcom/dhanantry/scapeandrunparasites/network/SRPPacketMusic;Lnet/minecraftforge/fml/common/network/simpleimpl/MessageContext;)Lnet/minecraftforge/fml/common/network/simpleimpl/IMessage;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;addScheduledTask(Ljava/lang/Runnable;)Lcom/google/common/util/concurrent/ListenableFuture;"),
            remap = false
    )
    private ListenableFuture<Object> srpmixins_getPlayerData(MinecraftServer instance, Runnable runnableToSchedule, Operation<ListenableFuture<Object>> original, @Local EntityPlayerMP player){
        return original.call(instance, (Runnable) () -> SRPMain.network.sendTo(new SRPPacketMusicTwo(SRPSaveDataInterface.get(player.world, player, null).getEvolutionPhase(player.world.provider.getDimension())), player));
    }
}
