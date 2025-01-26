package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.network.SRPCommandEvolution;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.util.SRPSaveDataInterface;

@Mixin(SRPCommandEvolution.class)
public abstract class SRPCommandEvolutionMixin {

    @Unique EntityPlayer player;

    @Inject(
            method = "func_184881_a",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    void savePlayerMixin(MinecraftServer server, ICommandSender sender, String[] argString, CallbackInfo ci){
        this.player = (EntityPlayer) sender.getCommandSenderEntity();
    }

    @Redirect(
            method="func_184881_a",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap=false
    )
    public SRPSaveData getPlayerDataMixin(World world){
        return SRPSaveDataInterface.get(world,player,null);
    }
}