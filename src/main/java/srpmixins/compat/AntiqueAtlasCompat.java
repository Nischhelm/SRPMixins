package srpmixins.compat;

import com.dhanantry.scapeandrunparasites.init.SRPBiomes;
import hunternif.mc.atlas.AntiqueAtlasMod;
import hunternif.mc.atlas.api.AtlasAPI;
import hunternif.mc.atlas.api.TileAPI;
import hunternif.mc.atlas.client.TextureSet;
import hunternif.mc.atlas.ext.ExtTileIdMap;
import net.minecraft.util.ResourceLocation;

public class AntiqueAtlasCompat {
    public static int bloodyRiverPseudoBiomeId = -1;

    public static TextureSet PARA_BIOME;
    public static TextureSet BLOODY_RIVER;

    public static void initTiles(){
        TileAPI tileAPI = AtlasAPI.getTileAPI();
        BLOODY_RIVER = tileAPI.registerTextureSet("BLOODY_RIVER",
                tileLoc("bloody_river"),
                tileLoc("bloody_river2"));
        PARA_BIOME = tileAPI.registerTextureSet("PARASITE_BIOME",
                tileLoc("parasite_empty"),
                tileLoc("parasite_mound"),
                tileLoc("parasite_forest"),
                tileLoc("parasite_forest"),
                tileLoc("parasite_flowers"),
                tileLoc("parasite_flowers"));

        //Stitching
        BLOODY_RIVER.stitchTo(TextureSet.WATER); //flows continuously into normal water
        PARA_BIOME.stitchTo(BLOODY_RIVER); //no hard edge towards bloody water

        bloodyRiverPseudoBiomeId = ExtTileIdMap.instance().getOrCreatePseudoBiomeID("bloodyRiver");
        tileAPI.setCustomTileTexture("bloodyRiver", BLOODY_RIVER);
        tileAPI.setBiomeTexture(SRPBiomes.biomeInfested, PARA_BIOME);
    }

    public static ResourceLocation tileLoc(String tileName){
        return new ResourceLocation(AntiqueAtlasMod.ID, "textures/gui/tiles/" + tileName + ".png");
    }
}
