package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.network.*;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(value = {
        SRPCommandDislodgment.class,
        SRPCommandEvolution.class,
        SRPCommandGeneration.class,
        SRPCommandRoot.class,
        SRPCommandUDevelopment.class
})
public abstract class SRPCommandAnyMixin_execute {
    @Redirect(
            method="execute",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;", remap = false)
    )
    public SRPSaveData srpmixins_getPlayerData(World world, int id, @Local(argsOnly = true) ICommandSender sender){
        return SRPSaveDataInterface.get(world,(EntityPlayer) sender.getCommandSenderEntity(),null);
    }
}