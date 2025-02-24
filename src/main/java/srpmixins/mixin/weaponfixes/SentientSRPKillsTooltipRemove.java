package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolMeleeBase;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WeaponToolMeleeBase.class)
public abstract class SentientSRPKillsTooltipRemove {
    @Shadow(remap = false) public abstract Item getNext();

    @ModifyExpressionValue(
            method = "addInformation",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getTagCompound()Lnet/minecraft/nbt/NBTTagCompound;")
    )
    @SideOnly(Side.CLIENT)
    private NBTTagCompound removeSentientTooltip(NBTTagCompound original) {
        if (getNext() == null) return null;
        return original;
    }
}