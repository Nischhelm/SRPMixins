package srpmixins.loot;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *  This LootCondition is true if the current evolution phase is in the range of acceptable phases provided here.
 *  Usage:
 *  {
 *      "condition": "srp_phase",
 *      "phase_range": 5
 *  }
 * or
 *  {
 *      "condition": "srp_phase",
 *      "phase_range": {
 *          "min": 0,
 *          "max": 10
 *      }
 *  }
 */
public class SRPPhaseLootCondition implements LootCondition {

    private final RandomValueRange valueRange;

    public SRPPhaseLootCondition(RandomValueRange valueRange) {
        this.valueRange = valueRange;
    }

    @Override
    public boolean testCondition(@Nonnull Random unused, LootContext context) {
        if(context.getLootedEntity() == null) return false;
        SRPSaveData data = SRPSaveDataInterface.get(context.getWorld(), null, context.getLootedEntity().getPosition());
        byte currPhase = data.getEvolutionPhase(context.getWorld().provider.getDimension());
        return this.valueRange.isInRange(currPhase);
    }

    public static class Serializer extends LootCondition.Serializer<SRPPhaseLootCondition> {
        public Serializer() {
            super(new ResourceLocation("srp_phase"), SRPPhaseLootCondition.class);
        }

        @Override
        public void serialize(@Nonnull JsonObject json, @Nonnull SRPPhaseLootCondition lootCondition, @Nonnull JsonSerializationContext context) {
            json.add("phase_range", context.serialize(lootCondition.valueRange));
        }

        @Override
        @Nonnull
        public SRPPhaseLootCondition deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new SRPPhaseLootCondition(JsonUtils.deserializeClass(json, "phase_range", context, RandomValueRange.class));
        }
    }
}
