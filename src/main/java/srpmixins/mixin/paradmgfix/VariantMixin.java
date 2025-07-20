package srpmixins.mixin.paradmgfix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.adapted.*;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityMes;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.*;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfEnderman;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.*;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.*;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityPheon;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityVesta;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.providers.SRPMobConfigProvider;

import java.util.List;

@Mixin(value = {
        EntityBanoAdapted.class,
        EntityCanraAdapted.class,
        EntityEmanaAdapted.class,
        EntityHullAdapted.class,
        EntityNoglaAdapted.class,
        EntityRanracAdapted.class,
        EntityShycoAdapted.class,
        EntityBano.class,
        EntityCanra.class,
        EntityEmana.class,
        EntityHull.class,
        EntityNogla.class,
        EntityRanrac.class,
        EntityShyco.class,
        EntityLum.class,
        EntityMes.class,
        EntityButhol.class,
        EntityGothol.class,
        EntityMudo.class,
        EntityNuuh.class,
        EntityRathol.class,
        EntityInfEnderman.class,
        EntityAlafha.class,
        EntityAnged.class,
        EntityEsor.class,
        EntityFlog.class,
        EntityGanro.class,
        EntityOmboo.class,
        EntityOrch.class,
        EntityPheon.class,
        EntityVesta.class
})
public abstract class VariantMixin extends EntityParasiteBase {
    public VariantMixin(World worldIn) {
        super(worldIn);
    }

    @Shadow(remap = false) public abstract int getParasiteIDRegister();

    @ModifyExpressionValue(
            method = "onInitialSpawn",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;variantChance:D")
    )
    private double srpmixins_changeVariants(double original){
        if(this.phaseCreated >= SRPConfigSystems.evolutionParasiteAlwaysVariant || this.canChangeVariant) return 0;

        if(this.getRNG().nextFloat() >= SRPConfig.variantChance) return 0;

        int paraId = this.getParasiteIDRegister();
        String mobName = SRPMobConfigProvider.paraIdToMobName.get(paraId);
        List<Integer> availableVariants = SRPMobConfigProvider.mobNameToVariantsMap.get(mobName);
        String mobGroup = "";
        byte phaseCreated = this.phaseCreated;
        int dimId = this.world.provider.getDimension();

        return 0;
    }
}
