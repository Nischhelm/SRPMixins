package srpmixins.mixin.waterparasites;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfSquid;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;

@Mixin(ParasiteEventEntity.class)
public abstract class InfSquidCap {
    @WrapWithCondition(
            method = "convertEntity",
            at = @At(value="INVOKE",target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z", ordinal = 0)
    )
    private static boolean srpmixins_infSquidCap(World world, Entity entity){
        if(entity instanceof EntityInfSquid) {
            int infSquidCount = (int) world.loadedEntityList.stream().filter(e -> e instanceof EntityInfSquid).count();
            return infSquidCount < SRPMixinsConfigHandler.waterparas.waterParasiteCap;
        }
        return true;
    }
}