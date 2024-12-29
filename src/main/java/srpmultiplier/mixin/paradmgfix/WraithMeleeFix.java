package srpmultiplier.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityElvia;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmultiplier.handlers.SRPMultiplierConfigHandler;

@Mixin(EntityElvia.class)
public class WraithMeleeFix extends EntityMob {
    public WraithMeleeFix(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "func_110147_ax",
            at = @At(value = "TAIL"),
            remap = false
    )
    void fixMeleeAuraDmg(CallbackInfo ci){
        if(SRPMultiplierConfigHandler.server.doDamageFixes)
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(SRPMultiplierConfigHandler.server.wraithMeleeDamage);
    }
}
