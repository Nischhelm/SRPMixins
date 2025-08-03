package srpmixins.mixin.spawning.summoningoverhaul;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityCanSummon;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.util.ISummonsByUUID;

import java.util.Map;
import java.util.UUID;

@Mixin(EntityParasiteBase.class)
public abstract class ReadWriteSummons {
    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    private void srpmixins_writeSummonsToNBT(NBTTagCompound compound, CallbackInfo ci){
        if(!(this instanceof EntityCanSummon)) return;

        Map<UUID, Integer> summonMap = ((ISummonsByUUID) this).srpmixins$getSummonEntries();
        if(summonMap.isEmpty()) return;

        NBTTagList nbtList = new NBTTagList();
        for(Map.Entry<UUID, Integer> entry : summonMap.entrySet()){
            NBTTagCompound tags = new NBTTagCompound();
            tags.setString("uuid",entry.getKey().toString());
            tags.setInteger("points",entry.getValue());
            nbtList.appendTag(tags);
        }
        compound.setTag("srpmixins_summons", nbtList);
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    private void srpmixins_readSummonsToNBT(NBTTagCompound compound, CallbackInfo ci){
        if(!(this instanceof EntityCanSummon)) return;
        if(!compound.hasKey("srpmixins_summons")) return;

        NBTTagList nbtList = compound.getTagList("srpmixins_summons", 10);
        for(NBTBase tag : nbtList){
            UUID uuid = UUID.fromString(((NBTTagCompound) tag).getString("uuid"));
            int points = ((NBTTagCompound) tag).getInteger("points");
            ((ISummonsByUUID) this).srpmixins$addSummon(uuid, points);
        }
    }
}
