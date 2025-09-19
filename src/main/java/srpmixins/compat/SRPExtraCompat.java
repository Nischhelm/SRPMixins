package srpmixins.compat;

import energon.srpextra.entity.IWaterParasite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

public class SRPExtraCompat {
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
