package srpmixins.world;

import com.dhanantry.scapeandrunparasites.init.SRPBiomes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class SRPWorldProvider extends WorldProvider {

	public static final int DIMENSION_ID = 254;

	public SRPWorldProvider() {
		hasSkyLight = false;
		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) { 
			setSkyRenderer(new IRenderHandler() { @Override @SideOnly(Side.CLIENT) public void render(float partialTicks, WorldClient world, Minecraft mc) {}});
		}
	}

	@Override @Nonnull
	public IChunkGenerator createChunkGenerator() {
		return new SRPChunkGenerator(this.world, this.world.getSeed());
	}

	@Override @Nonnull @SideOnly(Side.CLIENT)
	public Vec3d getSkyColor(@Nonnull Entity cameraEntity, float partialTicks) {
		return new Vec3d(0, 0, 0);
	}

	@Override @Nonnull @SideOnly(Side.CLIENT)
	public Vec3d getFogColor(float par1, float par2) {
		return new Vec3d(0, 0, 0);
	}

	@Override @SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1) {
		return 1F;
	}

	@Override @Nonnull
	public Biome getBiomeForCoords(@Nonnull BlockPos pos) {
		return SRPBiomes.biomeInfested;
	}

	@Override @SideOnly(Side.CLIENT)
	public float getCloudHeight() {
		return 600000F;
	}

	@SideOnly(Side.CLIENT)
	public double getVoidFogYFactor() {
		return 0.0;
	}

	@Override
	public boolean canDoLightning(@Nonnull Chunk chunk) {
		return true;
	}

	@Override
	public boolean canDoRainSnowIce(@Nonnull Chunk chunk) {
		return false;
	}

	@Override
	public boolean canSnowAt(@Nonnull BlockPos pos, boolean checkLight) {
		return false;
	}

	@Override
	public boolean shouldMapSpin(@Nonnull String entity, double x, double y, double z) {
		return true;
	}

	@Override @SideOnly(Side.CLIENT)
	public float[] calcSunriseSunsetColors(float par1, float par2) {
		return new float[] { 0.0F, 0.0F, 0.0F, 0.0F };
	}

	@Override @Nonnull
	public DimensionType getDimensionType() {
		return DimensionType.getById(DIMENSION_ID);
	}

	@Override
	public boolean canRespawnHere() {
		return false;
	}

	@Override @Nonnull
	public WorldSleepResult canSleepAt(@Nonnull EntityPlayer player, @Nonnull BlockPos pos){
		return WorldSleepResult.BED_EXPLODES;
	}

	@Override
	public boolean hasSkyLight()
	{
		return false;
	}

	public float calculateCelestialAngle(long par1, float par3) {
		return 0.75F;
	}
}
