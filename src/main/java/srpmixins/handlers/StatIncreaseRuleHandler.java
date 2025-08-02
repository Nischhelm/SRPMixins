package srpmixins.handlers;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.rules.ruleset.StatIncreaseRuleSet;

import java.util.HashMap;
import java.util.Map;

public class StatIncreaseRuleHandler {
    @SubscribeEvent
    public static void onJoinWorld(EntityJoinWorldEvent event){
        //I would have done it as a mixin in EntityParasiteBase.onInitialSpawn but thats before the skin is set (in onInitialSpawn of the sub class)
        if(event.getWorld().isRemote) return;
        if(event.isCanceled()) return;
        if(!(event.getEntity() instanceof EntityParasiteBase)) return;

        EntityParasiteBase para = (EntityParasiteBase) event.getEntity();
        if(para.getTags().contains("srpmixins_statrules_applied")) return;

        if (!StatIncreaseRuleSet.INSTANCE.hasNoRules()) {
            int paraId = para.getParasiteIDRegister();

            Map<String, Object> actualValues = new HashMap<>();
            actualValues.put("dim", event.getWorld().provider.getDimension());
            actualValues.put("phase", para.getPhaseCreated());
            actualValues.put("mob", paraId);
            actualValues.put("group", SRPMobConfigProvider.getParaGroup(paraId));
            actualValues.put("variant", para.getSkin());
            actualValues.put("nodes", SRPWorldData.get(event.getWorld()).getNodes("x").size());

            para.getAttributeMap().applyAttributeModifiers(StatIncreaseRuleSet.INSTANCE.getAllModifiers(actualValues));
        }

        para.getTags().add("srpmixins_statrules_applied");
    }
}
