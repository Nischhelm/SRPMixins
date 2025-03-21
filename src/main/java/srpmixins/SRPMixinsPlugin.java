package srpmixins;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import srpmixins.config.EarlyConfigReader;
import srpmixins.config.SRPMixinsConfigHandler;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class SRPMixinsPlugin implements IFMLLoadingPlugin {

	public SRPMixinsPlugin() {
		MixinBootstrap.init();

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srparasites.json", true);

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.adaptationbonusfix.json", () -> EarlyConfigReader.getBoolean("Fix Adaptation Bonus Config", SRPMixinsConfigHandler.adaptation.fixAdaptationBonusList));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.adaptationoverhaul.json", () -> EarlyConfigReader.getBoolean("Overhaul Adaptation", SRPMixinsConfigHandler.adaptation.overhaulAdaptation));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.adaptedinstadespawnpenalty.json", () -> EarlyConfigReader.getBoolean("Fix Adapted Penalty on Instant Despawn", SRPMixinsConfigHandler.phasepoints.fixAdaptedPenaltyInstantDespawn));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.adapteddespawnlock.json", () -> EarlyConfigReader.getInt("Adapted Despawn Penalty First Phase", SRPMixinsConfigHandler.phasepoints.adaptedDespawnPenaltyPhase) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.armorbowevo.json", () -> EarlyConfigReader.getBoolean("Sentient Armor+Bow Evolution", SRPMixinsConfigHandler.weapons.addArmorBowEvolution));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.beckoninfestationfix.json", () -> EarlyConfigReader.getBoolean("Fix Infested Block Reversion", SRPMixinsConfigHandler.deterrents.fixInfestedBlockReversion));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.beckoninfestationlimit.json", () -> EarlyConfigReader.getBoolean("Fix Block Infestation Limit", SRPMixinsConfigHandler.deterrents.fixInfestedBlockLimit));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.beckonupgradedeny.json", () -> EarlyConfigReader.getBoolean("Limit Stage 4 Beckons", SRPMixinsConfigHandler.deterrents.limitStage4Beckons));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.biomespreadlimit.json", () -> EarlyConfigReader.getBoolean("Fix Parasitic Biome spreading limit", SRPMixinsConfigHandler.various.fixBiomeSpreadingLimit));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.biomespreadpenalty.json", () -> EarlyConfigReader.getInt("Biome Spreading Penalty First Phase", SRPMixinsConfigHandler.phasepoints.biomeSpreadingPenaltyPhase) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.carcasssamelures.json", () -> EarlyConfigReader.getBoolean("Force carcass all same lure type", SRPMixinsConfigHandler.lures.forceCarcassSameLureVariant));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.clientpotions.json", () -> EarlyConfigReader.getBoolean("Fix clientside potions", SRPMixinsConfigHandler.various.fixClientPotions));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.configlistfix.json", () -> EarlyConfigReader.getBoolean("Fix Config List Parsing", SRPMixinsConfigHandler.various.fixConfigListParse));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.cooldownbypass.json", () -> EarlyConfigReader.getBoolean("Fix Carcasses not working during cooldown", SRPMixinsConfigHandler.lures.fixCarcassDuringCooldown));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.cothimmunityfix.json", () -> EarlyConfigReader.getBoolean("Fix srpcothimmunity tag", SRPMixinsConfigHandler.coth.fixSrpCothImmunity));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.damagefix.json", () -> EarlyConfigReader.getBoolean("Damage Fix: Global switch", SRPMixinsConfigHandler.dmgfix.doDamageFixes));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.defaultgainlossfix.json", () -> EarlyConfigReader.getBoolean("Fix default canGain/Lose", SRPMixinsConfigHandler.phasepoints.fixDefaultGainLoss));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.dimensionmultis.json", () -> EarlyConfigReader.getBoolean("Parasite Stat+Drop Multiplier: Global switch", SRPMixinsConfigHandler.dimension.doMultipliers));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.disablescentdebug.json", () -> EarlyConfigReader.getBoolean("Disable Scent Debug", SRPMixinsConfigHandler.various.disableScentDebug));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.flatsleeppenalty.json", () -> EarlyConfigReader.getBoolean("Flat sleep point penalty", SRPMixinsConfigHandler.phasepoints.flatSleepPenalty));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.lurecooldownstack.json", () -> EarlyConfigReader.getBoolean("Lures stack cooldown", SRPMixinsConfigHandler.lures.lureCooldownStacking));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.lureoverflowfix.json", () -> EarlyConfigReader.getBoolean("Fix Cooldown Overflow", SRPMixinsConfigHandler.lures.fixCooldownOverflow));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.modifiedclock.json", () -> EarlyConfigReader.getBoolean("Bloody Clock percentage", SRPMixinsConfigHandler.phasepoints.modifyBloodyClock));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.needlerfix.json", () -> EarlyConfigReader.getBoolean("Needler Fix", SRPMixinsConfigHandler.various.fixNeedler));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.nexuscap.json", () -> EarlyConfigReader.getInt("Nexus Mob Cap", SRPMixinsConfigHandler.deterrents.nexusCap) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.noderesetfix.json", () -> EarlyConfigReader.getBoolean("Fix Node Resets", SRPMixinsConfigHandler.phasepoints.fixNodeResets));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.phaseresetfix.json", () -> EarlyConfigReader.getBoolean("Fix Phase Resets", SRPMixinsConfigHandler.phasepoints.fixPhaseResets));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.pointreductionlimit.json", () -> EarlyConfigReader.getBoolean("Fix phase point reduction", SRPMixinsConfigHandler.phasepoints.limitPointReduction));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.potionuuidfix.json", () -> EarlyConfigReader.getBoolean("Fix attribute potions", SRPMixinsConfigHandler.various.fixPotionUUIDs));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.sentevodisable.json", () -> EarlyConfigReader.getBoolean("Disable Sentient Evolution Mechanic", SRPMixinsConfigHandler.weapons.disableSentientEvolution));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.senttooltipremove.json", () -> EarlyConfigReader.getBoolean("Remove Parasite Kills tooltip from sentient weapons", SRPMixinsConfigHandler.weapons.removeSentientSRPKillsTooltip));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.sentnbtlossfix.json", () -> EarlyConfigReader.getBoolean("Fix parasite weapon evolution NBT loss", SRPMixinsConfigHandler.weapons.fixSentientEvolutionNBT));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.simmermancap.json", () -> EarlyConfigReader.getInt("End Simmermen Conversion Cap", SRPMixinsConfigHandler.simmermen.endSimmermenCap) > -1);
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.simmermandespawn.json", () -> EarlyConfigReader.getBoolean("End Simmermen despawn", SRPMixinsConfigHandler.simmermen.despawnEndSimmermen));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.variablecarcasses.json", () -> EarlyConfigReader.getBoolean("Phase dependent Carcass Values", SRPMixinsConfigHandler.lures.variableCarcassValues));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.weapondamage.json", () -> EarlyConfigReader.getBoolean("Fix parasite weapon damage", SRPMixinsConfigHandler.weapons.fixParasiteDmg));

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.srp.simmermantp.json", true);

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.customphasemechanics.json", () -> EarlyConfigReader.getBoolean("Use Player Phases", SRPMixinsConfigHandler.playerphases.enabled) || EarlyConfigReader.getBoolean("Use Chunk Phases", SRPMixinsConfigHandler.chunkphases.enabled));

		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.lostcities.json", () -> EarlyConfigReader.getBoolean("Compat: Modpack has LostCities mod", SRPMixinsConfigHandler.modcompat.hasLostCities));
		FermiumRegistryAPI.enqueueMixin(true, "mixins.srpmixins.bloodmoon.json", () -> EarlyConfigReader.getBoolean("Compat: Modpack has LostCities mod", SRPMixinsConfigHandler.modcompat.hasLostCities) && EarlyConfigReader.getBoolean("Compat: Modpack has Bloodmoon mod", SRPMixinsConfigHandler.modcompat.hasBloodmoon));
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}
	
	@Override
	public String getModContainerClass()
	{
		return null;
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) { }
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}