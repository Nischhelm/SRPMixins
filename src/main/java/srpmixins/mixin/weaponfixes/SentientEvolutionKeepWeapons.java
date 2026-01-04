package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolMeleeBase;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WeaponToolMeleeBase.class)
public abstract class SentientEvolutionKeepWeapons {

    @WrapWithCondition(
            method = "onUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;shrink(I)V")
    )
    private boolean srpmixins_checkForSentient(ItemStack stack, int amount){
        return false; //shrinking later if successful
    }

    @WrapOperation(
            method = "onUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z")
    )
    private boolean srpmixins_writeNBTtoSentient(
            World world, Entity entityItem,
            Operation<Boolean> original,
            @Local(argsOnly = true) int itemSlot,
            @Local(argsOnly = true) Entity entityPlayer,
            @Local(argsOnly = true) ItemStack oldStack,
            @Local(name = "stackW") ItemStack newStack
    ) {
        if(oldStack.getCount() > 1) { //default behavior for stacked items if that ever happens
            oldStack.shrink(1);
            return original.call(world, entityItem);
        } else {
            EntityEquipmentSlot thisSlot = null;
            if(entityPlayer instanceof EntityPlayer){ //This should always be the case except if some mod modifies it
                EntityPlayer player = (EntityPlayer) entityPlayer;
                boolean isMainhand = player.getHeldItemMainhand() == oldStack;
                boolean isOffhand = player.getHeldItemOffhand() == oldStack;
                if(isMainhand) thisSlot = EntityEquipmentSlot.MAINHAND;
                else if(isOffhand) thisSlot = EntityEquipmentSlot.OFFHAND;
            }
            if (thisSlot != null) {
                oldStack.shrink(1);
                entityPlayer.setItemStackToSlot(thisSlot, newStack);
            }
            return false;
        }
    }
}