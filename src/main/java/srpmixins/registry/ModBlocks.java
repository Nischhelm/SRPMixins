package srpmixins.registry;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import srpmixins.SRPMixins;
import srpmixins.block.BlockEvolutionBed;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.tileentity.TileEntityEvolutionBed;

@Mod.EventBusSubscriber(modid = SRPMixins.MODID)
public class ModBlocks {

    @GameRegistry.ObjectHolder(SRPMixins.MODID + ":evolution_bed")
    public static BlockEvolutionBed evolutionBed = (BlockEvolutionBed) new BlockEvolutionBed().setRegistryName(new ResourceLocation(SRPMixins.MODID, "evolution_bed")).setTranslationKey(SRPMixins.MODID + ".evolution_bed");

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        if(SRPMixinsConfigHandler.lures.registerEvolutionBeds) {
            event.getRegistry().register(evolutionBed);
            GameRegistry.registerTileEntity(TileEntityEvolutionBed.class, new ResourceLocation(SRPMixins.MODID, "evolution_bed"));
            MinecraftForge.EVENT_BUS.register(TileEntityEvolutionBed.class);
        }
    }
}
