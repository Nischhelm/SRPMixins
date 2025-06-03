package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolArmorBase;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(WeaponToolArmorBase.class)
public abstract class SentientSRPHitsTooltipRemove {
    @Shadow(remap = false) public abstract Item getNext();

    @WrapWithCondition(
            method = "addInformation",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0)
    )
    @SideOnly(Side.CLIENT)
    private boolean srpmixins_removeSentientTooltip(List instance, Object e) {
        return this.getNext() != null;
    }
}