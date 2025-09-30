package srpmixins.mixin.modcompat.hordes;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.smileycorp.hordes.config.data.DataRegistry;
import net.smileycorp.hordes.config.data.DataType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.SRPMixins;

@Mixin(DataRegistry.class)
public abstract class DataRegistryMixin {
    @ModifyExpressionValue(
            method = "readValue",
            at = @At(value = "INVOKE", target = "Lcom/google/gson/JsonObject;has(Ljava/lang/String;)Z", ordinal = 1),
            remap = false
    )
    private static boolean srpmixins_allowEmptyValueGetters(boolean original, DataType type, JsonObject json){
        if(original) return true;

        SRPMixins.LOGGER.info("SRPMixins caught and fixed an issue of Hordes where ValueGetters couldn't be defined without a \"value\" for value getter {}", json.get("name").toString());
        return true;
    }
}
