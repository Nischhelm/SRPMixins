package srpmixins.mixin.tendrilfix;

import com.dhanantry.scapeandrunparasites.entity.monster.derived.EntityHeblu;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfDragonE;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.handlers.TendrilSyncHandler;

@Mixin(value = {EntityInfDragonE.class, EntityHeblu.class})
public abstract class SimDragonTendrils extends Entity implements TendrilSyncHandler.ISyncsTendrils {
    public SimDragonTendrils(World worldIn) {
        super(worldIn);
    }

    @Shadow(remap = false) private float headlHealth;
    @Shadow(remap = false) private float leftTendrilHealth;
    @Shadow(remap = false) private float rightTendrilHealth;

    @Inject(
            method = "readEntityFromNBT",
            at = @At(value = "TAIL")
    )
    private void srpmixins_readHeadHealth(NBTTagCompound compound, CallbackInfo ci){
        if (compound.hasKey("parasiteHeadTendril", 5))
            this.headlHealth = compound.getFloat("parasiteHeadTendril");
    }

    @Inject(
            method = "writeEntityToNBT",
            at = @At(value = "TAIL")
    )
    private void srpmixins_writeHeadHealth(NBTTagCompound compound, CallbackInfo ci){
        compound.setFloat("parasiteHeadTendril", this.headlHealth);
    }

    @Override
    public void srpmixins$onTracking(){
        if(this.leftTendrilHealth <= 0) this.world.setEntityState(this, (byte) 11);
        if(this.rightTendrilHealth <= 0) this.world.setEntityState(this, (byte) 22);
        if(this.headlHealth <= 0) this.world.setEntityState(this, (byte) 33);
    }
}
