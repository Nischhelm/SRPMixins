package srpmixins.mixin.vanilla;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.providers.LootPoolProvider;

@Mixin(ForgeHooks.class)
public abstract class ReadLootNameMixin {
    @Inject(
            method = "readPoolName",
            at = @At(value = "HEAD"),
            remap = false,
            cancellable = true
    )
    private static void srpmixins_dontRequireContextWhenParsingFromSRPMixinsConfig(JsonObject json, CallbackInfoReturnable<String> cir){
        if(LootPoolProvider.readingFromSRPMixinsConfig.get())
            cir.setReturnValue(JsonUtils.getString(json, "name"));
    }

    @Inject(
            method = "readLootEntryName",
            at = @At(value = "HEAD"),
            remap = false,
            cancellable = true
    )
    private static void srpmixins_dontRequireContextWhenParsingFromSRPMixinsConfig(JsonObject json, String type, CallbackInfoReturnable<String> cir){
        if(LootPoolProvider.readingFromSRPMixinsConfig.get())
            if("empty".equals(type))
                cir.setReturnValue("empty");
            else
                cir.setReturnValue(JsonUtils.getString(json, "name"));
    }
}
