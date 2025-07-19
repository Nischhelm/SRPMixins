package srpmixins.mixin.spawning.entirefix.scentspawn;

import com.dhanantry.scapeandrunparasites.potion.SRPEffectBase;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import srpmixins.handlers.WorldMobCapHandler;

import java.util.Collections;
import java.util.List;

@Mixin(SRPEffectBase.class)
public abstract class SRPEffectBaseMixin {

    @ModifyExpressionValue(
            method = "effectPrey",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;loadedEntityList:Ljava/util/List;")
    )
    private List<Entity> srpmixins_dontCount(List<Entity> original){
        return Collections.emptyList();
    }

    @ModifyVariable(
            method = "effectPrey",
            name = "count",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfigSystems;scentCap:I"),
            remap = false
    )
    private int srpmixins_useOtherCounter(int original, @Local(argsOnly = true)EntityLivingBase entity){
        return WorldMobCapHandler.scentCount.getOrDefault(entity.world.provider.getDimension(), 0);
    }
}
