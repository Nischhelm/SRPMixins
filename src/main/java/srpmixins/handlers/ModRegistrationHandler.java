package srpmixins.handlers;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.SRPMixins;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.recipe.RemoveAdaptationRecipe;

@Mod.EventBusSubscriber
public class ModRegistrationHandler {
    @SubscribeEvent
    public static void registerRecipeEvent(RegistryEvent.Register<IRecipe> event) {
        if(!SRPMixinsConfigHandler.adaptation.adaptationResetItem.isEmpty())
            event.getRegistry().register(new RemoveAdaptationRecipe().setRegistryName(new ResourceLocation(SRPMixins.MODID, "remove_adaptation")));
    }
}
