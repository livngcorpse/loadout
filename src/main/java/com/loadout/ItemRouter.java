package com.loadout;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

/**
 * Centralized router that handles real-time item routing based on configuration.
 * When an item is picked up, it immediately determines where it should go and moves it.
 */
public class ItemRouter {
    
    /**
     * Routes a newly picked up item to its designated slot based on configuration
     * @param player The player who picked up the item
     * @param pickedItem The item that was just picked up
     * @return true if the item was successfully routed, false otherwise
     */
    public boolean routeItem(ClientPlayerEntity player, ItemStack pickedItem) {
        if (pickedItem.isEmpty()) {
            return false; // Nothing to route
        }
        
        // Check if the player is in a safe state to route items
        if (!isSafeToRoute(player)) {
            return false;
        }
        
        // Find the target slot for this item
        int targetSlot = findTargetSlot(pickedItem, player.getInventory());
        
        if (targetSlot == -1) {
            // No configured slot for this item, leave it in inventory
            return false;
        }
        
        // Check if target slot is occupied and if replacement is allowed
        if (!isReplacementAllowed(player.getInventory(), targetSlot, pickedItem)) {
            // Cannot replace the current item in target slot
            return false;
        }
        
        // Find where the picked item currently is in inventory
        int sourceSlot = findItemSlot(player.getInventory(), pickedItem);
        
        if (sourceSlot == -1 || sourceSlot == targetSlot) {
            // Item is already in target position or not found
            return false;
        }
        
        // Perform the move operation
        return ItemSwapper.moveItem(player, sourceSlot, targetSlot);
    }
    
