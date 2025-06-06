package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityLesh;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.head.*;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(value = {
        EntityPStationary.class,
        EntityLesh.class,
        EntityInfCowHead.class,
        EntityInfDragonEHead.class,
        EntityInfHorseHead.class,
        EntityInfHumanHead.class,
        EntityInfPigHead.class,
        EntityInfPlayerHead.class,
        EntityInfSheepHead.class,
        EntityInfVillagerHead.class,
        EntityInfWolfHead.class
})
public abstract class EntityAnyMixin_onLivingUpdate extends Entity {
    public EntityAnyMixin_onLivingUpdate(World worldIn) {
        super(worldIn);
    }

    @Redirect(
            method="onLivingUpdate",
            at=@At(value="INVOKE",target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;", remap = false)
    )
    public SRPSaveData srpmixins_getPlayerData(World world, int id){
        return SRPSaveDataInterface.get(world,null,this.getPosition());
    }
}