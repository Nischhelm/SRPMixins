package srpmixins.mixin.parabiome;

import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.Random;

@Mixin(ParasiteEventWorld.class)
public class FixBiomeSpreadLimit {
    @Shadow(remap = false) public static int blockParasiteCount;

    @Inject(
            method = "spreadBiomeBlockStain",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private static void stopIfOverLimit(World worldIn, BlockPos pos, Random rand, CallbackInfo ci){
        if(SRPMixinsConfigHandler.various.fixBiomeSpreadingLimit)
            if(blockParasiteCount > SRPConfig.BlockParasiteLimit)
                ci.cancel();
    }
}
