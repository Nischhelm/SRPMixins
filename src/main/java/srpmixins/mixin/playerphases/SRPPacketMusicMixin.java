package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.network.SRPPacketMusic;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

//Looks like it doesn't work but it does work
@Mixin(targets = "com.dhanantry.scapeandrunparasites.network.SRPPacketMusic$Handler$1")
public class SRPPacketMusicMixin {
    @Unique private EntityPlayerMP srpmixins$player;

    @Inject(
            method = "<init>",
            at = @At("TAIL"),
            remap = false
    )
    private void srpmixins_savePlayer(SRPPacketMusic.Handler this$0, EntityPlayerMP var2, CallbackInfo ci){
        srpmixins$player = var2;
    }

    @Redirect(
            method = "run",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private SRPSaveData srpmixins_getPlayerData(World world, int id){
        return SRPSaveDataInterface.get(world, null, srpmixins$player.getPosition());
    }
}
