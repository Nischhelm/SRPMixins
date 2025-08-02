package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SRPWorldData.class)
public interface SRPWorldDataAccessor {
    @Invoker(value = "convertDayToAgeNode", remap = false)
    int invokeConvertDayToAgeNode(int zeroAnyway, int daysExisted);
}
