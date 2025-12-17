package com.loadout;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

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
            
            // Rank items based on profile criteria
            matchingItems = ItemEvaluator.rankItems(matchingItems, profile);
            
            // If we found matching items, place the best one in the slot
            if (!matchingItems.isEmpty()) {
                ItemStack bestItem = matchingItems.get(0);
                // TODO: Implement item swapping logic
                // This would involve moving items between slots
            }
        }
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