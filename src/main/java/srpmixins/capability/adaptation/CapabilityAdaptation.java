package srpmixins.capability.adaptation;

import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolArmorBase;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.*;

public class CapabilityAdaptation implements ICapabilityAdaptation {
    private final Map<String, Integer> trackedTypes = new HashMap<>();
    private byte isSentient;

    private NBTTagCompound cachedNBT = null;

    private static final int[] damageCaps = {SRPConfig.livingDamageCap, SRPConfig.sentientDamageCap};
    private static final int[] pointCaps = {SRPConfig.livingPointCap, SRPConfig.sentientPointCap};
    private static final float[] learnChances = {(float) SRPConfig.livingChanceLe, (float) SRPConfig.sentientChanceLe};
    private static final float[] reductionPerPoint = {SRPConfig.livingPointReduction, SRPConfig.sentientPointReduction};

    public CapabilityAdaptation(){this(ItemStack.EMPTY, Collections.emptyList(), Collections.emptyList());}
    public CapabilityAdaptation(ItemStack stack, List<String> oldNames, List<Integer> oldCounts) {
        if (!stack.isEmpty() && stack.getItem() instanceof WeaponToolArmorBase && ((WeaponToolArmorBase) stack.getItem()).canCall())
            this.isSentient = (byte) 1;
        else
            this.isSentient = (byte) 0;

        for (int i = 0; i < oldNames.size(); i++)
            addAdaptation(oldNames.get(i), oldCounts.get(i));
    }

    @Override
    public int getTrackCount() {
        return trackedTypes.size();
    }

    @Override
    public boolean hasAdaptation(String damageTypeName){
        return trackedTypes.containsKey(damageTypeName);
    }

    @Override
    public boolean tryAddAdaptation(String damageTypeName, Random rand, byte blacklistType){
        if (trackedTypes.size() >= damageCaps[isSentient]) return false;
        if (rand.nextDouble() >= learnChances[isSentient]) return false;
        if (isBlacklisted(damageTypeName, blacklistType)) return false;

        addAdaptation(damageTypeName, 0);
        return true;
    }

    private void addAdaptation(String damageTypeName, int startCount){
        trackedTypes.put(damageTypeName, startCount);
        invalidateCache();
    }

    @Override
    public float getReduction(String damageTypeName) {
        int dmgPointCap = pointCaps[isSentient];
        return reductionPerPoint[isSentient] * Math.min(trackedTypes.get(damageTypeName), dmgPointCap);
    }

    @Override
    public void resetAdaptations() {
        trackedTypes.clear();
        invalidateCache();
    }

    @Override
    public void tryIncreaseAdaptation(String damageTypeName, Random rand) {
        if (rand.nextDouble() >= learnChances[isSentient]) return;
        trackedTypes.put(damageTypeName,trackedTypes.get(damageTypeName) + 1);
        invalidateCache();
    }

    private static boolean isBlacklisted(String damage, byte type) {
        switch (type) {
            case 0:
                if (ParasiteEventEntity.checkName(damage, SRPConfig.armorDamageTypeBlackListMob, SRPConfig.armorDamageTypeBlackListWhite))
                    return true;
                if(SRPMixinsConfigHandler.adaptation.fixBlacklistCheck)
                    break;
            case 2:
                if (ParasiteEventEntity.checkName(damage, SRPConfig.armorDamageTypeBlackListElse, SRPConfig.armorDamageTypeBlackListWhite))
                    return true;
        }
        return false;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        if(cachedNBT == null) {
            cachedNBT = new NBTTagCompound();
            cachedNBT.setBoolean("sent", isSentient == 1);
            NBTTagList list = new NBTTagList();
            for (Map.Entry<String, Integer> trackerEntry : trackedTypes.entrySet()) {
                NBTTagCompound entryTags = new NBTTagCompound();
                entryTags.setString("type", trackerEntry.getKey());
                entryTags.setInteger("count", trackerEntry.getValue());
                list.appendTag(entryTags);
            }
            cachedNBT.setTag("list", list);
        }
        return cachedNBT;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        isSentient = (byte) (nbt.getBoolean("sent") ? 1 : 0);
        trackedTypes.clear();
        NBTTagList list = nbt.getTagList("list", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound entryTags = list.getCompoundTagAt(i);
            String dmgTypeName = entryTags.getString("type");
            int count = entryTags.getInteger("count");
            addAdaptation(dmgTypeName, count);
        }
    }

    private void invalidateCache() {
        cachedNBT = null;
    }

    @Override
    public List<String> getNames() {
        return new ArrayList<>(trackedTypes.keySet());
    }

    @Override
    public List<Integer> getCounts() {
        return new ArrayList<>(trackedTypes.values());
    }

    @Override
    public void copyAdaptationsFrom(ICapabilityAdaptation adaCap) {
        for(int i=0; i<adaCap.getTrackCount(); i++)
            this.trackedTypes.put(adaCap.getNames().get(i), adaCap.getCounts().get(i));
    }
}
