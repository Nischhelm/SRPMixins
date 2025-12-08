package srpmixins.mixin;

import com.dhanantry.scapeandrunparasites.world.gen.feature.*;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.SRPMixins;

import java.util.Random;

@Mixin(value = {
        WorldGenParasiteTree.class,
        WorldGenParasiteTreeThin.class,
        WorldGenParasiteBigBall.class,
        WorldGenParasiteBush.class,

        WorldGenParasiteBall.class,
        WorldGenParasiteMouth.class,
        WorldGenParasiteSpine.class,
        WorldGenParasiteTallFlower.class,
        WorldGenParasiteTenFlower.class
})
public abstract class WoldGenParasiteTreeMixin {
    @ModifyReturnValue(
            method = "generate",
            at = @At("RETURN")
    )
    private boolean mixin(boolean original, World worldIn, Random rand, BlockPos position){
        //SRPMixins.LOGGER.info("Para Feature {} returning {} at {} {} {}", this.getClass().getSimpleName() , original, position.getX(), position.getY(), position.getZ());
        return original;
    }
}
