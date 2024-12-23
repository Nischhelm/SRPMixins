package srpmultiplier.mixin.features;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityFlam;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(EntityFlam.class)
public abstract class SuccorFix extends EntityMob {

    public SuccorFix(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "setDamageATT",
            at = @At(value = "HEAD"),
            cancellable = true,
            remap = false
    )
    private void fixSuccorDamageMixin(EntityParasiteBase in, CallbackInfo ci) {
        if (SRPMultiplierConfigHandler.server.fixSuccorDamage) {
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(SRPMultiplierConfigHandler.server.fixedSuccorDamage);
            ci.cancel();
        }
    }
}