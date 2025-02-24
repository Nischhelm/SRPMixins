package srpmixins.mixin.adaptationoverhaul;

import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;


@Mixin(SRPEventHandlerBus.class)
public class ArmorAdaptationCancel {
    @Redirect(
            method = "entityHurt",
            at = @At(value = "INVOKE", target = "Ljava/util/Iterator;hasNext()Z"),
            remap = false
    )
    private boolean cancelOldHandling(Iterator<ItemStack> instance){
        return false;
    }
}
