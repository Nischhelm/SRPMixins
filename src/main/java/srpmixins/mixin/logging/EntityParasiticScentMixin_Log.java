package srpmixins.mixin.logging;

import com.dhanantry.scapeandrunparasites.entity.EntityParasiticScent;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(value = EntityParasiticScent.class, remap = false)
public abstract class EntityParasiticScentMixin_Log extends Entity {

    @Shadow private byte scentState;
    @Shadow private int lifeTicks;
    @Shadow private int currentL;
    @Shadow private int dangerToUs;
    @Shadow private byte active;
    @Shadow private int delay;
    @Shadow private byte scentLevel;
    @Shadow private byte scentReaction;
    @Shadow private int minwave;
    @Shadow private int maxwave;
    @Shadow private int minmob;
    @Shadow private int maxmob;
    @Shadow private boolean followTargetScent;
    @Shadow protected abstract EntityLivingBase getTargetToKill();

    @Unique private int srpmixins$lastTickLog = 0;

    public EntityParasiticScentMixin_Log(World worldIn) { super(worldIn); }

    // Periodic snapshot just before state switch (log every N ticks server-side)
    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/EntityParasiticScent;scentFollower()V", shift = At.Shift.AFTER))
    private void srpmixins$logScentTick(CallbackInfo ci){
        if (this.world.isRemote) return;
        int interval = Math.max(1, SRPMixinsConfigHandler.logs.tickInterval);
        if ((ticksExisted - srpmixins$lastTickLog) < interval) return;
        srpmixins$lastTickLog = ticksExisted;

        EntityLivingBase tgt = getTargetToKill();
        String tgtName = tgt == null ? "none" : tgt instanceof EntityPlayer ? "player" : String.valueOf(EntityList.getKey(tgt));
        int dim = this.world.provider.getDimension();
        int lifeRem = Math.max(0, lifeTicks - currentL);
        String msg = String.format("[ScentTick] id=%d dim=%d pos=%d,%d,%d tgt=%s d2=%.1f state=%d follow=%s active=%d react=%d delay=%d life=%d danger=%d lvl=%d wave=%d-%d mob=%d-%d",
                this.getEntityId(), dim, (int)Math.floor(this.posX), (int)Math.floor(this.posY), (int)Math.floor(this.posZ),
                tgtName, tgt == null ? -1.0 : tgt.getDistanceSq(this), (int)scentState, followTargetScent, (int)active,
                (int)scentReaction, delay, lifeRem, dangerToUs, (int)scentLevel, minwave, maxwave, minmob, maxmob);
        SRPMixins.logInWorld(this.world, msg);
    }

