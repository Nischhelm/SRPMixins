package srpmultiplier.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityFlam;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
        if (SRPMultiplierConfigHandler.dmgfix.fixSuccorDamage) {
            float fixedDmg = SRPMultiplierConfigHandler.dmgfix.fixedSuccorDamage * SRPConfig.globalDamageMultiplier;
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(fixedDmg);
            ci.cancel();
        }
    }
}