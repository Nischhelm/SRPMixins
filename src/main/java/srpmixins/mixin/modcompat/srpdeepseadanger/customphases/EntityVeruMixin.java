package srpmixins.mixin.modcompat.srpdeepseadanger.customphases;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import energon.srpdeepseadanger.entity.monster.deterrent.nexus.EntityVeru;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(EntityVeru.class)
public abstract class EntityVeruMixin extends Entity {
    public EntityVeruMixin(World worldIn) {
        super(worldIn);
    }

    @Redirect(
            method = "getGrowChance",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    private SRPSaveData srpmixins_useCustomPhases(World world){
        return SRPSaveDataInterface.get(world, null, this.getPosition());
    }
}
