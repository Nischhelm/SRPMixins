package srpmixins.mixin.oe_tmp;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.sirsquidly.oe.entity.EntityDrowned;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityDrowned.class)
public class EntityDrownedMixin {

    @WrapWithCondition(
            method = "removeInjectedAI",
            at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;)V"),
            remap = false
    )
    private boolean srpmixins_removeLogSpamSMH(Logger instance, String string){
        return false;
    }
}
