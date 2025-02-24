package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.monster.ancient.EntityOronco;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityOronco.class)
public abstract class DreadnaughtMeleeFix extends EntityMob {
    public DreadnaughtMeleeFix(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "applyEntityAttributes",
            at = @At(value = "TAIL")
    )
    private void fixMeleeAuraDmg(CallbackInfo ci){
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(SRPMixinsConfigHandler.dmgfix.dreadnaughtMeleeDamage * SRPConfig.globalDamageMultiplier);
    }
}
