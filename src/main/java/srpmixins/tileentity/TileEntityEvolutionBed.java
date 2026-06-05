package srpmixins.tileentity;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.registry.ModBlocks;
import srpmixins.registry.ModItems;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

public class TileEntityEvolutionBed extends TileEntityBed {

    private int phase = 0;

    @SubscribeEvent
    public static void onPlayerSleepInBedEvent(PlayerSleepInBedEvent event) {
        // Allow (instant) use in dimensions where you can't sleep
        if(event.getResultStatus() == null) {
            BlockPos pos = event.getPos();
            EntityPlayer player = event.getEntityPlayer();
            World world = player.world;
            IBlockState state = world.getBlockState(pos);
            if(state.getBlock() == ModBlocks.evolutionBed) {
                // Check TE when confirming Phase change
                if(!world.provider.isSurfaceWorld()) {
                    TileEntity te = world.getTileEntity(pos);
                    if (te instanceof TileEntityEvolutionBed) {
                        event.setResult(EntityPlayer.SleepResult.OTHER_PROBLEM);
                        if(!world.isRemote)
                            changePhase(state, world, player, pos, ((TileEntityEvolutionBed) te).getDestinationPhase());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSleepingTimeCheckEvent(SleepingTimeCheckEvent event) {
        // Allow any time of day
        BlockPos pos = event.getSleepingLocation();
        EntityPlayer player = event.getEntityPlayer();
        World world = player.world;
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == ModBlocks.evolutionBed) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public static void onSleepingLocationCheckEvent(SleepingLocationCheckEvent event) {
        BlockPos pos = event.getSleepingLocation();
        EntityPlayer player = event.getEntityPlayer();
        World world = player.world;
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() == ModBlocks.evolutionBed) {
            // Check TE when confirming Phase change
            if(player.isPlayerFullyAsleep() && !world.isRemote) {
                TileEntity te = world.getTileEntity(pos);
                if(te instanceof TileEntityEvolutionBed) {
                    // Kick out before vanilla sleep
                    changePhase(state, world, player, pos, ((TileEntityEvolutionBed) te).getDestinationPhase());
                    event.setResult(Event.Result.DENY);
                }
            }
            else {
                // Allow in bed
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerWakeUpEvent(PlayerWakeUpEvent event) {
        // Clear the occupied property
        EntityPlayer player = event.getEntityPlayer();
        IBlockState blockState = player.bedLocation == null ? null : player.world.getBlockState(player.bedLocation);

        if (blockState != null && blockState.getBlock() == ModBlocks.evolutionBed) {
            blockState.getBlock().setBedOccupied(player.world, player.bedLocation, player, false);
        }
    }

    public static void changePhase(IBlockState blockState, World world, EntityPlayer player, BlockPos pos, int destinationPhase) {
        SRPSaveData srpData = SRPSaveDataInterface.get(world, player, pos);
        srpData.setEvolutionPhase(
                world.provider.getDimension(),
                (byte) destinationPhase,
                true,
                world,
                true
        );
        srpData.setCooldown(
                0,
                world,
                world.provider.getDimension()
        );

        world.addWeatherEffect(new EntityLightningBolt(
                world,
                pos.getX() + 0.5D,
                pos.getY() + 0.5D,
                pos.getZ() + 0.5D,
                true)
        );
        if (!player.capabilities.isCreativeMode) {
            world.setBlockToAir(pos);

            BlockPos blockpos = pos.offset(blockState.getValue(BlockBed.FACING).getOpposite());
            if (world.getBlockState(blockpos).getBlock() == ModBlocks.evolutionBed) {
                world.setBlockToAir(blockpos);
            }
        }
    }

    public static int getClampedPhase(int phase) {
        int max = SRPMixinsConfigHandler.morephases.enableMorePhases ? SRPMixinsConfigHandler.morephases.maxEvolutionPhase : 10;

        return MathHelper.clamp(phase, -1, max);
    }

    public static int getMetaFromPhase(int phase) {
        return phase - 1;
    }

    @Override
    public void setItemValues(ItemStack itemStack) {
        this.setPhase(itemStack.getMetadata()+1);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("phase")) {
            this.phase = compound.getInteger("phase");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("phase", this.phase);
        return compound;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    public int getRespectivePhase() {
        return this.phase;
    }

    public int getDestinationPhase() {
        return getClampedPhase(this.phase + SRPMixinsConfigHandler.lures.evolutionBedPhaseIncrease);
    }

    public void setPhase(int phase) {
        this.phase = phase;
        this.markDirty();
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(ModItems.evolutionBed, 1, getMetaFromPhase(this.phase));
    }
}
