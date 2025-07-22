package srpmixins.compat;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import energon.srpextra.entity.IWaterParasite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import srpmixins.config.providers.SRPMobConfigProvider;
import energon.srpextra.util.config.SRPEConfigMobs;

public class SRPExtraCompat {
    public static void init(){
        SRPMobConfigProvider.registerParasite("hijacked_creeper", -1, "HIJACKED");
        SRPMobConfigProvider.registerParasite("hijacked_creeper_head", -10001, "HIJACKED"); //tmp id
        SRPMobConfigProvider.registerParasite("hijacked_skeleton", -2, "HIJACKED");
        SRPMobConfigProvider.registerParasite("hijacked_skeleton_head", -2, "HIJACKED"); //tmp id
        SRPMobConfigProvider.registerParasite("hijacked_skeleton_stray", -10003, "HIJACKED");
        SRPMobConfigProvider.registerParasite("stalker", -4, "PRIMITIVE");
        SRPMobConfigProvider.registerParasite("sim_witch", -5, "ASSIMILATED");
        SRPMobConfigProvider.registerParasite("sim_witch_head", -10005, "ASSIMILATED"); //tmp id
        SRPMobConfigProvider.registerParasite("sim_vindicator", -6, "ASSIMILATED");
        SRPMobConfigProvider.registerParasite("sim_vindicator_head", -10006, "ASSIMILATED"); //tmp id
        SRPMobConfigProvider.registerParasite("sim_evoker", -7, "ASSIMILATED");
        SRPMobConfigProvider.registerParasite("sim_ocelot", -8, "ASSIMILATED");
        SRPMobConfigProvider.registerParasite("sim_ocelot_head", -10008, "ASSIMILATED"); //tmp id
        SRPMobConfigProvider.registerParasite("ada_vermin", -9, "ADAPTED");

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
