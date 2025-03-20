package srpmixins.mixin.configparsefix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityWymo;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPConfigProvider;
import srpmixins.util.Triple;

import java.util.*;
import java.util.stream.Collectors;

@Mixin(SRPEventHandlerBus.class)
public abstract class SRPEventHandlerBusMixin_ParasiteLoot {
    @WrapOperation(
            method = "setLoot",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/util/handlers/SRPEventHandlerBus;loot(Lnet/minecraftforge/event/entity/living/LivingDropsEvent;[Ljava/lang/String;)V"),
            remap = false
    )
    private void srpmixins_overwriteLootPoolLogic(SRPEventHandlerBus instance, LivingDropsEvent event, String[] drop, Operation<Void> original, @Local EntityParasiteBase parasite) {
        int paraId = parasite.getParasiteIDRegister();
        if(paraId == 37 && parasite instanceof EntityWymo)
            paraId = SRPConfigProvider.WYMO_FAKEID; //hotfix for WYMO id being duplicated without overwriting it fully
        List<Triple<ItemStack, Integer, Boolean>> lootPool = SRPConfigProvider.lootPools.get(paraId);
        if (lootPool == null) {
            //Default behavior
            original.call(instance, event, drop);
            return;
        }

        Random rand = parasite.getRNG();
        lootPool.stream()
                .filter(Triple::getRight)
                .forEach(loot -> srpmixins$addLootToDrops(rand, loot, event));

        List<Triple<ItemStack, Integer, Boolean>> remainingLoot = lootPool.stream()
                .filter(pool -> !pool.getRight())
                .collect(Collectors.toList());
        if(!remainingLoot.isEmpty())
            srpmixins$addLootToDrops(rand, remainingLoot.get(rand.nextInt(remainingLoot.size())), event);

        remainingLoot.clear();
    }

    @Unique
    private static void srpmixins$addLootToDrops(Random rand, Triple<ItemStack, Integer, Boolean> loot, LivingDropsEvent event){
        if (rand.nextInt(100) < loot.getMiddle()) {
            ItemStack newStack = loot.getLeft().copy();
            newStack.setCount(rand.nextInt(newStack.getCount()));

            BlockPos pos = event.getEntity().getPosition();
            event.getDrops().add(new EntityItem(event.getEntity().getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), newStack));
        }
    }
}
