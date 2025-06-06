package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.proxy.CommonProxy;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPDisloEventHandler;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerTerrainGen;
import com.dhanantry.scapeandrunparasites.world.gen.WorldGenCustomStructures;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommonProxy.class)
public class PreventServerCrash {
    @Redirect(
            method = "preInit",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/handlers/SRPRegistryHandlers;initEvents()V"),
            remap = false
    )
    private void srpmixins_preventStartupServerCrash(){
        //This is all the stuff that would be in SRPRegistryHandlers.initEvents()
        MinecraftForge.EVENT_BUS.register(new SRPEventHandlerBus());
        MinecraftForge.EVENT_BUS.register(new SRPDisloEventHandler());
        MinecraftForge.TERRAIN_GEN_BUS.register(new SRPEventHandlerTerrainGen());
        //MinecraftForge.EVENT_BUS.register(new ScreenOverlayRenderer()); //crashes
        GameRegistry.registerWorldGenerator(new WorldGenCustomStructures(), 0);
    }
}
