package srpmixins.mixin.waterparasites;

import com.dhanantry.scapeandrunparasites.entity.monster.infected.EntityInfSquid;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityLum;
import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SRPSpawning.class)
public abstract class WaterParaSpawnRates {
    @Shadow(remap = false) public static void addSpawn(int type, Class<? extends EntityLiving> entity, int groupMin, int groupMax, Biome biome, int weight, boolean addSpawn) {}

    @Inject(
            method = "init",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/init/SRPSpawning;addSpawn(ILjava/lang/Class;IILnet/minecraft/world/biome/Biome;IZ)V", ordinal = 0),
            remap = false
    )
    private static void srpmixins_useWaterParaSpawnRates(CallbackInfo ci, @Local Biome biome){
        addSpawn(0, EntityInfSquid.class, 1, 1, biome, SRPConfigMobs.infsquidSpawnRate, SRPConfigMobs.infsquidEnabled);
        addSpawn(0, EntityLum.class, 1, 1, biome, SRPConfigMobs.lumSpawnRate, SRPConfigMobs.lumEnabled);
    }
}
