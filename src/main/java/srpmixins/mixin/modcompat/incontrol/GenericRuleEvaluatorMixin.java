package srpmixins.mixin.modcompat.incontrol;

import com.dhanantry.scapeandrunparasites.world.SRPWorldData;
import mcjty.incontrol.rules.support.GenericRuleEvaluator;
import mcjty.tools.rules.CommonRuleEvaluator;
import mcjty.tools.rules.IModRuleCompatibilityLayer;
import mcjty.tools.typed.AttributeMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.compat.InControlCompat;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(GenericRuleEvaluator.class)
public abstract class GenericRuleEvaluatorMixin extends CommonRuleEvaluator {
    public GenericRuleEvaluatorMixin(AttributeMap map, Logger logger, IModRuleCompatibilityLayer compatibility) {
        super(map, logger, compatibility);
    }

    @Inject(method = "addChecks", at = @At("TAIL"), remap = false)
    private void srpmixins$injectEvoPhaseCheck(AttributeMap map, CallbackInfo ci){
        if (map.has(InControlCompat.MINEVOPHASE)) this.srpmixins$addMinEvoPhaseCheck(map);
        if (map.has(InControlCompat.MAXEVOPHASE)) this.srpmixins$addMaxEvoPhaseCheck(map);
        if (map.has(InControlCompat.MINNODES)) this.srpmixins$addMinNodesCheck(map);
        if (map.has(InControlCompat.MAXNODES)) this.srpmixins$addMaxNodesCheck(map);
        if (map.has(InControlCompat.MINCOLOS)) this.srpmixins$addMinColosCheck(map);
        if (map.has(InControlCompat.MAXCOLOS)) this.srpmixins$addMaxColosCheck(map);
    }

    @Unique
    private void srpmixins$addMinEvoPhaseCheck(AttributeMap map) {
        int minPhase = map.get(InControlCompat.MINEVOPHASE);
        this.checks.add((event, query) -> {
            World world = query.getWorld(event);
            BlockPos pos = query.getPos(event);
            int dimId = world.provider.getDimension();
            return SRPSaveDataInterface.get(world, null, pos).getEvolutionPhase(dimId) >= minPhase;
        });
    }

    @Unique
    private void srpmixins$addMaxEvoPhaseCheck(AttributeMap map) {
        int maxPhase = map.get(InControlCompat.MAXEVOPHASE);
        this.checks.add((event, query) -> {
            World world = query.getWorld(event);
            BlockPos pos = query.getPos(event);
            int dimId = world.provider.getDimension();
            return SRPSaveDataInterface.get(world, null, pos).getEvolutionPhase(dimId) <= maxPhase;
        });
    }

    @Unique
    private void srpmixins$addMinNodesCheck(AttributeMap map) {
        int minNodes = map.get(InControlCompat.MINNODES);
        this.checks.add((event, query) -> {
            World world = query.getWorld(event);
            return SRPWorldData.get(world).getNodes("x").size() >= minNodes;
        });
    }

    @Unique
    private void srpmixins$addMaxNodesCheck(AttributeMap map) {
        int maxNodes = map.get(InControlCompat.MAXNODES);
        this.checks.add((event, query) -> {
            World world = query.getWorld(event);
            return SRPWorldData.get(world).getNodes("x").size() <= maxNodes;
        });
    }

    @Unique
    private void srpmixins$addMinColosCheck(AttributeMap map) {
        int minNodes = map.get(InControlCompat.MINCOLOS);
        this.checks.add((event, query) -> {
            World world = query.getWorld(event);
            return SRPWorldData.get(world).getColonies("x").size() >= minNodes;
        });
    }

    @Unique
    private void srpmixins$addMaxColosCheck(AttributeMap map) {
        int maxNodes = map.get(InControlCompat.MAXCOLOS);
        this.checks.add((event, query) -> {
            World world = query.getWorld(event);
            return SRPWorldData.get(world).getColonies("x").size() <= maxNodes;
        });
    }
}
