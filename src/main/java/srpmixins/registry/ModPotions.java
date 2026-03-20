package srpmixins.registry;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.potion.PotionHunting;

import java.util.UUID;

@Mod.EventBusSubscriber
public class ModPotions {
    public static Potion hunting = new PotionHunting().registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, UUID.nameUUIDFromBytes("srpmixins_hunting".getBytes()).toString(), SRPMixinsConfigHandler.modcompat.voiceChatMaxDistance, 0);

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        if(SRPMixinsConfigHandler.modcompat.enableVoiceChatCompat) event.getRegistry().register(hunting);
    }
}
