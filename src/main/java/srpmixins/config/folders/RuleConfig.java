package srpmixins.config.folders;

import net.minecraftforge.common.config.Config;

public class RuleConfig {

    @Config.Comment("Requires \"Fix Spawning Entirely\"\n" +
            "Parasite Mob Cap multipliers per phase and dimension (and bloodmoon). \n" +
            "Format: [dim = xxx] [phase><= xxx] [bloodmoon = true/false] mobCapMulti\n" +
            "Where all [] entries are optional and are handled with an AND connection if multiple are present.\n" +
            "For phase the operator options are =, !=, <, >, <= and >= \n" +
            "To define a specific area of phases, you can also use the \"phase\" keyword twice\n" +
            "All rules that fit to the current state will be applied as a multiplicator on the current mobCap\n" +
            "If phases are disabled, rules using phases will be ignored. The same happens if bloodmoon is disabled and a rule includes bloodmoons.")
    @Config.Name("Parasite Mob Cap Rules")
    public String[] mobCapRules = {
            "[dim = 0] [bloodmoon = true] 2",
            "[dim = 111] [bloodmoon = true] 4",
            "[phase < 0] 0",
            "[phase >= 0] [phase <= 10] 1"
    };

    @Config.Comment("Define how many ingame days each phase is allowed to take. Can be varied per dimension.\n" +
            "Setting a minimum means a phase will take at least that amount of ingame days.\n" +
            "Setting a maximum means the phase will automatically increase once the set amount of days has elapsed.\n" +
            "Setting both to the same value will fully ignore the point system and just increase the phase after the set amount of days.\n" +
            "Pattern: [dim = xxx] [phase =<> xxx] [min = xxx] [max = xxx] in any order with all of them optional\n" +
            "With phases being allowed to use operations =, !=, >, <, >=, <=\n" +
            "To set a range of phases, you can also write the phase twice, ie [phase >= 5] [phase <= 7] for phases 5 to 7\n" +
            "Setting no dimension or no phase will make it work for all of them respectively.\n" +
            "If more than one rule should apply, the one with the smallest min / biggest max value will take effect.\n" +
            "NOTE: similar to phase cooldowns this also counts the time that is slept away, not just actually played time.\n" +
            "Another NOTE: If playing with player phases or chunk phases, the mob cap is averaged over all players or chunks. Can't do player/chunk specific mob caps since mc 1.12.2 doesn't have local mob caps.")
    @Config.Name("Min/Max Days per Phase/Dimension")
    public String[] minMaxDaysPerPhase = {};

    @Config.Comment("Add rules to disable parasite variants. You can specify:\n" +
            "\t- group: mob group (INBORN, PRIMITIVE, ADAPTED, PURE, PREEMINENT)\n" +
            "\t- mob: specific mob or spacebar-separated list of mobs\n" +
            "\t- dim: dimension id\n" +
            "\t- phase: minphase and/or maxphase using operations >,<,=,!=,<=,>=\n" +
            "\t- variant: VIRULENT (green), BERSERKER (red), BREACHER (purple/dark) or SPECIAL (see below), can be multiple spacebar-separated ones" +
            "In situations where a rule is applicable, the named variant will be disabled.\n" +
            "Example: To disable all breacher parasites until phase 5 you do \"[phase <= 5] [variant = BREACHER]\"\n" +
            "Available variants:\n" +
            "Parasites that have VIRULENT, BERSERKER and BREACHER variants: ada_arachnida, ada_bolster, ada_longarms, ada_reeker, ada_summoner, pri_arachnida, pri_longarms, pri_reeker, pri_summoner, grunt\n" +
            "Parasites that have a BREACHER variant but no VIRULENT/BERSERKER: ada_manducater, ada_yelloweye, pri_devourer, pri_manducater, pri_yelloweye, all pures except grunt (light_bomber, marauder, monarch, overseer, vigilante, warden)\n" +
            "Rupters and Manglers only have VIRULENT and BERSERKER variants, but no BREACHER.\n" +
            "Primitive Bolsters only have VIRULENT and BREACHER variants, but no BERSERKER\n" +
            "SPECIAL cases: \n" +
            "\t- Variants with changed stats: ada_longarms (tyrant), carrier_colony (armored), haunter (armored), pri_reeker (pale), monarch (deviantive), thrall (unnamed, no skin change)\n" +
            "\t- All three carriers (light_carrier, heavy_carrier, flying_carrier) have empty variant\n" +
            "\t- sim_enderman has crawling variant"
    )
    @Config.Name("Variant Disable Rules")
    public String[] variantDisableRules = {};

    @Config.Comment("Despawn timer rules in ticks. Rules can be defined per phase, dimension, parasite and parasite group.\n" +
            "Format: [dim = xxx] [phase><= xxx] [mob = x y z] [group = xxx] despawnTimer\n" +
            "Where all [] entries are optional and are handled with an AND connection if multiple are present.\n" +
            "For phase the operator options are =, !=, <, >, <= and >= \n" +
            "To define a specific area of phases, you can also use the \"phase\" keyword twice\n" +
            "\"mob\" can list multiple whitespace separated parasite names (without srparasites:), group can only name one\n" +
            "All rules that fit to the current state will be applied and add up to the total despawn timer.\n" +
            "If phases are disabled, rules using phases will be ignored.\n" +
            "These rules will only apply to parasites that would otherwise not despawn (depending on SRP config/default behavior). Parasites that would despawn anyway will not be affected.\n" +
            "Rules will usually apply on parasite creation so a given phase rule will mean the phase the entity got created in.\n" +
            "If the total timer is 0 or lower it won't apply.")
    @Config.Name("Despawn Timer Rules")
    public String[] despawnTimerRules = {
            "[group = ASSIMILATED] 24000",              //20 minutes for assimilated
            "[mob = sim_bigspider] -12000",             //10 minutes for big spider instead
            "[group = CRUDE] 48000",                    //40 minutes for incomplete forms and moving flesh
    };

    @Config.Comment("Same thing as the other rules, just using basically all of the possible conditions and more\n" +
            "Available Conditions: mob (list), group, phase, dim, bloodmoon, variant\n" +
            "(Multiple) Attribute modifiers can be listed per line as [attributename = value @operation]\n" +
            "Where the operation is optional (default: op2 = MULT_TOTAL)\n" +
            "Various commonly used aliases (in upper case) for vanilla stats are available, like ATK for generic.attackDamage. You can also just use the stat name itself.\n" +
            "There are also aliases for the operations 0/1/2, like +, %, x or ADD, MULT_BASE, MULT_TOTAL.\n" +
            "By default only lists the SRP base variant stat multis, which will not be applied if this list is cleared.")
    @Config.Name("Stat Increase Rules")
    public String[] statIncreaseRules = {
            "[mob = ada_longarms] [variant = SPECIAL] [ATK = 2] [HP = 0.5]",
            "[mob = pri_reeker thrall monarch haunter] [variant = SPECIAL] [ATK = 1.5] [HP = 0.5]",
            "[mob = carrier_colony] [variant = SPECIAL] [ARMOR = 1.5] [SPD = 0.25]"
    };
}
