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
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(EntityLencia.class)
public class BogleMeleeFix extends EntityMob {
    public BogleMeleeFix(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "func_110147_ax",
            at = @At(value = "TAIL"),
            remap = false
    )
    void fixMeleeAuraDmg(CallbackInfo ci){
        if(SRPMixinsConfigHandler.dmgfix.doDamageFixes)
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(SRPMixinsConfigHandler.dmgfix.bogleMeleeDamage * SRPConfig.globalDamageMultiplier);
    }
}
