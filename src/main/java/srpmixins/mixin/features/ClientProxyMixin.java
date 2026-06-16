package srpmixins.mixin.features;

import com.dhanantry.scapeandrunparasites.init.SRPSounds;
import com.dhanantry.scapeandrunparasites.proxy.ClientProxy;
import com.google.common.collect.HashMultimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.util.configparse.Pair;

import java.util.*;

@Mixin(ClientProxy.class)
public class ClientProxyMixin {

    @Unique private static final Random SRPMIXINS$RANDOM = new Random();
    @Unique private static final List<SoundEvent> srpmixins$phaseSounds = Arrays.asList(null, SRPSounds.EVPHASE_1, SRPSounds.EVPHASE_2, SRPSounds.EVPHASE_3, SRPSounds.EVPHASE_4, SRPSounds.EVPHASE_5, SRPSounds.EVPHASE_6, SRPSounds.EVPHASE_7, SRPSounds.EVPHASE_8, SRPSounds.EVPHASE_9, SRPSounds.EVPHASE_10);

    @Unique private static final HashMultimap<Integer, Pair<SoundEvent, Float>> srpmixins$soundPitchListByPhase = HashMultimap.create();
    static {
        srpmixins$soundPitchListByPhase.put(1, new Pair<>(SRPSounds.DODSIV, 0F));
        srpmixins$soundPitchListByPhase.put(3, new Pair<>(SRPSounds.COLONY_1, 0F));
        srpmixins$soundPitchListByPhase.put(4, new Pair<>(SRPSounds.DODSIV, 0F));
        srpmixins$soundPitchListByPhase.put(7, new Pair<>(SRPSounds.COLONY_1, 0F));

        for(int i = 1; i <= 10; i++){
            if(i != 4) srpmixins$soundPitchListByPhase.put(i, new Pair<>(srpmixins$phaseSounds.get(i), 0.7F));
            srpmixins$soundPitchListByPhase.put(i, new Pair<>(srpmixins$phaseSounds.get(i), 0F));
        }
    }

    @Inject(
            method = "playMovingSound",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;playSound(Lnet/minecraft/client/audio/ISound;)V"),
            remap = false
    )
    private void srpmixins_playOtherSounds(int soundId, float unused, CallbackInfo ci) {
        if(0 <= soundId && soundId <= 10)
            srpmixins$getPhaseSoundOverride(soundId);
    }

    @Unique
    private void srpmixins$getPhaseSoundOverride(int phase) {
        Set<Pair<SoundEvent, Float>> soundList = srpmixins$soundPitchListByPhase.get(phase);
        if (soundList == null || soundList.isEmpty()) return;

        int pickCount = SRPMIXINS$RANDOM.nextInt(soundList.size() + 1);
        if (pickCount == 0) return;

        List<Pair<SoundEvent, Float>> shuffledList = new ArrayList<>(soundList);
        Collections.shuffle(shuffledList);

        shuffledList.subList(0, pickCount).forEach(p ->
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(p.getLeft(), p.getRight(), 1.0F))
        );
    }
}