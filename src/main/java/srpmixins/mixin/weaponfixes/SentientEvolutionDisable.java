package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolMeleeBase;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(WeaponToolMeleeBase.class)
public abstract class SentientEvolutionDisable {

    @Inject(
            method = "hitEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getTagCompound()Lnet/minecraft/nbt/NBTTagCompound;"),
            cancellable = true
    )
    private void disableIncrementSRPKills(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, CallbackInfoReturnable<Boolean> cir, @Local boolean flag){
        cir.setReturnValue(flag);
    }

    @Inject(
            method = "onUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getTagCompound()Lnet/minecraft/nbt/NBTTagCompound;"),
            cancellable = true
    )
    private void disableEvolve(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, CallbackInfo ci, @Local boolean flag){
        ci.cancel();
    }

    @ModifyExpressionValue(
            method = "addInformation",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getTagCompound()Lnet/minecraft/nbt/NBTTagCompound;")
    )
    @SideOnly(Side.CLIENT)
    private NBTTagCompound removeSentientTooltip(NBTTagCompound original) {
        return null;
    }
}