package srpmixins.compat;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.opus.OpusDecoder;
import de.maxhenkel.voicechat.voice.common.AudioUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;

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
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophonePacket);
    }

    private static OpusDecoder decoder = null;

    //TODO: config
    public static final int range = 50;
    public static final double threshold = -10;

    public void onMicrophonePacket(MicrophonePacketEvent event) {
        if(event.getSenderConnection() == null) return;
        EntityPlayer player = (EntityPlayer) event.getSenderConnection().getPlayer().getPlayer();
        if(player.world.getTotalWorldTime() % 20 != 0) return;

        if(decoder == null) decoder = event.getVoicechat().createDecoder();
        short[] decoded = decoder.decode(event.getPacket().getOpusEncodedData());
        double maxLvl = AudioUtils.getHighestAudioLevel(decoded);

        if(maxLvl < threshold) return;

        for(EntityParasiteBase entity : player.world.getEntitiesWithinAABB(EntityParasiteBase.class, player.getEntityBoundingBox().grow(range))) {
            entity.setAttackTarget(player);
            double base = entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue();
            if(base == 0) continue;
            int neededLvl = MathHelper.clamp(MathHelper.ceil((50 / base - 1) * 10), 0, 100);
            entity.addPotionEffect(new PotionEffect(SRPPotions.SENS_E, 100, neededLvl));
        }
        // TODO: notify player somehow
    }
}
