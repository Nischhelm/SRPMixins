package srpmixins.mixin.phasepointfixes;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.SRPMixinsPlugin;
import srpmixins.network.PhaseWarningOverhaul;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

import java.util.UUID;

@Mixin(SRPSaveData.class)
public abstract class UnlockMessageWithParaName {
    @WrapOperation(
            method = "checkForUnlock",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/ParasiteEventEntity;alertAllPlayerSer(Ljava/lang/String;Lnet/minecraft/world/World;)V"),
            remap = false
    )
    private void srpmixins_allowParaName(String message, World world, Operation<Void> original, @Local(name = "id") int paraId){
        UUID playerUUID = SRPMixinsPlugin.areCustomPhasesEnabled() ? ((SRPSaveDataInterface) this).srpmixins$getUUID() : null;
        PhaseWarningOverhaul.sendUnlockMessage(world, playerUUID, message, paraId);
    }
}
