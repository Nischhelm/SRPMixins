package srpmixins.mixin.modcompat.antiqueatlas;

import com.dhanantry.scapeandrunparasites.init.SRPBiomes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import hunternif.mc.atlas.core.BiomeDetectorBase;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import srpmixins.compat.AntiqueAtlasCompat;

@Mixin(BiomeDetectorBase.class)
public abstract class RenderParasiteBiome {
    @ModifyExpressionValue(
            method = "getBiomeID",
            at = @At(value = "FIELD", target = "Lhunternif/mc/atlas/core/BiomeDetectorBase;waterPoolBiomeID:I"),
            remap = false
    )
    private int aaam_replacePondsIfParasiteBiome(int original, @Local(name = "biomeID") int biomeId){
        if(biomeId == Biome.getIdForBiome(SRPBiomes.biomeInfested))
            return AntiqueAtlasCompat.bloodyRiverPseudoBiomeId;
        return original;
    }

    @ModifyExpressionValue(
            method = "getBiomeID",
            at = @At(value = "FIELD", target = "Lhunternif/mc/atlas/core/BiomeDetectorBase;doScanRavines:Z"),
            remap = false
    )
    private boolean aaam_skipRavinesIfParasiteBiome(boolean original, @Local(name = "biomeID") int biomeId){
        return original && biomeId != Biome.getIdForBiome(SRPBiomes.biomeInfested); //don't render ravines in para biome (looks bad)
    }
}
