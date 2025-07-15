package srpmixins.mixin.overlast.customphases.full;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.overlast.item.ItemEvoDevice;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(ItemEvoDevice.class)
public abstract class ItemEvoDeviceMixin {
    @WrapOperation(
            method = "onItemUseFinish",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;", remap = false)
    )
    private SRPSaveData srpmixins_useCustomPhases(World world, Operation<SRPSaveData> original, @Local EntityPlayerMP player){
        return SRPSaveDataInterface.get(world, player, null);
    }
}
