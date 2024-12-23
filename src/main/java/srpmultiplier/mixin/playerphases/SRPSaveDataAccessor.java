package srpmultiplier.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.ArrayList;

@Mixin(SRPSaveData.class)
public interface SRPSaveDataAccessor {

    @Accessor(value = "dimEPid", remap = false)
    ArrayList<Integer> getDimEPid();
    @Accessor(value = "dimEPtotalKills", remap = false)
    ArrayList<Integer> getDimEPtotalKills();
    @Accessor(value = "dimEPevolution", remap = false)
    ArrayList<Byte> getDimEPevolution();
    @Accessor(value = "dimEPtimeEvolution", remap = false)
    ArrayList<Integer> getDimEPtimeEvolution();
    @Accessor(value = "dimEPcanGainPoints", remap = false)
    ArrayList<Boolean> getDimEPcanGainPoints();

    //Should be called: CANT lose points
    @Accessor(value = "dimEPcanLossPoints", remap = false)
    ArrayList<Boolean> getDimEPcanLossPoints();
}