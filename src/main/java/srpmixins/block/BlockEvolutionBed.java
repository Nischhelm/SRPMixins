package srpmixins.block;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.block.BlockBed;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.registry.ModItems;
import srpmixins.tileentity.TileEntityEvolutionBed;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockEvolutionBed extends BlockBed {

    public BlockEvolutionBed() {
        super();
        this.setSoundType(SoundType.WOOD);
        this.setHardness(0.2F);
        this.disableStats();
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        else {
            if (state.getValue(PART) != BlockBed.EnumPartType.HEAD) {
                pos = pos.offset(state.getValue(FACING));
                state = worldIn.getBlockState(pos);

                if (state.getBlock() != this) {
                    return true;
                }
            }

            TileEntity te = worldIn.getTileEntity(pos);
            if(!(te instanceof TileEntityEvolutionBed)) return true;

            SRPSaveData srpData = SRPSaveDataInterface.get(worldIn, playerIn, pos);
            int currentPhase = srpData.getEvolutionPhase(playerIn.dimension);
            int destinationPhase = ((TileEntityEvolutionBed) te).getDestinationPhase();

            if (playerIn.capabilities.isCreativeMode || (currentPhase >= -1 && currentPhase < destinationPhase)) {
                if(!playerIn.capabilities.isCreativeMode && SRPMixinsConfigHandler.lures.blockBedUntilRespPhase) {
                    if(((TileEntityEvolutionBed) te).getRespectivePhase() != currentPhase) {
                        //Fear incapacitates
                        playerIn.sendStatusMessage(new TextComponentTranslation("srpmixins.msg.fearincapacitates").setStyle(new Style().setColor(TextFormatting.RED)), true);

                        //Fear effect
                        Potion fearEffect = Potion.getPotionFromResourceLocation("lycanitesmobs:fear");
                        if (fearEffect != null) playerIn.addPotionEffect(new PotionEffect(fearEffect, 100, 0));
                        return true;
                    }
                }
                if (state.getValue(OCCUPIED)) {
                    EntityPlayer entityplayer = this.getPlayerInEvolutionBed(worldIn, pos);

                    if (entityplayer != null) {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.occupied"), true);
                        return true;
                    }

                    state = state.withProperty(OCCUPIED, false);
                    worldIn.setBlockState(pos, state, 4);
                }

                EntityPlayer.SleepResult sleepResult = playerIn.trySleep(pos);
                switch (sleepResult) {
                    case OK:
                        state = state.withProperty(OCCUPIED, true);
                        worldIn.setBlockState(pos, state, 4);
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.srpmixins.evolution_bed.increasePhase", destinationPhase), false);
                        break;
                    case NOT_POSSIBLE_NOW:
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep"), true);
                        break;
                    case NOT_SAFE:
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe"), true);
                        break;
                    case TOO_FAR_AWAY:
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.tooFarAway"), true);
                        break;
                }
                return true;
            }
            else {
                playerIn.sendStatusMessage(new TextComponentTranslation("tile.srpmixins.evolution_bed.decreasePhase"), true);
                return true;
            }
        }
    }

    @Nullable
    public EntityPlayer getPlayerInEvolutionBed(World worldIn, BlockPos pos) {
        for (EntityPlayer entityplayer : worldIn.playerEntities) {
            if (entityplayer.isPlayerSleeping() && entityplayer.bedLocation.equals(pos)) {
                return entityplayer;
            }
        }
        return null;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        Item bed = super.getItemDropped(state, rand, fortune);
        return bed == Items.AIR ? bed : ModItems.evolutionBed;
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (state.getValue(PART) == BlockBed.EnumPartType.HEAD) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            int phase = tileentity instanceof TileEntityEvolutionBed
                    ? TileEntityEvolutionBed.getMetaFromPhase(((TileEntityEvolutionBed) tileentity).getRespectivePhase())
                    : 0;
            spawnAsEntity(worldIn, pos, new ItemStack(ModItems.evolutionBed, 1, phase));
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        BlockPos blockpos = pos;

        if (state.getValue(PART) == BlockBed.EnumPartType.FOOT) {
            blockpos = pos.offset(state.getValue(FACING));
        }

        TileEntity tileentity = worldIn.getTileEntity(blockpos);
        int phase = tileentity instanceof TileEntityEvolutionBed
                ? TileEntityEvolutionBed.getMetaFromPhase(((TileEntityEvolutionBed) tileentity).getRespectivePhase())
                : 0;
        return new ItemStack(ModItems.evolutionBed, 1, phase);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEvolutionBed();
    }
}
