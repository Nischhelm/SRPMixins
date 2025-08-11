package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityFlam;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityFlam.class)
public abstract class SuccorFix extends EntityMob {
    public SuccorFix(World worldIn) {
        super(worldIn);
    }

    @ModifyArg(
            method = "setDamageATT",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/attributes/IAttributeInstance;setBaseValue(D)V")
    )
    private double srpmixins_fixSuccorDamageMixin(double original) {
        if (SRPMixinsConfigHandler.dmgfix.fixSuccorDamage)
            return SRPMixinsConfigHandler.dmgfix.fixedSuccorDamage * SRPConfig.globalDamageMultiplier;
        return original;
    }
}