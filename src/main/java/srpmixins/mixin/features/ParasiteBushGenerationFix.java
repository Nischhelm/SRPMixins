package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.world.gen.feature.WorldGenParasiteBush;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

//By fonnymunkey / RLMixins, unmodified
@Mixin(WorldGenParasiteBush.class)
public abstract class ParasiteBushGenerationFix {
	
	@Inject(
			method = "generate",
			at = @At("HEAD"),
			remap = false,
			cancellable = true
	)
	public void srpmixins_dontGenerateBushIfNotLoaded(World worldIn, Random rand, BlockPos position, CallbackInfoReturnable<Boolean> cir) {
		if(!worldIn.isAreaLoaded(position, 10)) cir.setReturnValue(false);
	}
	
	@Inject(
			method = "VinGen",
			at = @At("HEAD"),
			remap = false,
			cancellable = true
	)
	public void srpmixins_dontGenerateVineIfNotLoaded(World worldIn, Random rand, BlockPos position, CallbackInfoReturnable<Boolean> cir) {
		if(!worldIn.isAreaLoaded(position, 10)) cir.setReturnValue(false);
	}
}