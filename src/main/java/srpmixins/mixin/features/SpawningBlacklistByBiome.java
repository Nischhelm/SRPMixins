package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.world.SRPWorldEntitySpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.config.SRPMixinsConfigProvider;

import java.util.ArrayList;

@Mixin(SRPWorldEntitySpawner.class)
public abstract class SpawningBlacklistByBiome {

    @Inject(
            method = "getSpawnListEntryForTypeAt",
            at = @At(value = "HEAD"),
            remap = false,
            cancellable = true
    )
    private static void srpmixins_blacklistBiomesAndDimensions(WorldServer worldServerIn, BlockPos pos, CallbackInfoReturnable<Biome.SpawnListEntry> cir){
        int dim = worldServerIn.provider.getDimension();
        ArrayList<String> biomeBlacklist = SRPMixinsConfigProvider.biomeSpawningBlacklists.get(dim);
        if(biomeBlacklist == null) return;

        ResourceLocation biome = worldServerIn.getBiome(pos).getRegistryName();
        if(biome == null) return;
        String currBiome = biome.toString();
        String currBiomeMod = biome.getNamespace();
        boolean isInList = biomeBlacklist.contains(currBiome) ||
                biomeBlacklist.contains(currBiomeMod) ||
                biomeBlacklist.isEmpty();

        if(isInList != SRPMixinsConfigHandler.various.biomeBlacklistIsWhitelist)
            cir.setReturnValue(null);
    }
}