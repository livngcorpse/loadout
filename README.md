# Loadout Mod

Smart real-time item routing system with fully customizable rules for Minecraft Fabric 1.20.10+.

## Features

- Real-time item routing: Items are immediately directed to their designated slots when picked up
- Item category-based assignment (Weapons, Tools, Armor, Food, etc.)
- Replacement mode controls (Never, Same Type Only, Always)
- Fully configurable through Cloth Config GUI
- Client-side only - no server-side requirements
- Sensible default configurations for new users
- Safe implementation - never deletes or duplicates items
- Slot locking to disable automatic management

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
  - Pickup Only: Only when picking up items (recommended for real-time routing)
  
### Slot Configuration

Each hotbar slot, armor slot, and the offhand can be configured individually with:

- **Item Category**: The type of items to route to this slot (Weapon, Tool, Armor, Food, etc.)
- **Replacement Mode**: When to replace items already in the slot (Never, Same Type Only, Always)
- **Locked**: Disables automatic management for this slot

### Slot Configuration

Each hotbar slot, armor slot, and the offhand can be configured individually with:

- **Allowed Items**: List of item IDs that are allowed in this slot (empty for any item)
- **Material Priority**: Preferred material type (Wood, Stone, Iron, Gold, Diamond, Netherite)
- **Durability Preference**: Whether to prefer items with high or low durability
- **Consider Enchantments**: Whether to factor enchantments into item selection
- **Enforce Single Item**: Prevents multiple items of the same type in the slot
- **Locked**: Disables automatic management for this slot

## Usage

Once configured, the mod will immediately route items to their designated slots when:

- You pick up items (real-time routing)
- You manually trigger it with the keybind (for existing items)

## Safety Features

- Will not route items if player is dead or in spectator mode
- Minimal safety checks to ensure routing doesn't interfere with gameplay
- Never destroys or duplicates items
- Respects player state (dead, spectator, creative modes)

## Dependencies

- Fabric API
- Cloth Config API
- Mod Menu (optional)

## Compatibility

This mod is compatible with Minecraft 1.20.10 and above, and requires Fabric Loader.