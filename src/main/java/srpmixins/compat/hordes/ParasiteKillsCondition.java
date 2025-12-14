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

public class ParasiteKillsCondition implements Condition {

	protected final ValueGetter<Integer> actualKills;
	protected final ComparableOperation operation;
	protected final ValueGetter<Integer> comparisonKills;

	private ParasiteKillsCondition(ComparableOperation operation, ValueGetter<Integer> value) {
		this.actualKills = new ParasiteKillGetter();
		this.operation = operation;
		this.comparisonKills = value;
	}

	public boolean apply(World level, EntityLivingBase entity, EntityPlayerMP player, Random rand) {
		return this.operation.apply(this.actualKills.get(level, entity, player, rand), this.comparisonKills.get(level, entity, player, rand));
	}

	public static ParasiteKillsCondition deserialize(JsonElement json) {
		try {
			JsonObject obj = json.getAsJsonObject();
			ComparableOperation operation = ComparableOperation.of(obj.get("operation").getAsString());
			ValueGetter<Integer> value = ValueGetter.readValue(DataType.INT,  obj.get("value"));
			return new ParasiteKillsCondition(operation, value);
		} catch(Exception e) {
			HordesLogger.logError("Incorrect parameters for condition srpmixins:kills", e);
		}
		return null;
	}

}
