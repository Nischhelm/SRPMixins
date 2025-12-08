package srpmixins.world;

import com.dhanantry.scapeandrunparasites.init.SRPBiomes;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import com.dhanantry.scapeandrunparasites.init.SRPFluids;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventWorld;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.terraingen.*;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SRPChunkGenerator implements IChunkGenerator {
    protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
    protected static final IBlockState PARABLOCK = SRPBlocks.ParasiteStain.getDefaultState();//Blocks.STONE.getDefaultState();
    protected static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
    protected static final IBlockState DEADBLOOD = SRPFluids.DEADBLOOD_FLUID.getBlock().getDefaultState();
    protected static final IBlockState PARABLOCK2 = SRPBlocks.ParasiteRubble.getDefaultState();

    private final World world;
    private final Random rand;

    double[] pnr;
    double[] ar;
    double[] br;
    double[] dr;
    public NoiseGeneratorOctaves scaleNoise;
    public NoiseGeneratorOctaves depthNoise;
    private NoiseGeneratorOctaves lperlinNoise1;
    private NoiseGeneratorOctaves lperlinNoise2;
    private NoiseGeneratorOctaves perlinNoise1;

    private double[] buffer;

    private double[] slowsandNoise = new double[256];
    private double[] depthBuffer = new double[256];
    private NoiseGeneratorOctaves slowsandGravelNoiseGen;
    private NoiseGeneratorOctaves parablockExculsivityNoiseGen;
    private MapGenBase genCaves = new MapGenSourceCaves();

    public SRPChunkGenerator(World worldIn, long seed) {
        this.world = worldIn;
        this.rand = new Random(seed);
        this.lperlinNoise1 = new NoiseGeneratorOctaves(this.rand, 16);
        this.lperlinNoise2 = new NoiseGeneratorOctaves(this.rand, 16);
        this.perlinNoise1 = new NoiseGeneratorOctaves(this.rand, 8);
        this.slowsandGravelNoiseGen = new NoiseGeneratorOctaves(this.rand, 4);
        this.parablockExculsivityNoiseGen = new NoiseGeneratorOctaves(this.rand, 4);
        this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
        this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
        worldIn.setSeaLevel(63);

        InitNoiseGensEvent.ContextHell ctx = new InitNoiseGensEvent.ContextHell(lperlinNoise1, lperlinNoise2, perlinNoise1, slowsandGravelNoiseGen, parablockExculsivityNoiseGen, scaleNoise, depthNoise);
        ctx = TerrainGen.getModdedNoiseGenerators(worldIn, this.rand, ctx);
        this.lperlinNoise1 = ctx.getLPerlin1();
        this.lperlinNoise2 = ctx.getLPerlin2();
        this.perlinNoise1 = ctx.getPerlin();
        this.slowsandGravelNoiseGen = ctx.getPerlin2();
        this.parablockExculsivityNoiseGen = ctx.getPerlin3();
        this.scaleNoise = ctx.getScale();
        this.depthNoise = ctx.getDepth();
        this.genCaves = TerrainGen.getModdedMapGen(genCaves, InitMapGenEvent.EventType.CAVE);
    }

    @Override @Nonnull
    public Chunk generateChunk(int x, int z) {
        this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.prepareHeights(x, z, chunkprimer);
        this.buildSurfaces(x, z, chunkprimer);
        this.genCaves.generate(this.world, x, z, chunkprimer);

        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);

        Arrays.fill(chunk.getBiomeArray(), (byte) Biome.getIdForBiome(SRPBiomes.biomeInfested));

        chunk.resetRelightChecks();
        return chunk;
    }

    public void prepareHeights(int cx, int cz, ChunkPrimer primer) {
        int deadBloodLevel = this.world.getSeaLevel() / 2 + 1;
        int always16 = 16 * 2; //for nether chunkgen this is 16, but we increase it to 32 to generate caves up to sky limit
        int always17 = always16 + 1;
        this.buffer = this.getHeights(this.buffer, cx * 4, 0, cz * 4, 5, always17, 5);

        for (int bigx = 0; bigx < 4; ++bigx) for (int bigz = 0; bigz < 4; ++bigz) for (int bigy = 0; bigy < always16; ++bigy) {
            double x0y0z0 = this.buffer[(bigx * 5 + bigz) * always17 + bigy];
            double x0y0z1 = this.buffer[(bigx * 5 + bigz + 1) * always17 + bigy];
            double x1y0z0 = this.buffer[((bigx + 1) * 5 + bigz) * always17 + bigy];
            double x1y0z1 = this.buffer[((bigx + 1) * 5 + bigz + 1) * always17 + bigy];
            double dVdy_at_x0z0 = (this.buffer[((bigx) * 5 + bigz) * always17 + bigy + 1] - x0y0z0) * 0.125D;
            double dVdy_at_x0z1 = (this.buffer[((bigx) * 5 + bigz + 1) * always17 + bigy + 1] - x0y0z1) * 0.125D;
            double dVdy_at_x1z0 = (this.buffer[((bigx + 1) * 5 + bigz) * always17 + bigy + 1] - x1y0z0) * 0.125D;
            double dVdy_at_x1z1 = (this.buffer[((bigx + 1) * 5 + bigz + 1) * always17 + bigy + 1] - x1y0z1) * 0.125D;

            for (int smally = 0; smally < 8; ++smally) {
                double d10 = x0y0z0;
                double d11 = x0y0z1;
                double dVdx_at_z0 = (x1y0z0 - x0y0z0) * 0.25D;
                double dVdx_at_z1 = (x1y0z1 - x0y0z1) * 0.25D;

                for (int smallx = 0; smallx < 4; ++smallx) {
                    double d15 = d10;
                    double dVdz_at_x0 = (d11 - d10) * 0.25D;

                    for (int smallz = 0; smallz < 4; ++smallz) {
                        IBlockState iblockstate = null;

                        if (bigy * 8 + smally < deadBloodLevel) iblockstate = DEADBLOOD;
                        if (d15 > 0.0D) iblockstate = PARABLOCK;

                        int x = smallx + bigx * 4;
                        int y = smally + bigy * 8;
                        int z = smallz + bigz * 4;
                        primer.setBlockState(x, y, z, iblockstate);
                        d15 += dVdz_at_x0;
                    }

                    d10 += dVdx_at_z0;
                    d11 += dVdx_at_z1;
                }

                x0y0z0 += dVdy_at_x0z0;
                x0y0z1 += dVdy_at_x0z1;
                x1y0z0 += dVdy_at_x1z0;
                x1y0z1 += dVdy_at_x1z1;
            }
        }
    }

    private double[] getHeights(double[] bufferIn, int cx_4, int cy_always0, int cz_4, int sizex_always5, int sizey_always17, int sizez_always5) {
        if (bufferIn == null) bufferIn = new double[sizex_always5 * sizey_always17 * sizez_always5];

        ChunkGeneratorEvent.InitNoiseField event = new ChunkGeneratorEvent.InitNoiseField(this, bufferIn, cx_4, cy_always0, cz_4, sizex_always5, sizey_always17, sizez_always5);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Event.Result.DENY)
            return event.getNoisefield();

        this.dr = this.depthNoise.generateNoiseOctaves(this.dr, cx_4, cy_always0, cz_4, sizex_always5, 1, sizez_always5, 100.0D, 0.0D, 100.0D);
        this.pnr = this.perlinNoise1.generateNoiseOctaves(this.pnr, cx_4, cy_always0, cz_4, sizex_always5, sizey_always17, sizez_always5, 8.555150000000001D, 34.2206D, 8.555150000000001D);
        this.ar = this.lperlinNoise1.generateNoiseOctaves(this.ar, cx_4, cy_always0, cz_4, sizex_always5, sizey_always17, sizez_always5, 684.412D, 2053.236D, 684.412D);
        this.br = this.lperlinNoise2.generateNoiseOctaves(this.br, cx_4, cy_always0, cz_4, sizex_always5, sizey_always17, sizez_always5, 684.412D, 2053.236D, 684.412D);
        int unrolled_idx = 0;
        double[] seventeen_pre_values = new double[sizey_always17];

        for (int bigy = 0; bigy < sizey_always17; ++bigy) {
            seventeen_pre_values[bigy] = Math.cos((double) bigy * Math.PI * 6.0D / (double) sizey_always17) * 2.0D;
            double d2 = bigy;

            if (bigy > sizey_always17 / 2) {
                d2 = sizey_always17 - 1 - bigy;
            }

            if (d2 < 4.0D) {
                d2 = 4.0D - d2;
                seventeen_pre_values[bigy] -= d2 * d2 * d2 * 10.0D;
            }
        }

        for (int bigx = 0; bigx < sizex_always5; ++bigx) for (int bigz = 0; bigz < sizez_always5; ++bigz) {

            for (int bigy = 0; bigy < sizey_always17; ++bigy) {
                double some_pre_value = seventeen_pre_values[bigy];
                double ar_val = this.ar[unrolled_idx] / 512.0D;
                double br_val = this.br[unrolled_idx] / 512.0D;
                double pnr_val = (this.pnr[unrolled_idx] / 10.0D + 1.0D) / 2.0D;
                double calced_val;

                //Step function between ar_val and br_val stepped linearly using pnr_val (linear part at pnr_val between 0 and 1)
                if (pnr_val < 0.0D) {
                    calced_val = ar_val;
                } else if (pnr_val > 1.0D) {
                    calced_val = br_val;
                } else {
                    calced_val = ar_val + (br_val - ar_val) * pnr_val;
                }

                calced_val -= some_pre_value;

                //Top 4 subchunks get special treatment moving them linearly to be calced_val = -10
                if (bigy > sizey_always17 - 4) {
                    double d9 = ((float) (bigy - (sizey_always17 - 4)) / 3F); //is 0 at bigy = 13, 1 at bigy = 16
                    calced_val = calced_val * (1 - d9) - 10 * d9; //is calced_val at bigy=13, -10 at bigy = 16
                }

                bufferIn[unrolled_idx] = calced_val;
                ++unrolled_idx;
            }
        }

        return bufferIn;
    }

    public void buildSurfaces(int cx, int cz, ChunkPrimer primer) {
        if (!ForgeEventFactory.onReplaceBiomeBlocks(this, cx, cz, primer, this.world)) return;
        int yAboveSeaLevel = this.world.getSeaLevel() + 1;
        this.slowsandNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.slowsandNoise, cx * 16, cz * 16, 0, 16, 16, 1, 0.03125D, 0.03125D, 1.0D);
        this.depthBuffer = this.parablockExculsivityNoiseGen.generateNoiseOctaves(this.depthBuffer, cx * 16, cz * 16, 0, 16, 16, 1, 0.0625D, 0.0625D, 0.0625D);

        for (int z = 0; z < 16; ++z) for (int x = 0; x < 16; ++x) {
            boolean shouldPlaceOtherBlock = this.slowsandNoise[z + x * 16] + this.rand.nextDouble() * 0.2D > 0.0D;
            int l = (int) (this.depthBuffer[z + x * 16] / 3.0D + 3.0D + this.rand.nextDouble() * 0.25D);
            int i1 = -1;
            IBlockState iblockstate = PARABLOCK;
            IBlockState iblockstate1 = PARABLOCK;

            int yMax = 2*128 -1; //127 for nether
            for (int y = yMax; y >= 0; --y) {
                if (y >= yMax - this.rand.nextInt(5) || y <= this.rand.nextInt(5)) {
                    //Bedrock above and below
                    primer.setBlockState(x, y, z, BEDROCK);
                } else {
                    IBlockState primerState = primer.getBlockState(x, y, z);
                    primer.setBlockState(x,y,z, primerState);

                    if (primerState.getMaterial() != Material.AIR) {
                        if (primerState.getBlock() == SRPBlocks.ParasiteStain) {
                            if (i1 == -1) {
                                if (l <= 0) {
                                    iblockstate = AIR;
                                    iblockstate1 = PARABLOCK;
                                } else if (y >= yAboveSeaLevel - 4 && y <= yAboveSeaLevel + 1) {
                                    iblockstate = PARABLOCK;
                                    iblockstate1 = PARABLOCK;

                                    if (shouldPlaceOtherBlock) {
                                        iblockstate = PARABLOCK2;
                                        iblockstate1 = PARABLOCK2;
                                    }
                                }

                                if (y < yAboveSeaLevel && iblockstate.getMaterial() == Material.AIR) {
                                    iblockstate = DEADBLOOD;
                                }

                                i1 = l;

                                if (y >= yAboveSeaLevel - 1) {
                                    primer.setBlockState(x, y, z, iblockstate);
                                } else {
                                    primer.setBlockState(x, y, z, iblockstate1);
                                }
                            } else if (i1 > 0) {
                                --i1;
                                primer.setBlockState(x, y, z, iblockstate1);
                            }
                        }
                    } else {
                        i1 = -1;
                    }
                }
            }
        }
    }

	@Override
    public void populate(int cx, int cz) {
        BlockFalling.fallInstantly = true;
        ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, cx, cz, false);

        //lilys 0 trees 1 extratrees 0.0 flowers 4 grass 15 deadbush 2 mushroom 0 reeds 0 cacti 15 gravel 1 sand 3 clay 1
        BlockPos startPos = new BlockPos(cx * 16, 0, cz * 16);
        SRPBiomes.biomeInfested.decorate(this.world, this.rand, startPos);
        ParasiteEventWorld.spawnGenFeatureParasite(this.world, startPos.add(8, 60, 8), this.rand);
        ParasiteEventWorld.spawnGenRoofParasite(this.world, startPos.add(8, 60, 8), this.rand);

        ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, cx, cz, false);

        BlockFalling.fallInstantly = false;
    }

    @Override public boolean generateStructures(Chunk chunkIn, int x, int z) {return false;}
	@Override @Nonnull public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {return SRPBiomes.biomeInfested.getSpawnableList(creatureType);}
	@Override @Nullable public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {return null;}
	@Override public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {return false;}
    @Override public void recreateStructures(Chunk chunkIn, int x, int z) {}

    private static class MapGenSourceCaves extends MapGenCaves {
        protected static final IBlockState BLK_DEADBLOOD = SRPFluids.DEADBLOOD_FLUID.getBlock().getDefaultState();

        @Override
        //This only overrides lava with deadblood in caves below sea level
        protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop, IBlockState state, IBlockState up) {
            Biome biome = SRPBiomes.biomeInfested;
            IBlockState top = biome.topBlock;
            IBlockState filler = biome.fillerBlock;

            if (this.canReplaceBlock(state, up) || state.getBlock() == top.getBlock() || state.getBlock() == filler.getBlock()) {
                if (y - 1 < 10)
                    data.setBlockState(x, y, z, BLK_DEADBLOOD);
                else {
                    data.setBlockState(x, y, z, BLK_AIR);
                    if (foundTop && data.getBlockState(x, y - 1, z).getBlock() == filler.getBlock())
                        data.setBlockState(x, y - 1, z, top.getBlock().getDefaultState());
                }
            }
        }
    }
}
