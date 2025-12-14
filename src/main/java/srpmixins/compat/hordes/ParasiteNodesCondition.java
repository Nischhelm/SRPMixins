package srpmixins.compat.hordes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.data.ComparableOperation;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.common.HordesLogger;
import net.smileycorp.hordes.config.data.conditions.Condition;
import net.smileycorp.hordes.config.data.values.ValueGetter;

import java.util.Random;

public class ParasiteNodesCondition implements Condition {

	protected final ValueGetter<Integer> actualNodes;
	protected final ComparableOperation operation;
	protected final ValueGetter<Integer> comparisonNodes;

	private ParasiteNodesCondition(ComparableOperation operation, ValueGetter<Integer> value) {
		this.actualNodes = new ParasiteNodesGetter();
		this.operation = operation;
		this.comparisonNodes = value;
	}

	public boolean apply(World level, EntityLivingBase entity, EntityPlayerMP player, Random rand) {
		return this.operation.apply(this.actualNodes.get(level, entity, player, rand), this.comparisonNodes.get(level, entity, player, rand));
	}

	public static ParasiteNodesCondition deserialize(JsonElement json) {
		try {
			JsonObject obj = json.getAsJsonObject();
			ComparableOperation operation = ComparableOperation.of(obj.get("operation").getAsString());
			ValueGetter<Integer> value = ValueGetter.readValue(DataType.INT,  obj.get("value"));
			return new ParasiteNodesCondition(operation, value);
		} catch(Exception e) {
			HordesLogger.logError("Incorrect parameters for condition srpmixins:nodes", e);
		}
		return null;
	}

}
