package srpmultiplier.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolMeleeBase;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

import java.util.List;

@Mixin(WeaponToolMeleeBase.class)
public abstract class SentientEvolutionNBTFix {
    @Shadow(remap = false)
    public abstract Item getNext();

    @Redirect(
            method = "func_77663_a",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;func_72838_d(Lnet/minecraft/entity/Entity;)Z"),
            remap = false
    )
    private boolean writeNBTtoSentient(World world, Entity entity, @Local(argsOnly = true) ItemStack stack) {
        if (SRPMultiplierConfigHandler.weapons.fixSentientEvolutionNBT) {
            NBTTagCompound oldTags = stack.getTagCompound();
            ((EntityItem) entity).getItem().setTagCompound(oldTags);
        }
        return world.spawnEntity(entity);
    }

    @Inject(
            method = "func_77624_a",
            at = @At(value = "HEAD"),
            cancellable = true,
            remap = false
    )
    private void removeSentientTooltip(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn, CallbackInfo ci) {
        if (!SRPMultiplierConfigHandler.weapons.removeSentientSRPKillsTooltip) return;
        if (getNext() != null) return;
        ci.cancel();
    }
}