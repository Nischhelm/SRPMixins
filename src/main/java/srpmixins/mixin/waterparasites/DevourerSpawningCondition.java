package srpmixins.mixin.waterparasites;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityLum;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityLum.class)
public abstract class DevourerSpawningCondition extends EntityParasiteBase {
    public DevourerSpawningCondition(World worldIn) {
        super(worldIn);
    }

    @ModifyReturnValue(
            method = "getCanSpawnHere",
            at = @At("RETURN")
    )
    public boolean getCanSpawnHere(boolean original) {
        if(!original) return false;
        if(this.world.getDifficulty() == EnumDifficulty.PEACEFUL || SRPConfig.spawnDays > (int) this.world.getWorldTime()) return false;

        if ((SRPConfigSystems.useEvolution && this.phaseCreated >= SRPConfigSystems.evolutionSpawningIgnoreSunlight)
                ||
            (!SRPConfigSystems.useEvolution && SRPConfig.ignoreL)
        )
            return this.isValidLightLevelTwo();
        else
            return this.isValidLightLevelOne();
    }
}
