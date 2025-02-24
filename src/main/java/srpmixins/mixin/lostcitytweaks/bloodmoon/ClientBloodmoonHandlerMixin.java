package srpmixins.mixin.lostcitytweaks.bloodmoon;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import lumien.bloodmoon.client.ClientBloodmoonHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(ClientBloodmoonHandler.class)
public abstract class ClientBloodmoonHandlerMixin {

    @ModifyExpressionValue(
            method="clientTick",
            at=@At(value="INVOKE",target = "Lnet/minecraft/world/WorldProvider;getDimension()I"),
            remap=false
    )
    private int bloodmoonInLCMixin(int original){
        if(SRPMixinsConfigHandler.modcompat.bloodmoonInLC && original == 111) return 0;
        return original;
    }
}