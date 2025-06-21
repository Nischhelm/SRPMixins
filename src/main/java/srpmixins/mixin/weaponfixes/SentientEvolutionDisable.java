package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolMeleeBase;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WeaponToolMeleeBase.class)
public abstract class SentientEvolutionDisable {

    @Inject(
            method = "hitEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getTagCompound()Lnet/minecraft/nbt/NBTTagCompound;"),
            cancellable = true
    )
    private void srpmixins_disableIncrementSRPKills(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, CallbackInfoReturnable<Boolean> cir, @Local boolean flag){
        cir.setReturnValue(flag);
    }

    @WrapOperation(
            method = "onUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;getInteger(Ljava/lang/String;)I")
    )
    private int srpmixins_disableEvolve(NBTTagCompound instance, String key, Operation<Integer> original){
        return 0;
    }

    @ModifyExpressionValue(
            method = "addInformation",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getTagCompound()Lnet/minecraft/nbt/NBTTagCompound;")
    )
    @SideOnly(Side.CLIENT)
    private NBTTagCompound srpmixins_removeSentientTooltip(NBTTagCompound original) {
        return null;
    }
}