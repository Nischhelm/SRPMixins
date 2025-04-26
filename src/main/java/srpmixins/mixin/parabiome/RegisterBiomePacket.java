package srpmixins.mixin.parabiome;

import com.dhanantry.scapeandrunparasites.SRPMain;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPPacketHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.network.SRPMixinsPacketBiomeChange;

@Mixin(SRPPacketHandler.class)
public abstract class RegisterBiomePacket {
    @Shadow(remap = false) public static int nextID() { return 0;}

    @Inject(
            method = "init",
            at = @At("TAIL"),
            remap = false
    )
    private static void srpmixins_registerBiomePacket(CallbackInfo ci){
        SRPMain.network.registerMessage(SRPMixinsPacketBiomeChange.Handler.class, SRPMixinsPacketBiomeChange.class, nextID(), Side.CLIENT);
    }
}
