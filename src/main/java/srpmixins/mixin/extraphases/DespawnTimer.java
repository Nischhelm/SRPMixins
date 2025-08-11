package srpmixins.mixin.extraphases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.providers.SRPMobConfigProvider;
import srpmixins.rules.ruleset.DespawnTimerRuleSet;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

import java.util.HashMap;
import java.util.Map;

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
        if(DespawnTimerRuleSet.INSTANCE.hasNoRules()) return;
        if(this.world.isRemote) return;

        int paraId = this.getParasiteIDRegister();
        int dimId = this.world.provider.getDimension();

        Map<String, Object> actualValues = new HashMap<>();
        actualValues.put("dim", dimId);
        actualValues.put("phase", SRPSaveDataInterface.get(this.world, null, this.getPosition()).getEvolutionPhase(dimId));
        actualValues.put("mob", paraId);
        actualValues.put("group", SRPMobConfigProvider.getParaGroup(paraId));

        int totalTimer = DespawnTimerRuleSet.INSTANCE.getTotalTimer(actualValues);
        if(totalTimer > 0) this.srpmixins$despawnTimer = totalTimer;
    }

    @Inject(
            method = "writeEntityToNBT",
            at = @At("TAIL")
    )
    private void writeTimerToNBT(NBTTagCompound compound, CallbackInfo ci){
        if(this.srpmixins$despawnTimer > 0)
            compound.setInteger("srpmixinsDespawnTimer", this.srpmixins$despawnTimer);
    }

    @Inject(
            method = "readEntityFromNBT",
            at = @At("TAIL")
    )
    private void readTimerFromNBT(NBTTagCompound compound, CallbackInfo ci){
        if(compound.hasKey("srpmixinsDespawnTimer"))
            this.srpmixins$despawnTimer = compound.getInteger("srpmixinsDespawnTimer");
    }
}
