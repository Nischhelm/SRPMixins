package srpmixins.compat.hordes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.smileycorp.hordes.common.HordesLogger;
import net.smileycorp.hordes.config.data.ComparableOperation;
import net.smileycorp.hordes.config.data.DataType;
import net.smileycorp.hordes.config.data.conditions.Condition;
import net.smileycorp.hordes.config.data.values.ValueGetter;

import java.util.Random;

public class ParasitePhaseCondition implements Condition {

	protected final ValueGetter<Integer> actualPhase;
	protected final ComparableOperation operation;
	protected final ValueGetter<Integer> comparisonPhase;

	private ParasitePhaseCondition(ComparableOperation operation, ValueGetter<Integer> value) {
		this.actualPhase = new ParasitePhaseGetter();
		this.operation = operation;
		this.comparisonPhase = value;
	}

	public boolean apply(World level, EntityLivingBase entity, EntityPlayerMP player, Random rand) {
		return this.operation.apply(this.actualPhase.get(level, entity, player, rand), this.comparisonPhase.get(level, entity, player, rand));
	}

	public static ParasitePhaseCondition deserialize(JsonElement json) {
		try {
			JsonObject obj = json.getAsJsonObject();
			ComparableOperation operation = ComparableOperation.of(obj.get("operation").getAsString());
			ValueGetter<Integer> value = ValueGetter.readValue(DataType.INT,  obj.get("value"));
			return new ParasitePhaseCondition(operation, value);
		} catch(Exception e) {
			HordesLogger.logError("Incorrect parameters for condition srpmixins:phase", e);
		}
		return null;
	}

}
