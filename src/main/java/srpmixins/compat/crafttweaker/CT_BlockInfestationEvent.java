package srpmixins.compat.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.events.handling.MCBlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.SRPMixins;
import srpmixins.event.BlockInfestationEvent;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;

@SuppressWarnings("unused")
@ZenRegister
@ZenClass(SRPMixins.MODID + ".BlockInfestationEvent")
public class CT_BlockInfestationEvent extends MCBlockEvent {
    private final BlockInfestationEvent internal;

    public CT_BlockInfestationEvent(BlockInfestationEvent internal) {
        super(internal);
        this.internal = internal;
    }

    @ZenGetter("currentState")
    public IBlockState getCurrentState() {
        return CraftTweakerMC.getBlockState(internal.getWorld().getBlockState(internal.getPos()));
    }

    @ZenGetter("resultState")
    public IBlockState getResultState() {
        return CraftTweakerMC.getBlockState(internal.resultState);
    }

    @ZenSetter("resultState")
    public void setResultState(IBlockState state) {
        internal.resultState = CraftTweakerMC.getBlockState(state);
    }

    @ZenGetter("isBiomeInfestation")
    public boolean isParaBiome() {
        return internal.isParaBiome;
    }

    @ZenGetter("generatesFeaturesAbove")
    public boolean generatesAbove() {
        return internal.generatesAbove;
    }

    @ZenSetter("generatesFeaturesAbove")
    public void setGeneratesAbove(boolean val) {
        internal.generatesAbove = val;
    }

    @ZenGetter("generatesFeaturesBelow")
    public boolean generatesBelow() {
        return internal.generatesBelow;
    }

    @ZenSetter("generatesFeaturesBelow")
    public void setGeneratesBelow(boolean val) {
        internal.generatesBelow = val;
    }

    @ZenGetter("increasesPoints")
    public boolean increasesPoints() {
        return internal.increasesPoints;
    }

    @ZenSetter("increasesPoints")
    public void setIncreasesPoints(boolean val) {
        internal.increasesPoints = val;
    }

    @ZenMethod
    public void cancel() {
        internal.setCanceled(true);
    }

    public static class CT_EventForwarder {
        @SubscribeEvent
        public static void onInfestation(BlockInfestationEvent event) {
            if(CT_BlockInfestationExpansion.hasBlockHarvestDrops())
                CT_BlockInfestationExpansion.publishBlockHarvestDrops(new CT_BlockInfestationEvent(event));
        }
    }
}