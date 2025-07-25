package srpmixins.compat;

import energon.srpdeepseadanger.init.DSDEntities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;

public class SRPDeepSeaCompat {
    public static void setPlacementTypesToWater(){
        for(EntityEntry entry : DSDEntities.DSD_ENTITIES)
            EntitySpawnPlacementRegistry.setPlacementType(entry.getEntityClass(), EntityLiving.SpawnPlacementType.IN_WATER);
    }
}