    /**
     * Determines if it's safe to route items for this player
     * @param player The player to check
     * @return true if safe to route, false otherwise
     */
    private boolean isSafeToRoute(ClientPlayerEntity player) {
        // Check if player is dead or in spectator mode
        if (player.isDead() || player.isSpectator()) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Finds the target slot where this item should go based on configuration
     * @param item The item to route
     * @param inventory The player's inventory
     * @return The target slot index, or -1 if no slot is configured for this item
     */
    private int findTargetSlot(ItemStack item, PlayerInventory inventory) {
        // Get the current configuration from LoadoutConfig
        LoadoutConfig config = LoadoutClient.getConfig();
        SlotRoutingConfig[] slotConfigs = config.getSlotRoutingConfigs();
        
        // Look for a slot configured to accept this item
        for (int slotIndex = 0; slotIndex < slotConfigs.length; slotIndex++) {
            SlotRoutingConfig slotConfig = slotConfigs[slotIndex];
            
            if (slotConfig == null || slotConfig.isLocked()) {
                continue; // Skip locked or null configs
            }
            
            if (ItemMatcher.matchesCategory(item, slotConfig.getItemCategory())) {
                return convertLogicalSlotToActualSlot(slotIndex, inventory);
            }
        }
        
        return -1; // No target slot found
    }
    
    /**
     * Checks if replacement is allowed in the target slot
     * @param inventory The player's inventory
     * @param targetSlot The slot to check
     * @param newItem The new item to place
     * @return true if replacement is allowed, false otherwise
     */
    private boolean isReplacementAllowed(PlayerInventory inventory, int targetSlot, ItemStack newItem) {
        // Get the current configuration from LoadoutConfig
        LoadoutConfig config = LoadoutClient.getConfig();
        SlotRoutingConfig[] slotConfigs = config.getSlotRoutingConfigs();
        
        // Find the config for this slot
        int logicalSlotIndex = convertActualSlotToLogicalSlot(targetSlot, inventory);
        if (logicalSlotIndex >= 0 && logicalSlotIndex < slotConfigs.length) {
            SlotRoutingConfig slotConfig = slotConfigs[logicalSlotIndex];
            if (slotConfig != null) {
                ItemStack currentStack = getStackFromSlot(inventory, targetSlot);
                
                if (currentStack.isEmpty()) {
                    // Target slot is empty, always OK to place
                    return true;
                }
                
                switch (slotConfig.getReplacementMode()) {
                    case NEVER:
                        return false;
                    case SAME_TYPE_ONLY:
                        // Only replace if the new item is the same type as the current one
                        return ItemMatcher.isSameItemType(currentStack, newItem);
                    case ALWAYS:
                        // Always replace regardless of item type
                        return true;
                }
            }
        }
        
        return false; // Default to not allowing replacement
    }
    
    /**
     * Finds the slot containing a specific item stack
     * @param inventory The player's inventory
     * @param targetItem The item stack to find
     * @return The slot index, or -1 if not found
     */
    private int findItemSlot(PlayerInventory inventory, ItemStack targetItem) {
        // Check main inventory (hotbar + main)
        for (int i = 0; i < inventory.main.size(); i++) {
            ItemStack stack = inventory.main.get(i);
            if (ItemStack.areEqual(stack, targetItem)) {
                return i;
            }
        }
        
        // Check offhand
        ItemStack offhandStack = inventory.offHand.get(0);
        if (ItemStack.areEqual(offhandStack, targetItem)) {
            return PlayerInventory.OFF_HAND_SLOT;
        }
        
        // Check armor slots
        for (int i = 0; i < inventory.armor.size(); i++) {
            ItemStack armorStack = inventory.armor.get(i);
            if (ItemStack.areEqual(armorStack, targetItem)) {
                // Armor slots in screen handler are indexed after main inventory and hotbar
                int hotbarSize = 9;
                int mainInventorySize = inventory.main.size() - hotbarSize;
                return hotbarSize + mainInventorySize + i;
            }
        }
        
        return -1; // Not found
    }
    
    /**
     * Converts a logical slot index (0-13) to actual slot index in inventory
     * Logical order: Hotbar 0-8, Armor 9-12, Offhand 13
     * Minecraft screen handler slot layout:
     * - 0-8: Hotbar (bottom row)
     * - 9-35: Main inventory (3x9 grid, bottom to top, after hotbar)
     * - 36-39: Armor slots (36=boots, 37=leggings, 38=chestplate, 39=helmet)
     * - 40: Offhand
     * @param logicalIndex The logical slot index
     * @param inventory The player's inventory
     * @return The actual slot index in the inventory
     */
    private int convertLogicalSlotToActualSlot(int logicalIndex, PlayerInventory inventory) {
        if (logicalIndex >= 0 && logicalIndex < 9) {
            // Hotbar slots 0-8 map directly to screen handler indices 0-8
            return logicalIndex;
        } else if (logicalIndex >= 9 && logicalIndex <= 12) {
            // Armor slots (logical 9-12) map to screen handler indices 36-39
            // Note: Armor slots are in reverse order: boots(36), leggings(37), chestplate(38), helmet(39)
            // So logical 9(helmet) -> 39, 10(chestplate) -> 38, 11(leggings) -> 37, 12(boots) -> 36
            int armorIndex = logicalIndex - 9; // 0=helmet, 1=chestplate, 2=leggings, 3=boots
            return 39 - armorIndex;
        } else if (logicalIndex == 13) {
            // Offhand slot (logical 13) maps to screen handler index 40
            return 40; // OFF_HAND_SLOT is 40 in Minecraft
        }
        
        return -1; // Invalid logical index
    }
    
    /**
     * Converts an actual slot index to logical slot index
     * @param actualIndex The actual slot index in inventory
     * @param inventory The player's inventory
     * @return The logical slot index, or -1 if invalid
     */
    private int convertActualSlotToLogicalSlot(int actualIndex, PlayerInventory inventory) {
        if (actualIndex >= 0 && actualIndex < 9) {
            // Hotbar slots 0-8
            return actualIndex;
        } else if (actualIndex >= 36 && actualIndex <= 39) {
            // Armor slots 36-39 map to logical 9-12
            // Note: Armor slots are in reverse order: boots(36), leggings(37), chestplate(38), helmet(39)
            // So screen 39(helmet) -> logical 9, 38(chestplate) -> logical 10, 37(leggings) -> logical 11, 36(boots) -> logical 12
            return 9 + (39 - actualIndex);
        } else if (actualIndex == 40) { // OFF_HAND_SLOT
            // Offhand slot 40 maps to logical 13
            return 13;
        }
        
        return -1; // Invalid or unsupported slot
    }
    
    /**
     * Gets the item stack from a specific slot
     * @param inventory The player's inventory
     * @param slotIndex The slot index
     * @return The item stack in that slot
     */
    private ItemStack getStackFromSlot(PlayerInventory inventory, int slotIndex) {
        if (slotIndex >= 0 && slotIndex < 9) {
            // Hotbar slots 0-8
            return inventory.getStack(slotIndex);
        } else if (slotIndex >= 9 && slotIndex <= 35) {
            // Main inventory slots 9-35 (3x9 grid, bottom to top)
            int mainIndex = slotIndex - 9;
            return inventory.main.get(mainIndex);
        } else if (slotIndex >= 36 && slotIndex <= 39) {
            // Armor slots 36-39 (boots, leggings, chestplate, helmet)
            int armorIndex = 39 - slotIndex; // Reverse the order: 39->0(helmet), 38->1(chestplate), 37->2(leggings), 36->3(boots)
            return inventory.armor.get(armorIndex);
        } else if (slotIndex == 40) { // OFF_HAND_SLOT
            // Offhand slot 40
            return inventory.offHand.get(0);
        }
        
        return ItemStack.EMPTY; // Invalid slot
    }
}