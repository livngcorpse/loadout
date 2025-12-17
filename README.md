# Loadout Mod

Smart hotbar, armor, and offhand management with fully customizable rules for Minecraft Fabric 1.20.10+.

## Features

- Automatically organizes your 9-slot hotbar based on customizable rules
- Equips armor automatically based on user-defined preferences
- Sets offhand items according to your configuration
- Fully configurable through Cloth Config GUI
- Client-side only - no server-side requirements
- No default behavior - everything is opt-in
- Safe implementation - never deletes or duplicates items

## Installation

1. Download and install Fabric Loader for Minecraft 1.20.10+
2. Download and install Fabric API
3. Download and install Cloth Config API
4. Download and install Mod Menu (optional but recommended)
5. Place the Loadout mod JAR file in your `.minecraft/mods` folder

## Building from Source

To build the mod from source:

1. Clone this repository
2. Run `./gradlew build` in the project directory
3. The built JAR will be located in `build/libs/`

## Configuration

Access the configuration screen through Mod Menu or by pressing the configuration keybind.

### General Settings

- **Activation Mode**: Controls when the loadout is automatically organized
  - Manual Only: Only when manually triggered
  - Respawn Only: Only after respawning
  - Pickup Only: Only when picking up items
  - All Events: On respawn, item pickup, and manual activation
  
- **Enable Hotbar Management**: Enable automatic organization of the hotbar
- **Enable Armor Management**: Enable automatic equipping of armor
- **Enable Offhand Management**: Enable automatic setting of offhand items
- **Respawn Delay (Ticks)**: Delay before organizing loadout after respawning
- **Cooldown (Ticks)**: Minimum time between loadout organizations

### Slot Configuration

Each hotbar slot, armor slot, and the offhand can be configured individually with:

- **Material Priority**: Preferred material type (Wood, Stone, Iron, Gold, Diamond, Netherite)
- **Durability Preference**: Whether to prefer items with high or low durability
- **Consider Enchantments**: Whether to factor enchantments into item selection
- **Enforce Single Item**: Prevents multiple items of the same type in the slot
- **Locked**: Disables automatic management for this slot

## Usage

Once configured, the mod will automatically organize your loadout based on your rules when:

- You respawn (if enabled)
- You pick up items (if enabled)
- You manually trigger it with the keybind

## Safety Features

- Will not swap items during combat or while using items
- Implements cooldowns to prevent rapid reorganization
- Delays execution briefly after respawning
- Never destroys or duplicates items

## Dependencies

- Fabric API
- Cloth Config API
- Mod Menu (optional)

## Compatibility

This mod is compatible with Minecraft 1.20.10 and above, and requires Fabric Loader.