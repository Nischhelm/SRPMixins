package srpmixins.compat;

import energon.srpdeepseadanger.entity.monster.adapted.EntityHorgoAdapted;
import energon.srpdeepseadanger.entity.monster.adapted.EntityLumAdapted;
import energon.srpdeepseadanger.entity.monster.deterrent.nexus.EntityVeru;
import energon.srpdeepseadanger.entity.monster.deterrent.nexus.EntityVeruSII;
import energon.srpdeepseadanger.entity.monster.deterrent.nexus.EntityVeruSIII;
import energon.srpdeepseadanger.entity.monster.deterrent.nexus.EntityVeruSIV;
import energon.srpdeepseadanger.entity.monster.feral.EntityFerDolphin;
import energon.srpdeepseadanger.entity.monster.feral.EntityFerFish;
import energon.srpdeepseadanger.entity.monster.hijacked.EntityHiElderGuardian;
import energon.srpdeepseadanger.entity.monster.hijacked.EntityHiGuardian;
import energon.srpdeepseadanger.entity.monster.inborn.*;
import energon.srpdeepseadanger.entity.monster.infected.EntityInfDolphin;
import energon.srpdeepseadanger.entity.monster.infected.EntityInfDrowned;
import energon.srpdeepseadanger.entity.monster.infected.EntityInfFish;
import energon.srpdeepseadanger.entity.monster.infected.head.EntityInfDolphinHead;
import energon.srpdeepseadanger.entity.monster.primitive.EntityHorgo;
import energon.srpdeepseadanger.entity.monster.pure.EntityIrol;
import energon.srpdeepseadanger.entity.monster.tendril.EntityTendrilHorgo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;

public class SRPDeepSeaCompat {
    public static void setPlacementTypesToWater(){
        EntitySpawnPlacementRegistry.setPlacementType(EntityRyba.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityRekin.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityOso.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityInfDrowned.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityInfDolphin.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityInfDolphinHead.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityFerDolphin.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityInfFish.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityFerFish.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityHiGuardian.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityHiElderGuardian.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityHorgo.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityHorgoAdapted.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityLumAdapted.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityTendrilHorgo.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityVeru.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityVeruSII.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityVeruSIII.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityVeruSIV.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityIrol.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntitySethol.class, EntityLiving.SpawnPlacementType.IN_WATER);
        EntitySpawnPlacementRegistry.setPlacementType(EntityFoho.class, EntityLiving.SpawnPlacementType.IN_WATER);
    }
}

