package srpmixins.compat;

import energon.srpextra.entity.IWaterParasite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import srpmixins.config.providers.SRPMobConfigProvider;
import energon.srpextra.util.config.SRPEConfigMobs;

public class SRPExtraCompat {
    public static void init(){
        if(SRPEConfigMobs.activeFeralWolf) {
            SRPMobConfigProvider.mobNameToParaIdMap.remove("fer_wolf");
            SRPMobConfigProvider.mobNameToParaIdMap.put("feral_wolf", 300);
            SRPMobConfigProvider.paraIdToMobName.replace(300, "feral_wolf");
        }
    }

    public static int countWaterParasites(World world){
        return world.countEntities(IWaterParasite.class);
    }

    public static boolean isWaterParasite(Entity entity) {
        return entity instanceof IWaterParasite;
    }

    public static boolean isWaterParasite(Class<? extends EntityLiving> entityClass) {
        return IWaterParasite.class.isAssignableFrom(entityClass);
    }
}
