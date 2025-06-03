package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolArmorBase;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WeaponToolArmorBase.class)
public abstract class SentientEvolutionDisable_Armor {

    @Inject(
            method = "onUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getTagCompound()Lnet/minecraft/nbt/NBTTagCompound;"),
            cancellable = true
    )
    private void srpmixins_disableEvolve(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, CallbackInfo ci, @Local boolean flag){
        ci.cancel();
    }

    @WrapWithCondition(
            method = "addInformation",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z")
    )
    @SideOnly(Side.CLIENT)
    private boolean srpmixins_removeSentientTooltip(List instance, Object e) {
        return false;
    }
}