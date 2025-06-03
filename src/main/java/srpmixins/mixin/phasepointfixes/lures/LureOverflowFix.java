package srpmixins.mixin.phasepointfixes.lures;

import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEvolutionLure.class)
public abstract class LureOverflowFix {
    @Unique private boolean srpmixins$didCooldownOperation = false;
    @Unique private boolean srpmixins$didSendMsgOperation = false;

    @WrapWithCondition(
            method = "onBlockActivated",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setCooldown(ILnet/minecraft/world/World;IZ)V", remap = false)
    )
    private boolean srpmixins_onlySetCooldownOnce(SRPSaveData instance, int i, World world, int in, boolean worldIn){
        if(!srpmixins$didCooldownOperation){
            srpmixins$didCooldownOperation = true;
            return true;
        }
        return false;
    }

    @WrapWithCondition(
            method = "onBlockActivated",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;sendStatusMessage(Lnet/minecraft/util/text/ITextComponent;Z)V")
    )
    private boolean srpmixins_onlySendMessageOnce(EntityPlayer instance, ITextComponent chatComponent, boolean actionBar){
        //Don't count the first msg for point reduction, even though that should never run both at the same time
        if(chatComponent instanceof TextComponentTranslation && ((TextComponentTranslation) chatComponent).getKey().equals("message.srparasites.lureb")){
            if(!srpmixins$didSendMsgOperation){
                srpmixins$didSendMsgOperation = true;
                return true;
            }
            return false;
        }
        return true;
    }

    @Inject(
            method = "onBlockActivated",
            at = @At("RETURN")
    )
    private void srpmixins_resetBooleans(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir){
        srpmixins$didCooldownOperation = false;
        srpmixins$didSendMsgOperation = false;
    }
}