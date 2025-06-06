package srpmixins.mixin.playerphases;

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

    //Since 1.10 fixed to actually be "can lose" instead of previously "cant lose"
    @Accessor(value = "dimEPcanLossPoints", remap = false)
    ArrayList<Boolean> getDimEPcanLossPoints();


    @Accessor(value = "simRegId", remap = false)
    ArrayList<Byte> getSimRegId();
    @Accessor(value = "simRegIdTimes", remap = false)
    ArrayList<Integer> getSimRegIdTimes();
    @Accessor(value = "lockedParasites", remap = false)
    ArrayList<Integer> getLockedParasites();

    @Accessor(value = "dimEPcurrentCodes", remap = false)
    ArrayList<String> getDimEPcurrentCodes();
    @Accessor(value = "dimEPcurrentCodesDur", remap = false)
    ArrayList<String> getDimEPcurrentCodesDur();
    @Accessor(value = "dimGeneration", remap = false)
    ArrayList<Byte> getDimGeneration();
    @Accessor(value = "dimGenerationTime", remap = false)
    ArrayList<Integer> getDimGenerationTime();
    @Accessor(value = "dimUpdates", remap = false)
    ArrayList<Integer> getDimUpdates();
    @Accessor(value = "dimEIVHealth", remap = false)
    ArrayList<Integer> getDimEIVHealth();
    @Accessor(value = "dimEIVArea", remap = false)
    ArrayList<Integer> getDimEIVArea();

}