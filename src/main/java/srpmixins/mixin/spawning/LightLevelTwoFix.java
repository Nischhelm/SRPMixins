package srpmixins.mixin.spawning;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.Random;

@Mixin(EntityParasiteBase.class)
public abstract class LightLevelTwoFix extends EntityLivingBase {
    public LightLevelTwoFix(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "isValidLightLevelTwo",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private void srpmixins_earlyReturn(CallbackInfoReturnable<Boolean> cir){
        //This and the next @WrapOperation is just to shuffle up this independent failure state for minimal performance gains
        cir.setReturnValue(this.getRNG().nextInt(8) == 0);
    }

    @WrapOperation(
            method = "isValidLightLevelTwo",
            at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", ordinal = 1),
            remap = false
    )
    private int srpmixins_dontCheckLater(Random instance, int i, Operation<Integer> original){
        return 0;
    }

    @ModifyConstant(
            method = "isValidLightLevelTwo",
            constant = @Constant(intValue = 7),
            remap = false
    )
    private int srpmixins_varyBlocklightThreshold(int constant) {
        return SRPMixinsConfigHandler.spawns.blockLightThresholdTwo;
    }
}