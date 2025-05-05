package srpmixins.mixin.spawning;

import com.dhanantry.scapeandrunparasites.init.SRPSpawning;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;

import java.util.ArrayList;

@Mixin(SRPSpawning.class)
public abstract class SpawningBlacklistByBiomeNoEvolution {

    @WrapWithCondition(
            method = "init",
            at = @At(value = "INVOKE", target = "Lcom/dhanantry/scapeandrunparasites/init/SRPSpawning;addSpawn(ILjava/lang/Class;IILnet/minecraft/world/biome/Biome;IZ)V"),
            remap = false
    )
    private static boolean srpmixins_blacklistBiomesAndDimensions(int type, Class<? extends EntityLiving> entity, int groupMin, int groupMax, Biome biome, int weight, boolean addSpawn){
        ArrayList<String> biomeBlacklist = SRPMixinsConfigProvider.biomeSpawningBlacklists.get(Integer.MAX_VALUE);
        if(biomeBlacklist == null) return true;

        ResourceLocation biomeReg = biome.getRegistryName();
        if(biomeReg == null) return true;
        String currBiome = biomeReg.toString();
        String currBiomeMod = biomeReg.getNamespace();
        boolean isInList = biomeBlacklist.contains(currBiome) ||
                biomeBlacklist.contains(currBiomeMod) ||
                biomeBlacklist.isEmpty();

        return isInList == SRPMixinsConfigHandler.spawns.biomeBlacklistIsWhitelist;
    }
}