package srpmixins.handlers;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

public class XpPerPhaseHandler {
    @SubscribeEvent
    public static void onExperienceDrop(LivingExperienceDropEvent event){
        if(!(event.getEntityLiving() instanceof EntityParasiteBase)) return;

        World world = event.getEntity().world;
        byte phase = SRPSaveDataInterface.get(world, event.getAttackingPlayer(), null).getEvolutionPhase(world.provider.getDimension());
        if(phase < 0) return;
        if(phase >= SRPMixinsConfigHandler.phasepoints.xpMultis.length) return;

        float multi = SRPMixinsConfigHandler.phasepoints.xpMultis[phase];
        if(multi == 1) return;

        event.setDroppedExperience((int) (event.getDroppedExperience() * multi));
    }
}
