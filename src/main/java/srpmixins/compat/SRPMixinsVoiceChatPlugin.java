package srpmixins.compat;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.opus.OpusDecoder;
import de.maxhenkel.voicechat.voice.common.AudioUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import srpmixins.config.SRPMixinsConfigHandler;
import srpmixins.registry.ModPotions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ForgeVoicechatPlugin
@SuppressWarnings("unused")
public class SRPMixinsVoiceChatPlugin implements VoicechatPlugin {

    @Override
    public String getPluginId() {
        return "srpmixins_vc";
    }

    @Override
    public void initialize(VoicechatApi api) {
        VoicechatPlugin.super.initialize(api);
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        if(SRPMixinsConfigHandler.modcompat.enableVoiceChatCompat)
            registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophonePacket);
    }

    private static OpusDecoder decoder = null;
    private static final Map<UUID, Double> meanVolumes = new HashMap<>();

    public void onMicrophonePacket(MicrophonePacketEvent event) {
        if(event.getSenderConnection() == null) return;
        EntityPlayer player = (EntityPlayer) event.getSenderConnection().getPlayer().getPlayer();

        if(decoder == null) decoder = event.getVoicechat().createDecoder();
        short[] decoded = decoder.decode(event.getPacket().getOpusEncodedData());
        double maxLvl = AudioUtils.getHighestAudioLevel(decoded);

        double oldVol =  meanVolumes.getOrDefault(player.getUniqueID(), 0.);
        double newVol = 0.9 * oldVol + 0.1 * maxLvl; //rolling avg
        meanVolumes.put(player.getUniqueID(), newVol);

        if(newVol < SRPMixinsConfigHandler.modcompat.voiceChatThreshold) return;
        if(player.world.getTotalWorldTime() % 20 != 0) return;

        for(EntityParasiteBase entity : player.world.getEntitiesWithinAABB(EntityParasiteBase.class, player.getEntityBoundingBox().grow(SRPMixinsConfigHandler.modcompat.voiceChatThreshold))) {
            entity.setAttackTarget(player);
            entity.addPotionEffect(new PotionEffect(ModPotions.hunting, 100, 0, false, false));
        }

        // TODO: notify player somehow less intrusively if they choose to fight
        if(SRPMixinsConfigHandler.modcompat.voiceChatNotify)
            player.sendStatusMessage(new TextComponentString("They can hear you").setStyle(new Style().setColor(TextFormatting.GOLD)), true);
    }
}
