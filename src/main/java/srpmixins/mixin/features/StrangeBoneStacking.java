package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.init.SRPItems;
import com.dhanantry.scapeandrunparasites.item.ItemBase;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//By nischhelm / RLMixins, modified
@Mixin(SRPItems.class)
public abstract class StrangeBoneStacking {

    @WrapOperation(
            method="init",
            at= @At(value = "NEW", target = "(Ljava/lang/String;IB)Lcom/dhanantry/scapeandrunparasites/item/ItemBase;"),
            remap = false
    )
    private static ItemBase srpmixins_strangeBonesStackToSixteen(String name, int maxStack, byte id, Operation<ItemBase> original){
        if(name.equals("bone")) maxStack = 16;
        return original.call(name, maxStack, id);
    }
}