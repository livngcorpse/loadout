package com.loadout;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
// PlayerInventory is accessed through player.getInventory()
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class HotbarController {
    private final SlotProfile[] hotbarProfiles;
    
    public HotbarController() {
        // Initialize 9 slot profiles for the hotbar
        hotbarProfiles = new SlotProfile[9];
        for (int i = 0; i < 9; i++) {
            hotbarProfiles[i] = new SlotProfile(i);
        }
    }
    
    /**
     * Organizes the player's hotbar based on the configured slot profiles
     * @param player The player whose hotbar to organize
     */
    public void organizeHotbar(ClientPlayerEntity player) {
        // Check if it's safe to organize the loadout
        if (!SafetyChecker.isSafeToOrganize(player)) {
            return;
        }
        
        PlayerInventory inventory = player.getInventory();
        
        // Process each hotbar slot
        for (int slotIndex = 0; slotIndex < 9; slotIndex++) {
            SlotProfile profile = hotbarProfiles[slotIndex];
            
            // Skip locked slots
            if (profile.isLocked()) {
                continue;
            }
            
            // Get current item in the hotbar slot
            ItemStack currentItem = inventory.getStack(slotIndex);
            
            // If single item enforcement is enabled and we already have an item, skip
            if (profile.isEnforceSingleItem() && !currentItem.isEmpty()) {
                continue;
            }
            
            // Scan inventory for matching items
            List<ItemStack> matchingItems = InventoryScanner.scanInventory(inventory, profile);
            
            // If single item enforcement is enabled across the entire hotbar,
            // filter out items that already exist in other hotbar slots
            if (profile.isEnforceSingleItem()) {
                filterItemsAlreadyInHotbar(matchingItems, inventory);
            }
            
            // Rank items based on profile criteria
            matchingItems = ItemEvaluator.rankItems(matchingItems, profile);
            
            // If we found matching items, place the best one in the slot
            if (!matchingItems.isEmpty()) {
                ItemStack bestItem = matchingItems.get(0);
                // Find the slot where this item is currently located
                int sourceSlot = findItemSlot(inventory, bestItem);
                if (sourceSlot != -1 && sourceSlot != slotIndex) {
                    // Move the best item to the hotbar slot
                    ItemSwapper.moveItem(player, sourceSlot, slotIndex);
                    
                    // If there was an item in the hotbar slot, move it to an empty slot
                    if (!currentItem.isEmpty()) {
                        int emptySlot = ItemSwapper.findEmptySlot(inventory);
                        if (emptySlot != -1 && emptySlot != slotIndex) {
                            ItemSwapper.moveItem(player, slotIndex, emptySlot);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Filters out items that already exist in other hotbar slots
     * @param items The list of items to filter
     * @param inventory The player's inventory
     */
    private void filterItemsAlreadyInHotbar(List<ItemStack> items, PlayerInventory inventory) {
        // Create a set of item identifiers that are already in hotbar slots
        Set<String> hotbarItemIds = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                Identifier itemId = Registries.ITEM.getId(stack.getItem());
                hotbarItemIds.add(itemId.toString());
            }
        }
        
        // Remove items that are already in hotbar slots
        items.removeIf(stack -> {
            Identifier itemId = Registries.ITEM.getId(stack.getItem());
            return hotbarItemIds.contains(itemId.toString());
        });
    }
    
    /**
     * Finds the slot containing a specific item stack
     * @param inventory The player's inventory
     * @param targetItem The item stack to find
     * @return The slot index, or -1 if not found
     */
    private int findItemSlot(PlayerInventory inventory, ItemStack targetItem) {
        // Check main inventory including hotbar
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
     * Gets the slot profile for a specific hotbar slot
     * @param slotIndex The index of the hotbar slot (0-8)
     * @return The slot profile for that slot
     */
    public SlotProfile getSlotProfile(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= 9) {
            throw new IllegalArgumentException("Slot index must be between 0 and 8");
        }
        return hotbarProfiles[slotIndex];
    }
    
    /**
     * Sets the slot profile for a specific hotbar slot
     * @param slotIndex The index of the hotbar slot (0-8)
     * @param profile The slot profile to set
     */
    public void setSlotProfile(int slotIndex, SlotProfile profile) {
        if (slotIndex < 0 || slotIndex >= 9) {
            throw new IllegalArgumentException("Slot index must be between 0 and 8");
        }
        hotbarProfiles[slotIndex] = profile;
    }
}