package srpmixins.mixin.logging;

import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityRof;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.SRPMixins;

@Mixin(value = EntityRof.class, remap = false)
public abstract class EntityRofMixin_Log {

    @Shadow public EntityLivingBase targetScent;

    // Intercept world.spawnEntity return in general? Instead, log in onLivingUpdate when it first becomes active
    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    private void srpmixins$logRofHead(CallbackInfo ci){
        EntityRof self = (EntityRof)(Object)this;
        if (self.world.isRemote) return;
        if (self.ticksExisted == 1){
            SRPMixins.logInWorld(self.world, String.format("[ScentWave] worm=%d pos=%d,%d,%d tgt=%s minmob=%d maxmob=%d",
                    self.getEntityId(), (int)Math.floor(self.posX), (int)Math.floor(self.posY), (int)Math.floor(self.posZ),
                    targetScent == null ? "none" : targetScent instanceof EntityPlayer ? "player" : String.valueOf(EntityList.getKey(targetScent)), self.minmob, self.maxmob));
        }
    }

    // Intercept world.spawnEntity return in general? Instead, log in onLivingUpdate when it first becomes active
    @WrapOperation(method = "spawnWaves", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private boolean srpmixins$logRofInvoke(World world, Entity entity, Operation<Boolean> original){
        boolean spawned = original.call(world, entity);
        if (world.isRemote) return false;
        SRPMixins.logInWorld(world, String.format("[ScentWaveSpawn] worm=%d spawned=%s",
                ((EntityRof)(Object)this).getEntityId(), entity == null ? "none" : EntityList.getKey(entity).toString()));
        return spawned;
    }

    // Detailed mob logging from ROF spawns is not hooked here to avoid fragile redirects.
}
