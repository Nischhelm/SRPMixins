package srpmixins.mixin.spawning.entirefix;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import srpmixins.handlers.WorldMobCapHandler;

import java.util.Collections;
import java.util.List;

@Debug(export = true)
@Mixin(targets = "com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityIki.AIBomb")
public abstract class PrimVerminSummonFix {
    @Shadow(remap = false) @Final private EntityParasiteBase parent;

    @ModifyExpressionValue(
            method = "updateTask",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;loadedEntityList:Ljava/util/List;")
    )
    private List<Entity> srpmixins_dontCountBruh(List<Entity> original){
        return Collections.emptyList();
    }

    @ModifyVariable(
            method = "updateTask",
            at = @At(value = "FIELD", target = "Lcom/dhanantry/scapeandrunparasites/util/config/SRPConfig;worldGnatCap:I", remap = false),
            name = "count"
    )
    private int srpmixins_useGlobalCount(int value){
        return WorldMobCapHandler.gnatCount.getOrDefault(this.parent.world.provider.getDimension(), 0);
    }
}
