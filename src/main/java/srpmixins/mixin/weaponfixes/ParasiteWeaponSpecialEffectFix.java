package srpmixins.mixin.weaponfixes;

import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.item.tool.WeaponToolMeleeBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import srpmixins.handlers.SRPMixinsConfigHandler;

@Mixin(EntityParasiteBase.class)
public abstract class ParasiteWeaponSpecialEffectFix extends EntityMob {

    public ParasiteWeaponSpecialEffectFix(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "attackEntityFrom",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void fixParasiteWeaponDmg(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!SRPMixinsConfigHandler.weapons.fixParasiteDmg) return;
        if (this.world.isRemote) return;

        if (source.getTrueSource() == null) return;
        if (!(source.getTrueSource() instanceof EntityPlayer)) return;
        ItemStack mainhand = ((EntityPlayer) source.getTrueSource()).getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        if (mainhand.getItem() instanceof WeaponToolMeleeBase) {
            //SRP Code was missing a "return", that's the whole bug
            cir.setReturnValue(super.attackEntityFrom(source, amount));
        }
    }
}