package srpmixins.handlers;

import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;

public class CamouflageHandler {
    public static final int NOCHECKYET = 0;
    public static final int FAILEDTOPROTECT = 1;
    public static final int ISPROTECTING = 2;

    @SubscribeEvent
    public static void onPotionApplicableCamouflage(PotionEvent.PotionApplicableEvent event){
        if(event.getEntityLiving().world.isRemote) return;
        if(event.getPotionEffect().getPotion() != SRPPotions.EPEL_E) return;

        //If Camouflage is applied and mob already has COTH, cancel
        if(event.getEntityLiving().getActivePotionEffect(SRPPotions.COTH_E) != null) {
            event.setResult(Event.Result.DENY);
            return;
        }

        //Else store info that camouflage is trying to protect against COTH
        addProtectionState(event.getPotionEffect());
    }

    @SubscribeEvent
    public static void onPotionApplicableCOTH(PotionEvent.PotionApplicableEvent event){
        if(event.getEntityLiving().world.isRemote) return;
        if(event.getPotionEffect().getPotion() != SRPPotions.COTH_E) return;
        if(SRPConfigSystems.epelChanceCOTH == 1) return; //no need to do special handling

        //If COTH is applied and mob doesn't have camouflage, we don't care
        PotionEffect camoEffect = event.getEntityLiving().getActivePotionEffect(SRPPotions.EPEL_E);
        if(camoEffect == null) return;

        //Else do the random check for the full time
        ItemStack camoStack = getProtectionStack(camoEffect);
        if(camoStack == null) return;

        int stackCount = camoStack.getMetadata();
        if(stackCount != NOCHECKYET) return;
        if(event.getEntityLiving().getRNG().nextFloat() < SRPConfigSystems.epelChanceCOTH)
            camoStack.setCount(ISPROTECTING);
        else
            camoStack.setCount(FAILEDTOPROTECT);
    }

    public static void addProtectionState(PotionEffect camoEffect){
        //Using Curative Items instead of custom NBT because this is the only vanilla data container for PotionEffects
        List<ItemStack> curativeItems = camoEffect.getCurativeItems();
        //item that no one would ever add a rightclick action to, i hope
        curativeItems.add(new ItemStack(Items.CHORUS_FRUIT_POPPED, 1, NOCHECKYET));
        camoEffect.setCurativeItems(curativeItems);
    }

    @Nullable
    //Jank should be encapsulated
    private static ItemStack getProtectionStack(PotionEffect camoEffect){
        List<ItemStack> curativeItems = camoEffect.getCurativeItems();
        ItemStack chorusStack = null;
        for(ItemStack curativeItem : curativeItems)
            if(curativeItem.getItem() == Items.CHORUS_FRUIT_POPPED) {
                chorusStack = curativeItem;
                break;
            }
        return chorusStack;
    }

    public static int getProtectionState(PotionEffect camoEffect){
        ItemStack stack = getProtectionStack(camoEffect);
        return stack == null ? -1 : stack.getMetadata();
    }

    public static double getChanceFromState(double originalChance, EntityLivingBase target) {
        PotionEffect camoEffect = target.getActivePotionEffect(SRPPotions.EPEL_E);
        int protectionState = getProtectionState(camoEffect);
        if(protectionState == FAILEDTOPROTECT) return 0;
        if(protectionState == ISPROTECTING) return 1;

        return originalChance;
    }
}
