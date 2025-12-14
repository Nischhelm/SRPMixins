package srpmixins.compat.hordes;

import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import com.google.gson.JsonObject;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.data.DataType;
import net.smileycorp.hordes.common.HordesLogger;
import net.smileycorp.hordes.config.data.values.ValueGetter;

import java.util.Random;

public class ParasiteNodesGetter implements ValueGetter<Integer> {

	@Override
	public Integer get(World level, EntityLivingBase entity, EntityPlayerMP player, Random rand) {
		return SRPWorldData.get(level).getNodes("x").size();
	}
	
	public static <T extends Comparable<T>> ValueGetter deserialize(JsonObject object, DataType<T> type) {
		try {
			return new ParasiteNodesGetter();
		} catch (Exception e) {
			HordesLogger.logError("invalid value for srparasites:nodes", e);
		}
		return null;
	}
	
}
