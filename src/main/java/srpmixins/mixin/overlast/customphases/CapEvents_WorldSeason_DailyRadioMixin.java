package srpmixins.mixin.overlast.customphases;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.overlast.cap.CapEvents;
import com.overlast.season.DailyRadio;
import com.overlast.season.WorldSeason;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin({
        DailyRadio.class,
        CapEvents.class,
        WorldSeason.class
})
public abstract class CapEvents_WorldSeason_DailyRadioMixin {
    @WrapOperation(
            method = "onPlayerUpdate",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private SRPSaveData srpmixins_useCustomPhases(World world, Operation<SRPSaveData> original, @Local EntityPlayer player, @Share("saveddata") LocalRef<SRPSaveData> savedData){
        //just to keep calcs low we share it
        if(savedData.get() == null)
            savedData.set(SRPSaveDataInterface.get(world, player, null));
        return savedData.get();
    }
}
