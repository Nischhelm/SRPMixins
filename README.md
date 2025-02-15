# SRPMixins

Tweaks and Fixes for SRParasites

Originally intended for SRParasites to work better with RLCraft

## Contents

### Dimension Multipliers
- Global Multipliers for Health, Armor, Damage and KB Resistance are set per dimension (default is 1x for overworld, 2x for nether+end, 4x for LC)
- Dimension specific drop chance for parasite parts
- Dimension specific mob cap multipliers

### Phase point tweaks/fixes
- Fixes the phase sometimes randomly resetting to 0 in single player
- Fixes killing parasites being able to reduce phase by underflowing the point limit, after which any action that increases points again will decrease phase
- Fixed Multiplayer Sleeping Penalty being counted per sleeping player
- Bloody Clock also shows the progress to next phase in %
- Each player can have their own phases, making late joins in multiplayer less bad
- As another option, phase mechanics can be applied to chunks instead of whole dimensions, making the infestation grow only where players stay for a while. Custom starting phases can be set per biome.
- Added phase limit from which on Adapted mobs despawning gives a point penalty
- Adds a minimum phase for parasitic biome spreading giving point penalty

### Damage tweaks/fixes
- Fixed Wraith/Bogle/Dreadnaught/Overlord/Haunter dmgs not being properly declared/used
- Disabled Succors having 2x the Atk stat of their thrower, instead adds a fixed configable dmg

### Lure + Carcass tweaks/fixes
- Adds phase specific multipliers to carcass point reduction values
- Carcass needs all lures to be the same type
- Allows carcasses to reduce points during cooldown
- Using lures adds cooldown time to current instead of overwriting it

### Weapon tweaks/fixes
- Fixes living/sentient weapons not working against parasites
- Makes living weapons keep their nbt (enchants etc) when evolving to sentient
- Disables writing the --> xyz (srpkills) tooltip on sentient weapons

### Nexus/Deterrent tweaks/fixes
- Cap Nexus Type spawns to 15 (custom mob cap)
- Added whitelist for Nexus/Deterrent types to not take dmg over time in too low phases
- Plays respective sounds when Dispatchers or Beckons of higher stages naturally spawn
- Blocks Stage 3 Beckons from upgrading to Stage 4 if there is already a Stage 4 Beckon nearby (20 Blocks)
- Fixes Beckons removing the infested blocks around them on Stage upgrade if evolution phases are disabled

### General tweaks/fixes
- Fixed Rage and Heightened Senses applying random UUID attribute modifiers which can accumulate modifiers on repeated restarts
- Fixes potion effects being applied on client side making them never run out but also not do anything
- Fixes SRP spamming debug logs when players touch a scent
- Fixes COTH immune mobs getting converted/feralised anyway
- Tweaks assimilated and feral enderman tp radius for performance
- Nerf Assimilated Endermen in End dimension to stop the utter chaos
  - Added custom mob cap for Endermen turning to Assimilated Endermen in the End Dimension
  - Made Assimilated Endermen always be able to despawn in the End Dimension, even when coming from COTH
- Adds Biome specific blacklist for spawning parasites
- Allows for turning SRP lists to proper wildcardable lists (safer to use but enabling this makes you have to go through all SRP configs and fixing them)

### Tweaks intended for RLCraft
- Lures are disabled in Lost Cities Dimension and turn to Dispatcher Nidus when trying to use them
- LC Portal only enabled from Phase 6, unlock Preeminents and Beckon+Dispatcher spawns everywhere from Phase 6 after entering (config)
- Bloodmoon in LC (increased Parasite Mob Cap + increased Parasite spawning speed)
- Fix Lycanite spawner jsons not picking up SRP mobs

Suggested Config Files for running SRPMixins with RLCraft in RLCraft Parasited.zip

Built upon FermiumTemplateMod by fonnymunkey, needs FermiumBooter
https://github.com/fonnymunkey/FermiumTemplateMod/
