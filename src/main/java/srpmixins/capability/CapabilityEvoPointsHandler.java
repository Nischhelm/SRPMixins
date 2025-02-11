package srpmixins.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityEvoPointsHandler {
    public static final ResourceLocation CAP_EVOPOINTS_KEY = new ResourceLocation(SRPMixins.MODID, "evopoints");

    @CapabilityInject(ICapabilityEvoPoints.class)
    public static Capability<ICapabilityEvoPoints> CAP_EVOPOINTS;

    public static void registerCapability() {
        CapabilityManager.INSTANCE.register(ICapabilityEvoPoints.class, new Storage(), CapabilityEvoPoints::new);
    }

    @Mod.EventBusSubscriber
    public static class AttachCapabilityHandler {
        @SubscribeEvent
        public static void onAttachChunkCapabilities(AttachCapabilitiesEvent<Chunk> event) {
            Chunk chunk = event.getObject();
            World world = chunk.getWorld();
            //IntelliJ thinks world can't be null but there's always a chunk with chunkCoords 0,0 that has a null world, idk why
            if (world == null || world.isRemote) return;
            if (chunk.hasCapability(CAP_EVOPOINTS, null)) return;

            //Set starting phase for biome at NW corner block of chunk
            byte startPhase = -9;
            if(!SRPMixinsConfigProvider.biomeStartPhases.isEmpty()) {
                ResourceLocation biomeId = chunk.getBiome(chunk.getPos().getBlock(0, 0, 0), chunk.getWorld().provider.getBiomeProvider()).getRegistryName();
                if (biomeId != null) startPhase = SRPMixinsConfigProvider.biomeStartPhases.getOrDefault(biomeId.toString(), (byte) -9);
            }

            event.addCapability(CAP_EVOPOINTS_KEY, new CapabilityEvoPointsHandler.Provider(chunk, startPhase));
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        private final ICapabilityEvoPoints instance;

        public Provider(Chunk chunk, byte startPhase) {
            this.instance = new CapabilityEvoPoints(chunk, startPhase);
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CAP_EVOPOINTS;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == CAP_EVOPOINTS ? CAP_EVOPOINTS.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) CAP_EVOPOINTS.writeNBT(instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            CAP_EVOPOINTS.readNBT(instance, null, nbt);
        }
    }

    public static class Storage implements Capability.IStorage<ICapabilityEvoPoints> {
        @Override
        public NBTBase writeNBT(Capability<ICapabilityEvoPoints> capability, ICapabilityEvoPoints instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("evoPoints", instance.getEvoPoints());
            nbt.setInteger("evoPointsBuffer", instance.getEvoPointsBuffer());
            nbt.setByte("evoPhase", instance.getEvoPhase());
            nbt.setLong("cooldownUntil", instance.getCooldownUntil());
            return nbt;
        }

        @Override
        public void readNBT(Capability<ICapabilityEvoPoints> capability, ICapabilityEvoPoints instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tags = (NBTTagCompound) nbt;
            instance.setEvoPointsFromNBT(tags.getInteger("evoPoints"));
            instance.setEvoPointsBufferFromNBT(tags.getInteger("evoPointsBuffer"));
            instance.setEvoPhaseFromNBT(tags.getByte("evoPhase"));
            instance.setCooldownUntilFromNBT(tags.getLong("cooldownUntil"));
        }
    }
}

