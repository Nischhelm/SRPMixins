package srpmixins.mixin.waterparasites;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfSquid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(EntityInfSquid.class)
public abstract class InfSquidReach {
    @ModifyArgs(
            method = "initEntityAI",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/entity/ai/EntityAIAttackMeleeNotGround;<init>(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;DDDZI)V")
    )
    private void srpmixins_reduceInfSquidReach(Args args){
        args.set(1, SRPMixinsConfigHandler.waterparas.infSquidAttackRange); // = new EntityAIAttackMeleeNotGround(this, configValue, SRPConfig.primitiveFollow, 0.05, false, 0)
    }
}
