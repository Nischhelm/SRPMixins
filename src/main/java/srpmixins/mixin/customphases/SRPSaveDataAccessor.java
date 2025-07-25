package srpmixins.mixin.customphases;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;

@Mixin(SRPSaveData.class)
public interface SRPSaveDataAccessor {

    @Accessor(value = "instance", remap = false)
    SRPSaveData getInstance();
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
    @Invoker(value = "checkForUnlock", remap = false)
    void invokeCheckForUnlock(byte phase, int worldId, World w);

    //Should be called: CANT lose points
    @Accessor(value = "dimEPcanLossPoints", remap = false)
    ArrayList<Boolean> getDimEPcanLossPoints();


    @Accessor(value = "simRegId", remap = false)
    ArrayList<Byte> getSimRegId();
    @Accessor(value = "simRegIdTimes", remap = false)
    ArrayList<Integer> getSimRegIdTimes();
    @Accessor(value = "lockedParasites", remap = false)
    ArrayList<Integer> getLockedParasites();
}