package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolMeleeBase;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeaponToolMeleeBase.class)
public abstract class SentientEvolutionNBTFix {

    @Inject(
            method = "onUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/item/EntityItem;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V")
    )
    private void srpmixins_writeNBTtoSentient(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, CallbackInfo ci, @Local(name = "stackW") ItemStack stackNew) {
        NBTTagCompound oldTags = stack.getTagCompound();
        if(oldTags != null) stackNew.setTagCompound(oldTags.copy());
    }
}