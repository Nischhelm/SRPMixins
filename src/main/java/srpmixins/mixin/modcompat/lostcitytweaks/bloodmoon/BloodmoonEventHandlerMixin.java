package srpmixins.mixin.modcompat.lostcitytweaks.bloodmoon;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import lumien.bloodmoon.handler.BloodmoonEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(BloodmoonEventHandler.class)
public abstract class BloodmoonEventHandlerMixin {

    @ModifyExpressionValue(
            method="loadWorld",
            at=@At(value="INVOKE",target = "Lnet/minecraft/world/WorldProvider;getDimension()I"),
            remap=false
    )
    private int srpmixins_bloodmoonInLC(int original){
        if(SRPMixinsConfigHandler.modcompat.bloodmoonInLC && original == 111) return 0;
        return original;
    }

    @ModifyExpressionValue(
            method="livingUpdate",
            at=@At(value="FIELD",target = "Lnet/minecraft/entity/EntityLivingBase;dimension:I"),
            remap=false
    )
    private int srpmixins_killBloodMobs(int original){
        if(SRPMixinsConfigHandler.modcompat.bloodmoonInLC && original == 111) return 0;
        return original;
    }
}