package srpmixins.mixin.modcompat.overlast.customphases.full;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.overlast.util.OverUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(OverUtil.class)
public abstract class OverUtilMixin {
    @WrapOperation(
            method = {
                    "getPhase",
                    "setPhase",
                    "getTotalPoint",
                    "setCooldown",
                    "getCooldown",
                    "addEvoPoint"
            },
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private SRPSaveData srpmixins_useCustomPhases(World world, Operation<SRPSaveData> original, @Local(argsOnly = true) EntityPlayer player){
        return SRPSaveDataInterface.get(world, player, null);
    }
}
