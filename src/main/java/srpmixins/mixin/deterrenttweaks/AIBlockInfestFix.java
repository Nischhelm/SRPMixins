package srpmixins.mixin.deterrenttweaks;

import com.dhanantry.scapeandrunparasites.block.IMetaName;
import com.dhanantry.scapeandrunparasites.entity.ai.EntityAIBlockInfest;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityAIBlockInfest.class)
public class AIBlockInfestFix {
    @ModifyExpressionValue(
            method = "updateTask",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getBlock()Lnet/minecraft/block/Block;")
    )
    private Block srpmixins_startPropagatingStagedInfestation(Block original) {
        //Shouldn't stop when standing inside infested bush (why do we do this check?)
        return original instanceof IMetaName ? Blocks.AIR : original;
    }
}