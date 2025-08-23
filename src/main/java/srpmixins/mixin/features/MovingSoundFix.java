package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParasiteEventEntity.class)
public abstract class MovingSoundFix {
    @Unique private static boolean srpmixins$alreadySentMovingSound = false;

    @Inject(method = "alertAllPlayerDim", at = @At(value = "HEAD"))
    private static void srpmixins_resetFlag(World worldIn, String message, int warning, CallbackInfo ci){
        srpmixins$alreadySentMovingSound = false;
    }

    @WrapWithCondition(
            method = "alertAllPlayerDim",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/network/simpleimpl/SimpleNetworkWrapper;sendToDimension(Lnet/minecraftforge/fml/common/network/simpleimpl/IMessage;I)V")
    )
    private static boolean srpmixins_onlySendOnce(SimpleNetworkWrapper instance, IMessage message, int dimensionId){
        if(srpmixins$alreadySentMovingSound) return false;
        srpmixins$alreadySentMovingSound = true;
        return true;
    }
}
