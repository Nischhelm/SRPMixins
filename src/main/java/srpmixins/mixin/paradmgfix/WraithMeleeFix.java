package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityElvia;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityElvia.class)
public abstract class WraithMeleeFix extends EntityMob {
    public WraithMeleeFix(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "applyEntityAttributes",
            at = @At(value = "TAIL")
    )
    private void srpmixins_fixMeleeAuraDmg(CallbackInfo ci){
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(SRPMixinsConfigHandler.dmgfix.wraithMeleeDamage * SRPConfig.globalDamageMultiplier);
    }
}
