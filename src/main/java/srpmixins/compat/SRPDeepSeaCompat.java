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
import srpmixins.config.providers.SRPMobConfigProvider;

public class SRPDeepSeaCompat {
    public static void init(){
        SRPMobConfigProvider.registerParasite("swimmer", -10, "INBORN");
        SRPMobConfigProvider.registerParasite("leecher", -11, "INBORN");
        SRPMobConfigProvider.registerParasite("sim_drowned", -23, "ASSIMILATED");
        SRPMobConfigProvider.registerParasite("sim_dolphin", -24, "ASSIMILATED");
        SRPMobConfigProvider.registerParasite("sim_dolphin_head", -280, "ASSIMILATED"); //Not actually like that in code yet
        SRPMobConfigProvider.registerParasite("sim_fish", -25, "ASSIMILATED");
        SRPMobConfigProvider.registerParasite("fer_dolphin", -536, "FERAL"); //Not actually like that in code yet
        SRPMobConfigProvider.registerParasite("pri_hammerhead", -26, "PRIMITIVE");
        SRPMobConfigProvider.registerParasite("ada_hammerhead", -27, "ADAPTED");
        SRPMobConfigProvider.registerParasite("sprouter_si", -28, "NEXUS");
        SRPMobConfigProvider.registerParasite("sprouter_sii", -29, "NEXUS");
        SRPMobConfigProvider.registerParasite("sprouter_siii", -30, "NEXUS");
        SRPMobConfigProvider.registerParasite("sprouter_siv", -31, "NEXUS");
        SRPMobConfigProvider.registerParasite("plankton", -32, "INBORN");
        SRPMobConfigProvider.registerParasite("carrier_sea", -33, "INBORN");
        SRPMobConfigProvider.registerParasite("bomber_mini", -34, "INBORN");
        SRPMobConfigProvider.registerParasite("supporter", -35, "PURE");
        SRPMobConfigProvider.registerParasite("hi_guardian", -38, "HIJACKED");
        SRPMobConfigProvider.registerParasite("hi_elder_guardian", -39, "HIJACKED");
        SRPMobConfigProvider.registerParasite("fer_fish", -40, "FERAL"); //Not actually like that in code yet
        SRPMobConfigProvider.registerParasite("ada_devourer", -41, "ADAPTED"); //Not actually like that in code yet
        SRPMobConfigProvider.registerParasite("tendril_hammerhead", -283);
    }

    public static void postInit(){
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

