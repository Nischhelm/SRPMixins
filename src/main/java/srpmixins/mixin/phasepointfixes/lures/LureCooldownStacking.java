package srpmixins.mixin.phasepointfixes.lures;

import com.dhanantry.scapeandrunparasites.block.BlockEvolutionLure;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
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
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(BlockEvolutionLure.class)
public abstract class LureCooldownStacking {

    @ModifyArg(
            method = "onBlockActivated",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setCooldown(ILnet/minecraft/world/World;I)V", remap = false),
            index = 0
    )
    private int lureCooldownStacking(int original, @Local(argsOnly = true) World world, @Local SRPSaveData data) {
        if (SRPMixinsConfigHandler.lures.lureCooldownStacking)
            return original + data.getCooldown(world, world.provider.getDimension());
        return original;
    }

    @Unique private boolean didCooldownOperation = false;
    @Unique private boolean didSendMsgOperation = false;

    @WrapWithCondition(
            method = "onBlockActivated",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;setCooldown(ILnet/minecraft/world/World;I)V", remap = false)
    )
    private boolean onlySetCooldownOnce(SRPSaveData instance, int i, World world, int in){
        if(!SRPMixinsConfigHandler.lures.fixCooldownOverflow || !didCooldownOperation){
            didCooldownOperation = true;
            return true;
        }
        return false;
    }

    @WrapWithCondition(
            method = "onBlockActivated",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;sendStatusMessage(Lnet/minecraft/util/text/ITextComponent;Z)V")
    )
    private boolean onlySendMessageOnce(EntityPlayer instance, ITextComponent chatComponent, boolean actionBar){
        //Don't count the first msg for point reduction, even though that should never run both at the same time
        if(chatComponent instanceof TextComponentTranslation && ((TextComponentTranslation) chatComponent).getKey().equals("message.srparasites.lureb")){
            if(!SRPMixinsConfigHandler.lures.fixCooldownOverflow || !didSendMsgOperation){
                didSendMsgOperation = true;
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
    private void resetBooleans(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir){
        didCooldownOperation = false;
        didSendMsgOperation = false;
    }
}