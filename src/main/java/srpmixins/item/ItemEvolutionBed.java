package srpmixins.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.registry.ModBlocks;
import srpmixins.tileentity.TileEntityEvolutionBed;

import java.util.List;

public class ItemEvolutionBed extends ItemBed {

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        else if (facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        }
        else {
            IBlockState footBlockstate = worldIn.getBlockState(pos);
            Block footBlock = footBlockstate.getBlock();
            boolean footReplaceable = footBlock.isReplaceable(worldIn, pos);

            if (!footReplaceable) {
                pos = pos.up();
            }

            int facingIndex = MathHelper.floor((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            EnumFacing enumfacing = EnumFacing.byHorizontalIndex(facingIndex);
            BlockPos blockpos = pos.offset(enumfacing);
            ItemStack itemstack = player.getHeldItem(hand);

            if (player.canPlayerEdit(pos, facing, itemstack) && player.canPlayerEdit(blockpos, facing, itemstack)) {
                IBlockState headBlockstate = worldIn.getBlockState(blockpos);
                boolean headReplaceable = headBlockstate.getBlock().isReplaceable(worldIn, blockpos);
                boolean foot = footReplaceable || worldIn.isAirBlock(pos);
                boolean head = headReplaceable || worldIn.isAirBlock(blockpos);

                if (foot && head && worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP) && worldIn.getBlockState(blockpos.down()).isSideSolid(worldIn, pos, EnumFacing.UP)) {
                    IBlockState evoBedBlock = ModBlocks.evolutionBed.getDefaultState().withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false)).withProperty(BlockBed.FACING, enumfacing).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
                    worldIn.setBlockState(pos, evoBedBlock, 10);
                    worldIn.setBlockState(blockpos, evoBedBlock.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD), 10);
                    SoundType soundtype = evoBedBlock.getBlock().getSoundType(evoBedBlock, worldIn, pos, player);
                    worldIn.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    TileEntity teHead = worldIn.getTileEntity(blockpos);

                    if (teHead instanceof TileEntityBed) {
                        ((TileEntityBed)teHead).setItemValues(itemstack);
                    }

                    TileEntity teFoot = worldIn.getTileEntity(pos);

                    if (teFoot instanceof TileEntityBed) {
                        ((TileEntityBed)teFoot).setItemValues(itemstack);
                    }

                    worldIn.notifyNeighborsRespectDebug(pos, footBlock, false);
                    worldIn.notifyNeighborsRespectDebug(blockpos, headBlockstate.getBlock(), false);

                    if (player instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack);
                    }

                    itemstack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
                else {
                    return EnumActionResult.FAIL;
                }
            }
            else {
                return EnumActionResult.FAIL;
            }
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.getTranslationKey() + "." + stack.getMetadata();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltip.tile.srpmixins.evolution_bed",TileEntityEvolutionBed.getClampedPhase(stack.getMetadata() + 1 + SRPMixinsConfigHandler.lures.evolutionBedPhaseIncrease)));
    }

    // Fill creative with 10 total [0-9]
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            int phases = 10;

            if(SRPMixinsConfigHandler.morephases.enableMorePhases) {
                phases = SRPMixinsConfigHandler.morephases.maxEvolutionPhase;
            }

            for (int i = 0; i < phases; ++i) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }
}
