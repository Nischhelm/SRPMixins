package srpmixins.mixin.modcompat.hordes;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.hordes.config.data.DataRegistry;
import net.smileycorp.hordes.config.data.DataType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.SRPMixins;
import srpmixins.compat.hordes.ParasiteKillGetter;
import srpmixins.compat.hordes.ParasiteKillsCondition;
import srpmixins.compat.hordes.ParasitePhaseCondition;
import srpmixins.compat.hordes.ParasitePhaseGetter;

@Mixin(DataRegistry.class)
public abstract class DataRegistryMixin {
    @Inject(method = "registerValueGetters", at = @At("TAIL"), remap = false)
    private static void srpmixins_addSRPCompat_values(CallbackInfo ci) {
        DataRegistry.registerValueGetter(new ResourceLocation("srparasites:phase"), ParasitePhaseGetter::deserialize);
        DataRegistry.registerValueGetter(new ResourceLocation("srparasites:kills"), ParasiteKillGetter::deserialize);
    }

    @Inject(method = "registerConditionDeserializers", at = @At("TAIL"), remap = false)
    private static void srpmixins_addSRPCompat_conditions(CallbackInfo ci) {
        DataRegistry.registerConditionDeserializer(new ResourceLocation("srpmixins:phase"), ParasitePhaseCondition::deserialize);
        DataRegistry.registerConditionDeserializer(new ResourceLocation("srpmixins:kills"), ParasiteKillsCondition::deserialize);
    }

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
