package srpmixins.mixin.lostcitytweaks;

import com.dhanantry.scapeandrunparasites.block.BlockBase;
import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(BlockEvolutionLure.class)
public abstract class LCLureDisable extends BlockBase {
    public LCLureDisable(Material material, String name, float hardness, boolean creative, boolean tickRandom) {
        super(material, name, hardness, creative, tickRandom);
    }

    @Inject(
            method="onBlockActivated",
            at= @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/block/BlockEvolutionLure;checkBlocks(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lcom/dhanantry/scapeandrunparasites/block/BlockEvolutionLure$EnumType;)Z", remap = false),
            cancellable = true
    )
    public void srpmixins_lureDisable(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir){
        if(!SRPMixinsConfigHandler.modcompat.disableLuresInLC) return;

        if (playerIn.dimension == 111) {
            playerIn.sendStatusMessage(new TextComponentTranslation("srpmixins.msg.hivewhispers"), true);
            worldIn.setBlockState(pos, SRPBlocks.dodN.getDefaultState());
            cir.setReturnValue(super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ));
        }
    }
}