package srpmixins.mixin.parabiome;

import com.dhanantry.scapeandrunparasites.block.BlockBiomePurifier;
import com.dhanantry.scapeandrunparasites.block.BlockInfestedRubble;
import com.dhanantry.scapeandrunparasites.block.BlockInfestedStain;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPBlocks;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.SRPMixinsConfigHandler;

import javax.annotation.Nonnull;
import java.util.*;

@Mixin(BlockBiomePurifier.class)
public abstract class BiomePurifierTweak extends Block {
    public BiomePurifierTweak(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    @Inject(
            method = "updateTick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void srpmixins_dontTick(World worldIn, BlockPos pos, IBlockState state, Random rand, CallbackInfo ci){
        ci.cancel();
    }

    @Inject(
            method = "onBlockActivated",
            at = @At("TAIL")
    )
    private void srpmixins_effectsOnActivate(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir){
        if (worldIn.isRemote) return;
        if (!worldIn.isAreaLoaded(pos, 3)) return;

        WorldServer world = (WorldServer) worldIn;

        Set<EntityParasiteBase> parasites = new HashSet<>();
        Set<EntityPStationaryArchitect> nexus = new HashSet<>();

        ChunkPos cPos = new ChunkPos(pos);
        int chunkRange = SRPMixinsConfigHandler.parabiome.biomePurifPotionRange;

        for (int dx = -chunkRange; dx <= chunkRange; dx++)
            for (int dz = -chunkRange; dz <= chunkRange; dz++){
                int cx = cPos.x + dx;
                int cz = cPos.z + dz;
                if(!world.getChunkProvider().chunkExists(cx, cz)) continue;
                Chunk chunk = world.getChunk(cx, cz);
                for (ClassInheritanceMultiMap<Entity> entityList : chunk.getEntityLists()) //All subchunks
                    for (EntityParasiteBase para : entityList.getByClass(EntityParasiteBase.class)) {
                        parasites.add(para);
                        if (para instanceof EntityPStationaryArchitect) nexus.add((EntityPStationaryArchitect) para);
                    }
            }

        for (EntityParasiteBase mob : parasites)
            mob.addPotionEffect(new PotionEffect(SRPPotions.RAGE_E, SRPMixinsConfigHandler.parabiome.biomePurifRageDuration, 1, false, false));

        if (SRPMixinsConfigHandler.parabiome.biomePurifApplyGlowingToAll)
            for (EntityPStationaryArchitect mob : nexus)
                mob.addPotionEffect(new PotionEffect(MobEffects.GLOWING, SRPMixinsConfigHandler.parabiome.biomePurifGlowingDuration, 0, false, false));
        else
            nexus.stream().findFirst().ifPresent(nex -> nex.addPotionEffect(new PotionEffect(MobEffects.GLOWING, SRPMixinsConfigHandler.parabiome.biomePurifGlowingDuration, 0, false, false)));
    }

    @Override
    public void onBlockPlacedBy(@Nonnull World worldIn, BlockPos startPos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, @Nonnull ItemStack stack) {
        int purifyRange = SRPMixinsConfigHandler.parabiome.biomePurifReversionRange;

        for (BlockPos.MutableBlockPos pos : BlockPos.getAllInBoxMutable(startPos.add(-purifyRange, -purifyRange*2, -purifyRange), startPos.add(purifyRange, purifyRange*2, purifyRange))) {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (block == SRPBlocks.InfestedStain) {
                worldIn.setBlockState(pos, SRPBlocks.InfestedStain.getDefaultState().withProperty(BlockInfestedStain.STAGE, 5));
                worldIn.updateBlockTick(pos, SRPBlocks.InfestedStain, 40, 5);
            } else if (block == SRPBlocks.InfestedRubble) {
                worldIn.setBlockState(pos, SRPBlocks.InfestedRubble.getDefaultState().withProperty(BlockInfestedRubble.STAGE, 5));
                worldIn.updateBlockTick(pos, SRPBlocks.InfestedRubble, 40, 5);
            }
        }
    }
}

