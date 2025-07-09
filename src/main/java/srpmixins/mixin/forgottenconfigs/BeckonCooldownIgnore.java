package srpmixins.mixin.forgottenconfigs;

import com.dhanantry.scapeandrunparasites.entity.ai.EntityAIVenkrolSummon;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationaryArchitect;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(EntityAIVenkrolSummon.class)
public abstract class BeckonCooldownIgnore {
    @Shadow(remap = false) private int attackTimer;
    @Shadow(remap = false) @Final private EntityPStationaryArchitect parentEntity;

    @Inject(
            method = "<init>",
            at = @At(value = "RETURN"),
            remap = false
    )
    private void srpmixins_beckonCooldown_init(EntityPStationaryArchitect venkrol, int limit, int cooldown, int stage, int CAV, double CAM, CallbackInfo ci){
        if(SRPConfigSystems.useEvolution && !venkrol.world.isRemote)
            if(SRPSaveDataInterface.get(venkrol.world, null, venkrol.getPosition()).getEvolutionPhase(venkrol.dimension) >= SRPConfigSystems.evolutionBeckonIgnoreCooldown)
                this.attackTimer = 0;
    }

    @ModifyExpressionValue(
            method = "updateTask",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/EntityAIVenkrolSummon;sCooldown:I", remap = false)
    )
    private int srpmixins_beckonCooldown_updateTask(int original){
        EntityPStationaryArchitect beckon = this.parentEntity;
        if(beckon.world.isRemote) return original;
        if(SRPSaveDataInterface.get(beckon.world, null, beckon.getPosition()).getEvolutionPhase(beckon.dimension) >= SRPConfigSystems.evolutionBeckonIgnoreCooldown)
            return 0;
        return original;
    }

    @ModifyExpressionValue(
            method = "setNearbyFree",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/EntityAIVenkrolSummon;sCooldown:I"),
            remap = false
    )
    private int srpmixins_beckonCooldown_setNearbyFree(int original){
        EntityPStationaryArchitect beckon = this.parentEntity;
        if(beckon.world.isRemote) return original;
        if(SRPSaveDataInterface.get(beckon.world, null, beckon.getPosition()).getEvolutionPhase(beckon.dimension) >= SRPConfigSystems.evolutionBeckonIgnoreCooldown)
            return 0;
        return original;
    }
}
