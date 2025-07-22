package srpmixins.mixin.waterparasites;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfSquid;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.compat.CompatUtil;
import srpmixins.compat.SRPExtraCompat;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.handlers.WorldMobCapHandler;

@Mixin(ParasiteEventEntity.class)
public abstract class WaterParasiteCapOnConversion {
    @WrapWithCondition(
            method = "convertEntity",
            at = @At(value="INVOKE",target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z", ordinal = 0)
    )
    private static boolean srpmixins_infSquidCap(World world, Entity entity){
        if(srpmixins$isWaterPara(entity)) {
            if(SRPMixinsConfigHandler.spawns.fixSpawningEntirely)
                return WorldMobCapHandler.waterCount.get(world.provider.getDimension()) < SRPMixinsConfigHandler.waterparas.waterParasiteCap;
            else {
                int infSquidCount = (int) world.loadedEntityList.stream().filter(e -> e instanceof EntityInfSquid).count();
                return infSquidCount < SRPMixinsConfigHandler.waterparas.waterParasiteCap;
            }
        }
        return true;
    }

    @Unique
    private static boolean srpmixins$isWaterPara(Entity entity){
        if(entity instanceof EntityInfSquid) return true;
        if(CompatUtil.srpextra.isLoaded() && SRPExtraCompat.isWaterParasite(entity)) return true;
        return false;
    }
}