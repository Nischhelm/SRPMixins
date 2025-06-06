package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.client.renderer.ScreenOverlayRenderer;
import com.dhanantry.scapeandrunparasites.proxy.ClientProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientProxy.class)
public class PreventServerCrashClient {
    @Inject(
            method = "preInit",
            at = @At(value = "TAIL"),
            remap = false
    )
    private void srpmixins_preventStartupServerCrash(FMLPreInitializationEvent e, CallbackInfo ci){
        MinecraftForge.EVENT_BUS.register(new ScreenOverlayRenderer());
    }
}
