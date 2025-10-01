package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.init.SRPItems;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolRangeBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;

@Mixin(WeaponToolRangeBase.class)
public abstract class RepairableBow extends Item {
    @Override
    public boolean getIsRepairable(ItemStack toRepair, @Nonnull ItemStack repair) {
        return toRepair.getItem() instanceof WeaponToolRangeBase && repair.getItem().equals(SRPItems.tendrons);
    }
}
