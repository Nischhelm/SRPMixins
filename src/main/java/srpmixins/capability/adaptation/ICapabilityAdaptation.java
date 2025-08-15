package srpmixins.capability.adaptation;

import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Random;

public interface ICapabilityAdaptation {
    int getTrackCount();
    boolean hasAdaptation(String damageTypeName);
    boolean tryAddAdaptation(String damageTypeName, Random rand, byte blacklistType);
    void tryIncreaseAdaptation(String damageTypeName, Random rand);
    float getReduction(String damage);

    void resetAdaptations();

    NBTTagCompound writeToNBT();
    void readFromNBT(NBTTagCompound nbt);

    List<String> getNames();
    List<Integer> getCounts();

    void copyAdaptationsFrom(ICapabilityAdaptation adaCap);
}
