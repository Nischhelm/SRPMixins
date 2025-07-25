package srpmixins.mixin.modcompat.srpextra.customphases;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.sugar.Local;
import energon.srpextra.util.SRPEWorldSpawnMob;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(SRPEWorldSpawnMob.class)
public abstract class SRPEWorldSpawnMobMixin {
    //Bit of a bruh
    @ModifyReceiver(
            method = "playerSpawn",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getEvolutionPhase(I)B")
    )
    private SRPSaveData srpmixins_useCustomPhases(SRPSaveData instance, int i, @Local(argsOnly = true) EntityPlayer player){
        return SRPSaveDataInterface.get(player.world, player, null);
    }
}
