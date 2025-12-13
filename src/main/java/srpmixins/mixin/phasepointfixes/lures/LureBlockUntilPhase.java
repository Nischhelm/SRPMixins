package srpmixins.mixin.phasepointfixes.lures;

import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Debug(export = true)
@Mixin(BlockEvolutionLure.class)
public abstract class LureBlockUntilPhase extends Block {
    public LureBlockUntilPhase(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    @Shadow(remap = false) @Final public static PropertyEnum<BlockEvolutionLure.EnumType> VARIANT;

    @WrapOperation(
            method = "checkBlocks",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;")
    )
    private IBlockState srpmixins_checkIfCorrectPhase(
            World world, BlockPos pos,
            Operation<IBlockState> original,
            @Share("evoPhase") LocalRef<Byte> evoPhase
    ){
        IBlockState state = original.call(world, pos);
        if(state.getBlock() == SRPBlocks.evolutionLure) {
            if(evoPhase.get() == null) evoPhase.set(SRPSaveDataInterface.get(world, null, pos).getEvolutionPhase(world.provider.getDimension()));
            if(srpmixins$currentPhaseisBelowLurePhase(evoPhase.get(), state)) return Blocks.AIR.getDefaultState();
        }
        return state;
    }

    @ModifyExpressionValue(
            method = "onBlockActivated",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;getEvolutionPhase(I)B", remap = false)
    )
    private byte srpmixins_checkIfCorrectPhase(byte currPhase, World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, @Cancellable CallbackInfoReturnable<Boolean> cir){
        if(srpmixins$currentPhaseisBelowLurePhase(currPhase, state))
            cir.setReturnValue(super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ));
        return currPhase;
    }

    @Unique
    private static boolean srpmixins$currentPhaseisBelowLurePhase(byte currPhase, IBlockState state){
        int lurePhase = state.getValue(VARIANT).ordinal()+1;
        //true if the current phase is too low to use this lure
        return currPhase < lurePhase;
    }
}
