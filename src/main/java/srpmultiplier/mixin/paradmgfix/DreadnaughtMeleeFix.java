package srpmultiplier.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.monster.ancient.EntityOronco;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(EntityOronco.class)
public class DreadnaughtMeleeFix extends EntityMob {
    public DreadnaughtMeleeFix(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "func_110147_ax",
            at = @At(value = "TAIL"),
            remap = false
    )
    void fixMeleeAuraDmg(CallbackInfo ci){
        if(SRPMultiplierConfigHandler.server.doDamageFixes)
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(SRPMultiplierConfigHandler.server.dreadnaughtMeleeDamage * SRPConfig.globalDamageMultiplier);
    }
}
