package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolMeleeBase;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(WeaponToolMeleeBase.class)
public abstract class SentientEvolutionNBTFix {
    @Shadow(remap = false) public abstract Item getNext();

    @Inject(
            method = "func_77644_a",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;func_77978_p()Lnet/minecraft/nbt/NBTTagCompound;"),
            remap = false,
            cancellable = true
    )
    private void disableIncrementSRPKills(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, CallbackInfoReturnable<Boolean> cir, @Local boolean flag){
        if(SRPMixinsConfigHandler.weapons.disableSentientEvolution)
            cir.setReturnValue(flag);
    }

    @Inject(
            method = "func_77663_a",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;func_77978_p()Lnet/minecraft/nbt/NBTTagCompound;"),
            remap = false,
            cancellable = true
    )
    private void disableEvolve(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, CallbackInfo ci, @Local boolean flag){
        if(SRPMixinsConfigHandler.weapons.disableSentientEvolution)
            ci.cancel();
    }

    @Redirect(
            method = "func_77663_a",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;func_72838_d(Lnet/minecraft/entity/Entity;)Z"),
            remap = false
    )
    private boolean writeNBTtoSentient(World world, Entity entity, @Local(argsOnly = true) ItemStack stack) {
        if (SRPMixinsConfigHandler.weapons.fixSentientEvolutionNBT) {
            NBTTagCompound oldTags = stack.getTagCompound();
            ((EntityItem) entity).getItem().setTagCompound(oldTags);
        }
        return world.spawnEntity(entity);
    }

    @Redirect(
            method = "func_77624_a",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;func_77978_p()Lnet/minecraft/nbt/NBTTagCompound;"),
            remap = false
    )
    @SideOnly(Side.CLIENT)
    private NBTTagCompound removeSentientTooltip(ItemStack instance) {
        if (SRPMixinsConfigHandler.weapons.disableSentientEvolution) return null;
        if (SRPMixinsConfigHandler.weapons.removeSentientSRPKillsTooltip && getNext() == null)
            return null;
        return instance.getTagCompound();
    }
}