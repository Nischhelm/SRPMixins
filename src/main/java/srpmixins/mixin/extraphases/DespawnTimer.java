package srpmixins.mixin.extraphases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.rules.rulesetholder.DespawnTimerRuleSetHolder;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(EntityParasiteBase.class)
public abstract class DespawnTimer extends EntityLivingBase {
    public DespawnTimer(World worldIn) {
        super(worldIn);
    }

    @Shadow(remap = false) public abstract void cannotDespawn(boolean in);
    @Shadow(remap = false) public abstract int getParasiteIDRegister();

    @Unique private int srpmixins$despawnTimer = -1;

    @ModifyReturnValue(
            method = "canDespawn",
            at = @At("RETURN")
    )
    private boolean srpmixins_allowDespawnAfterCertainTime(boolean original){
        if(original) return true;
        if(srpmixins$despawnTimer == -1) return original;

        if(this.ticksExisted > srpmixins$despawnTimer){
            this.cannotDespawn(true); //should be called setCanDespawn
            return true; //can despawn from now on
        }
        return original;
    }

    @Inject(
            method = "cannotDespawn",
            at = @At("TAIL"),
            remap = false
    )
    private void srpmixins_allowDespawnAfterCertainTime(boolean newCanDespawnFlag, CallbackInfo ci){
        if(newCanDespawnFlag) return; //no timer for mobs that can just despawn normally
        if(DespawnTimerRuleSetHolder.INSTANCE.hasNoRules()) return;

        int paraId = this.getParasiteIDRegister();
        String group = SRPMobConfigProvider.getParaGroup(paraId);
        int dimId = this.world.provider.getDimension();
        byte phase = SRPSaveDataInterface.get(this.world, null, this.getPosition()).getEvolutionPhase(dimId);

        int totalTimer = DespawnTimerRuleSetHolder.INSTANCE.getTotalTimer(dimId, phase, paraId, group);
        if(totalTimer > 0) this.srpmixins$despawnTimer = totalTimer;
    }
}
