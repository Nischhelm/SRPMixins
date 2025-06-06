package srpmixins.mixin.playerphases;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.*;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import srpmixins.util.customphasemechanics.SRPSaveDataInterface;

@Mixin(value = {
        EntityPInfected.class,
        EntityPFeral.class,
        EntityPAdapted.class,
        EntityPPure.class,
        EntityPPrimitive.class,
        EntityPStationary.class
})
public abstract class EntityPAnyMixin_deathDislo extends Entity {
    public EntityPAnyMixin_deathDislo(World worldIn) {
        super(worldIn);
    }

    @Redirect(
            method = "onDeathDislo",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;get(Lnet/minecraft/world/World;I)Lcom/dhanantry/scapeandrunparasites/world/SRPSaveData;"),
            remap = false
    )
    public SRPSaveData srpmixins_getPlayerData(World world, int id) {
        return SRPSaveDataInterface.get(world, null, this.getPosition());
    }
}