package srpmixins.mixin.adaptationoverhaul;

import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolArmorBase;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.capability.adaptation.CapabilityAdaptationHandler;
import srpmixins.capability.adaptation.ICapabilityAdaptation;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Mixin(WeaponToolArmorBase.class)
public class ArmorTooltip {
    @Unique ICapabilityAdaptation srpmixins$adaCap = null;

    @Inject(
            method = "addInformation",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getTagCompound()Lnet/minecraft/nbt/NBTTagCompound;")
    )
    private void srpmixins_overwriteLists(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn, CallbackInfo ci, @Local(ordinal = 0) ArrayList<String> resistanceS, @Local(ordinal = 1) ArrayList<Integer> resistanceI){
        srpmixins$adaCap = stack.getCapability(CapabilityAdaptationHandler.CAP_ADAPTATION, null);
        if(srpmixins$adaCap != null) {
            resistanceS.addAll(srpmixins$adaCap.getNames());
            resistanceI.addAll(srpmixins$adaCap.getCounts());
        }
    }

    @ModifyExpressionValue(
            method = "addInformation",
            at = @At(value = "FIELD", target = "Ljava/math/RoundingMode;DOWN:Ljava/math/RoundingMode;")
    )
    private RoundingMode srpmixins_roundCorrectly(RoundingMode original){
        return RoundingMode.HALF_UP;
    }

    @WrapOperation(
            method = "addInformation",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;)Z")
    )
    private boolean srpmixins_changeTooltip(NBTTagCompound instance, String key, Operation<Boolean> original, @Local(argsOnly = true) ItemStack stack) {
        boolean newHandling = srpmixins$adaCap != null && srpmixins$adaCap.getTrackCount() > 0;
        srpmixins$adaCap = null;
        return newHandling;
    }
}
