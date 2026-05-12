package srpmixins.mixin.customphases;

import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.util.IIsTicking;

import java.util.HashMap;
import java.util.Map;

@Mixin(SRPSaveData.class)
public abstract class SRPSaveDataMixin_TickData implements IIsTicking {
    //This class overwrites the original cooldown handling (which was skethy AF) to use a ticking system instead

    @Inject(method = "writeToNBT", at = @At("TAIL"))
    private void srpmixins_writeTick(NBTTagCompound compound, CallbackInfoReturnable<NBTTagCompound> cir){
        compound.setLong("srpmixins_tick", srpmixins$tickCount);
    }

    @Inject(method = "readFromNBT", at = @At("TAIL"))
    private void srpmixins_readTick(NBTTagCompound compound, CallbackInfo ci){
        this.srpmixins$tickCount = compound.getLong("srpmixins_tick");
    }

    @Inject(method = "setCooldown", at = @At("HEAD"), remap = false)
    private void srpmixins_setCooldownTick(int newCooldownSeconds, World world, int dim, CallbackInfo ci){
        if(newCooldownSeconds <= 0) return;
        this.srpmixins$cooldowns.put(dim, this.srpmixins$cooldowns.getOrDefault(dim, 0) + newCooldownSeconds * 20);
    }

    @Inject(method = "getCooldown", at = @At("HEAD"), remap = false, cancellable = true)
    private void srpmixins_getCooldownTick(World worldIn, int dim, CallbackInfoReturnable<Integer> cir){
        cir.setReturnValue(srpmixins$cooldowns.getOrDefault(dim, 0) / 20);
    }

    @Unique private long srpmixins$tickCount = 0;
    @Unique private final Map<Integer,Integer> srpmixins$cooldowns = new HashMap<>();

    @Override
    public long srpmixins$getTick() {
        return this.srpmixins$tickCount;
    }

    @Override
    public void srpmixins$setTick(long tick) {
        this.srpmixins$tickCount = tick;
    }

    @Override
    public void srpmixins$tick(int by) {
        this.srpmixins$tickCount += by; //Tick internal clock up

        for(Integer dim : this.srpmixins$cooldowns.keySet()) {
            int cooldown = this.srpmixins$cooldowns.get(dim);
            if(cooldown <= 0) continue;
            this.srpmixins$cooldowns.put(dim, Math.max(cooldown - by, 0)); //tick cooldowns down
        }
    }
}
