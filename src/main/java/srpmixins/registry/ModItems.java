package srpmixins.registry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import srpmixins.SRPMixins;
import srpmixins.client.renderer.tileentity.TESREvolutionBed;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.item.ItemEvolutionBed;
import srpmixins.tileentity.TileEntityEvolutionBed;

@Mod.EventBusSubscriber(modid = SRPMixins.MODID)
public class ModItems {

    @GameRegistry.ObjectHolder(SRPMixins.MODID + ":evolution_bed")
    public static final ItemEvolutionBed evolutionBed = (ItemEvolutionBed) new ItemEvolutionBed().setMaxStackSize(1).setRegistryName(SRPMixins.MODID, "evolution_bed").setTranslationKey(SRPMixins.MODID + ".evolution_bed");

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        if(SRPMixinsConfigHandler.lures.registerEvolutionBeds) event.getRegistry().register(evolutionBed);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModelEvent(ModelRegistryEvent event) {
        if(SRPMixinsConfigHandler.lures.registerEvolutionBeds) {
            evolutionBed.setTileEntityItemStackRenderer(new TESREvolutionBed.TEISREvolutionBed());
            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEvolutionBed.class, new TESREvolutionBed());

            int phases = 10; // Anything beyond 10 won't have textures, however they will have a model
            if(SRPMixinsConfigHandler.morephases.enableMorePhases) {
                phases = SRPMixinsConfigHandler.morephases.maxEvolutionPhase;
            }
            for (int i = 0; i < phases; ++i) {
                ModelLoader.setCustomModelResourceLocation(evolutionBed, i, new ModelResourceLocation(evolutionBed.getRegistryName(), "inventory"));
            }
        }
    }
}
