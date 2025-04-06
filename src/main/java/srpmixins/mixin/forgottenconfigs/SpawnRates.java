package srpmixins.mixin.forgottenconfigs;

import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityMes;
import com.dhanantry.scapeandrunparasites.entity.monster.deterrent.EntityNak;
import com.dhanantry.scapeandrunparasites.entity.monster.feral.EntityFerBear;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityGothol;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.EntityOrch;
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
public abstract class SpawnRates {
    @Shadow(remap = false) public static void addSpawn(int type, Class<? extends EntityLiving> entity, int groupMin, int groupMax, Biome biome, int weight, boolean addSpawn) {}

    @Inject(
            method = "init",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/init/SRPSpawning;addSpawn(ILjava/lang/Class;IILnet/minecraft/world/biome/Biome;IZ)V", ordinal = 0),
            remap = false
    )
    private static void srpmixins_useForgottenSpawnRates(CallbackInfo ci, @Local Biome biome){
        addSpawn(0, EntityGothol.class, 1, 1, biome, SRPConfigMobs.gotholSpawnRate, SRPConfigMobs.gotholEnabled);
        addSpawn(0, EntityMes.class, 1, 1, biome, SRPConfigMobs.thrallSpawnRate, SRPConfigMobs.thrallEnabled);
        addSpawn(0, EntityFerBear.class, 1, 1, biome, SRPConfigMobs.ferbearSpawnRate, SRPConfigMobs.ferbearEnabled);
        addSpawn(0, EntityOrch.class, 1, 1, biome, SRPConfigMobs.orchSpawnRate, SRPConfigMobs.orchEnabled);
        addSpawn(0, EntityNak.class, 1, 1, biome, SRPConfigMobs.nakSpawnRate, SRPConfigMobs.nakEnabled);
    }
}
