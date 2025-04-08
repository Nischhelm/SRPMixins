package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPMalleable;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;

@Mixin(EntityPMalleable.class)
public abstract class FoodStealBlacklist {
    @WrapOperation(
            method = "attackEntityAsMobFood",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;")
    )
    private Item srpmixins_blacklistSomeFoods(ItemStack stack, Operation<Item> original) {
        Item item = original.call(stack); //stack.getItem();
        if(!(item instanceof ItemFood)) return item;
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(item);
        if(loc == null) return item;
        String name = loc.toString();
        int metadata = stack.getMetadata();
        boolean isInList = SRPMixinsConfigProvider.foodBlacklist.containsKey(name);
        int listedMetadata = SRPMixinsConfigProvider.foodBlacklist.getOrDefault(name, -2);

        //true if item is in list with either metadata -1 (no metadata defined, any) or listed with the current metadata
        //false if item is not in list or listed but with different metadata
        boolean metadataIsListed = isInList && (listedMetadata == -1 || listedMetadata == metadata);

        //if item is listed with its metadata (or any) and it's a blacklist, return AIR, fail the isFood test after this
        //or if item is not listed at all or listed with different metadata and it's a whitelist, return AIR to also fail
        if(SRPMixinsConfigHandler.various.foodBlacklistIsWhitelist != metadataIsListed) return Items.AIR;
        //otherwise just continue on
        return item;
    }
}
