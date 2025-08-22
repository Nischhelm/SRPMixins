package srpmixins.handlers;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CothNoDropsOnConversion {
    public static final DamageSource COTH = new DamageSource("srpmixins_coth");

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDrops(LivingDropsEvent event) {
        if(event.getSource() != COTH) return;
        event.setCanceled(true);
    }
}