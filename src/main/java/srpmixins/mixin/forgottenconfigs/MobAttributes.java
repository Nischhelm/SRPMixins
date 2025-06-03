package srpmixins.mixin.forgottenconfigs;

import com.dhanantry.scapeandrunparasites.util.SRPAttributes;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigMobs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SRPAttributes.class)
public class MobAttributes {
    @Shadow(remap = false) public static double ATA_HEALTH;
    @Shadow(remap = false) public static double ATA_ARMOR;
    @Shadow(remap = false) public static double ATA_ATTACK_DAMAGE;
    @Shadow(remap = false) public static double ATA_KD_RESISTANCE;

    @Shadow(remap = false) public static double GOTHOL_HEALTH;
    @Shadow(remap = false) public static double GOTHOL_ARMOR;
    @Shadow(remap = false) public static double GOTHOL_DAMAGE;
    @Shadow(remap = false) public static double GOTHOL_KD_RESISTANCE;

    @Shadow(remap = false) public static double HIGOLEM_HEALTH;
    @Shadow(remap = false) public static double HIGOLEM_ARMOR;
    @Shadow(remap = false) public static double HIGOLEM_ATTACK_DAMAGE;
    @Shadow(remap = false) public static double HIGOLEM_KD_RESISTANCE;

    @Shadow(remap = false) public static double KOL_HEALTH;
    @Shadow(remap = false) public static double KOL_ARMOR;
    @Shadow(remap = false) public static double KOL_ATTACK_DAMAGE;
    @Shadow(remap = false) public static double KOL_KD_RESISTANCE;

    @Inject(method = "init", at = @At(value = "TAIL"), remap = false)
    private static void srpmixins_useMobAttributes(CallbackInfo ci){
        ATA_HEALTH *= (SRPConfig.globalHealthMultiplier * SRPConfigMobs.ataHealthMultiplier);
        ATA_ARMOR *= (SRPConfig.globalArmorMultiplier * SRPConfigMobs.ataArmorMultiplier);
        ATA_ATTACK_DAMAGE *= (SRPConfig.globalDamageMultiplier * SRPConfigMobs.ataDamageMultiplier);
        ATA_KD_RESISTANCE *= (SRPConfig.globalKDResistanceMultiplier * SRPConfigMobs.ataKDResistanceMultiplier);

        GOTHOL_HEALTH *= (SRPConfig.globalHealthMultiplier * SRPConfigMobs.gotholHealthMultiplier);
        GOTHOL_ARMOR *= (SRPConfig.globalArmorMultiplier * SRPConfigMobs.gotholArmorMultiplier);
        GOTHOL_DAMAGE *= (SRPConfig.globalDamageMultiplier * SRPConfigMobs.gotholDamageMultiplier);
        GOTHOL_KD_RESISTANCE *= (SRPConfig.globalKDResistanceMultiplier * SRPConfigMobs.gotholKDResistanceMultiplier);

        HIGOLEM_HEALTH *= (SRPConfig.globalHealthMultiplier * SRPConfigMobs.higolemHealthMultiplier);
        HIGOLEM_ARMOR *= (SRPConfig.globalArmorMultiplier * SRPConfigMobs.higolemArmorMultiplier);
        HIGOLEM_ATTACK_DAMAGE *= (SRPConfig.globalDamageMultiplier * SRPConfigMobs.higolemDamageMultiplier);
        HIGOLEM_KD_RESISTANCE *= (SRPConfig.globalKDResistanceMultiplier * SRPConfigMobs.higolemKDResistanceMultiplier);

        KOL_HEALTH *= (SRPConfig.globalHealthMultiplier * SRPConfigMobs.kolHealthMultiplier);
        KOL_ARMOR *= (SRPConfig.globalArmorMultiplier * SRPConfigMobs.kolArmorMultiplier);
        KOL_ATTACK_DAMAGE *= (SRPConfig.globalDamageMultiplier * SRPConfigMobs.kolDamageMultiplier);
        KOL_KD_RESISTANCE *= (SRPConfig.globalKDResistanceMultiplier * SRPConfigMobs.kolKDResistanceMultiplier);

    }
}
