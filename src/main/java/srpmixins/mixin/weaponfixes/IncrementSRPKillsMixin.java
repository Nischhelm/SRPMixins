package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolArmorBase;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolRangeBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(EntityParasiteBase.class)
public class IncrementSRPKillsMixin extends EntityMob {
    public IncrementSRPKillsMixin(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "onDeath",
            at = @At(value = "TAIL")
    )
    private void incrementSRPKills(DamageSource source, CallbackInfo ci){
        if(this.world.isRemote) return;
        if(SRPMixinsConfigHandler.weapons.disableSentientEvolution) return;
        if(!SRPMixinsConfigHandler.weapons.addArmorBowEvolution) return;
        if(source == null) return;
        if(source.getTrueSource() == null) return;
        if(!(source.getTrueSource() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) source.getTrueSource();

        for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()){
            ItemStack stack = player.getItemStackFromSlot(slot);
            if(stack.isEmpty()) continue;
            if(stack.getItem() instanceof WeaponToolRangeBase || stack.getItem() instanceof WeaponToolArmorBase){
                NBTTagCompound compound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
                compound.setInteger("srpkills", compound.getInteger("srpkills") + (int) this.getMaxHealth());
                stack.setTagCompound(compound);
            }
        }
    }
}