    // Transition to Tactical (state 4)
    @Inject(method = "scentListener", at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/EntityParasiticScent;warnPlayers(Ljava/lang/String;)V"))
    private void srpmixins$logScentActive(CallbackInfo ci){
        if (this.world.isRemote) return;
        EntityLivingBase tgt = getTargetToKill();
        String tgtName = tgt == null ? "none" : String.valueOf(EntityList.getKey(tgt));
        SRPMixins.logInWorld(this.world, String.format("[Observer→Tactical] id=%d active=%d react=%d life=%d target=%s",
                getEntityId(), (int)active, (int)scentReaction, lifeTicks, tgtName));
    }

    // Tactical -> Attacker
    @Inject(method = "scentTactical", at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/entity/EntityParasiticScent;scentState:B", ordinal = 0))
    private void srpmixins$logScentTactical(CallbackInfo ci){
        if (this.world.isRemote) return;
        SRPMixins.logInWorld(this.world, String.format("[Tactical→Attacker] id=%d active=%d", getEntityId(), (int)active));
    }

    // Tactical -> Observer1
    @Inject(method = "scentTactical", at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/entity/EntityParasiticScent;scentState:B", ordinal = 1))
    private void srpmixins$logScentTactical2(CallbackInfo ci){
        if (this.world.isRemote) return;
        SRPMixins.logInWorld(this.world, String.format("[Tactical→Observer1] id=%d active=%d", getEntityId(), (int)active));
    }

    // Tactical -> Attacker
    @Inject(method = "scentAttacker", at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/entity/EntityParasiticScent;scentState:B", ordinal = 0))
    private void srpmixins$logScentscentAttacker(CallbackInfo ci){
        if (this.world.isRemote) return;
        SRPMixins.logInWorld(this.world, String.format("[Attacker→Builder] id=%d active=%d", getEntityId(), (int)active));
    }

    // Tactical -> Observer1
    @Inject(method = "scentAttacker", at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/entity/EntityParasiticScent;scentState:B", ordinal = 1))
    private void srpmixins$logScentscentAttacker2(CallbackInfo ci){
        if (this.world.isRemote) return;
        SRPMixins.logInWorld(this.world, String.format("[Attacker→Tactical] id=%d active=%d", getEntityId(), (int)active));
    }

    // Attacker outcome
    @Inject(method = "scentAttacker", at = @At("TAIL"))
    private void srpmixins$logScentAttacker(CallbackInfo ci){
        if (this.world.isRemote) return;
        SRPMixins.logInWorld(this.world, String.format("[ScentAttacker] id=%d nextState=%d delay=%d life=%d", getEntityId(), (int)scentState, delay, lifeTicks));
    }

    // Tactical -> Observer1
    @Inject(method = "scentBuilder", at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/entity/EntityParasiticScent;scentState:B"))
    private void srpmixins$logScentscentBuilder(CallbackInfo ci){
        if (this.world.isRemote) return;
        SRPMixins.logInWorld(this.world, String.format("[Builder→Tactical] id=%d active=%d", getEntityId(), (int)active));
    }

    // Builder summary (after loop & state/delay adjustments)
    @Inject(method = "scentBuilder", at = @At("TAIL"))
    private void srpmixins$logScentBuilder(CallbackInfo ci){
        if (this.world.isRemote) return;
        SRPMixins.logInWorld(this.world, String.format("[ScentBuilder] id=%d nextState=%d delay=%d life=%d", getEntityId(), (int)scentState, delay, lifeTicks));
    }

    // Follow snap
    @Inject(method = "scentFollower", at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/EntityParasiticScent;copyLocationAndAnglesFrom(Lnet/minecraft/entity/Entity;)V"))
    private void srpmixins$logFollowerSnap(CallbackInfo ci){
        if (this.world.isRemote) return;
        EntityLivingBase tgt = getTargetToKill();
        if (tgt != null && tgt.isPotionActive(SRPPotions.PREY_E)){
            SRPMixins.logInWorld(this.world, String.format("[ScentSnap] id=%d to=%d d2=%.1f", getEntityId(), tgt.getEntityId(), tgt.getDistanceSq(this)));
        }
    }

    // Target set success/failure
    @Inject(method = "setTargetToKill", at = @At("HEAD"))
    private void srpmixins$logSetTargetHead(EntityLivingBase in, boolean checkATT, CallbackInfoReturnable<Boolean> cir){
        if (this.world.isRemote) return;
        String name = in == null ? "null" : in instanceof EntityPlayer ? "player" : String.valueOf(EntityList.getKey(in));
        SRPMixins.logInWorld(this.world, String.format("[ScentTarget?] id=%d target=%s check=%s", getEntityId(), name, checkATT));
    }

    @Inject(method = "setTargetToKill", at = @At("RETURN"))
    private void srpmixins$logSetTargetReturn(EntityLivingBase in, boolean checkATT, CallbackInfoReturnable<Boolean> cir){
        if (this.world.isRemote) return;
        SRPMixins.logInWorld(this.world, String.format("[ScentTarget] id=%d result=%s", getEntityId(), cir.getReturnValue()));
    }

    // Hook placeWaves to tag spawned Rof with source scent id and log
    @Inject(method = "placeWaves", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z", ordinal = 0))
    private void srpmixins$logPlaceWaves(int minDist, int maxDist, CallbackInfoReturnable<Integer> cir){
        if (this.world.isRemote) return;
        // Last created entity before spawn is the local variable 'samuel' (EntityRof)
        // We can’t access it directly without @Local, so log a generic message at our pos and rely on Rof mixin for detailed attribution.
        SRPMixins.logInWorld(this.world, String.format("[ScentWave] scent=%d pos=%d,%d,%d minmob=%d maxmob=%d",
                getEntityId(), (int)Math.floor(this.posX), (int)Math.floor(this.posY), (int)Math.floor(this.posZ), minmob, maxmob));
    }
}
