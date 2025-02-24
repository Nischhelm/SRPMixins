package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityLencia;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityLencia.class)
public abstract class BogleMeleeFix extends EntityMob {
    public BogleMeleeFix(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "applyEntityAttributes",
            at = @At(value = "TAIL")
    )
    private void fixMeleeAuraDmg(CallbackInfo ci){
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(SRPMixinsConfigHandler.dmgfix.bogleMeleeDamage * SRPConfig.globalDamageMultiplier);
    }
}
