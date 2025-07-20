package srpmixins.compat;

import mcjty.incontrol.rules.support.GenericRuleEvaluator;
import mcjty.incontrol.rules.support.RuleKeys;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.function.BiFunction;
import java.util.function.Function;

public class IncontrolCompat {
    private void addMaxCountCheck(AttributeMap map) {
        String json = (String)map.get(RuleKeys.MAXCOUNT);
        GenericRuleEvaluator.CountInfo info = this.parseCountInfo(json);
        BiFunction<World, Entity, Integer> counter = this.getCounter(info);
        Function<World, Integer> amountAdjuster = this.getAmountAdjuster(info, info.amount);
        this.checks.add((event, query) -> {
            World world = query.getWorld(event);
            Entity entity = query.getEntity(event);
            int count = (Integer)counter.apply(world, entity);
            int amount = (Integer)amountAdjuster.apply(world);
            return count < amount;
        });
    }
}
