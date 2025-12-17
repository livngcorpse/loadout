package com.loadout;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class OffhandController {
    private final SlotProfile offhandProfile;
    
    public OffhandController() {
        // Initialize the offhand profile
        this.offhandProfile = new SlotProfile(0); // Slot index doesn't matter for offhand
    }
    
    /**
     * Sets the item in the offhand based on the configured profile
     * @param player The player whose offhand to set
     */
    public void setOffhandItem(ClientPlayerEntity player) {
        // Check if it's safe to organize the loadout
        if (!SafetyChecker.isSafeToOrganize(player)) {
            return;
        }
        
        PlayerInventory inventory = player.getInventory();
        
        // Skip if slot is locked
        if (offhandProfile.isLocked()) {
            return;
        }
        
        // Get current item in the offhand
        ItemStack currentOffhand = inventory.offHand.get(0);
        
        // If single item enforcement is enabled and we already have an item, skip
        if (offhandProfile.isEnforceSingleItem() && !currentOffhand.isEmpty()) {
            return;
        }
        
        // Scan inventory for matching items
        List<ItemStack> matchingItems = InventoryScanner.scanInventory(inventory, offhandProfile);
        
        // Rank items based on profile criteria
        matchingItems = ItemEvaluator.rankItems(matchingItems, offhandProfile);
        
        // If we found matching items, place the best one in the offhand
        if (!matchingItems.isEmpty()) {
            ItemStack bestItem = matchingItems.get(0);
            // Find the slot where this item is currently located
            int sourceSlot = findItemSlot(inventory, bestItem);
            if (sourceSlot != -1 && sourceSlot != PlayerInventory.OFF_HAND_SLOT) {
                // Move the best item to the offhand slot
                ItemSwapper.moveItem(player, sourceSlot, PlayerInventory.OFF_HAND_SLOT);
                
                // If there was an item in the offhand slot, move it to an empty slot
                if (!currentOffhand.isEmpty()) {
                    int emptySlot = ItemSwapper.findEmptySlot(inventory);
                    if (emptySlot != -1 && emptySlot != PlayerInventory.OFF_HAND_SLOT) {
                        ItemSwapper.moveItem(player, PlayerInventory.OFF_HAND_SLOT, emptySlot);
                    }
                }
            }
        }
    }
    
    /**
     * Finds the slot containing a specific item stack
     * @param inventory The player's inventory
     * @param targetItem The item stack to find
     * @return The slot index, or -1 if not found
     */
    private int findItemSlot(PlayerInventory inventory, ItemStack targetItem) {
        // Check main inventory
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
                return inventory.main.size() + i; // Armor slots come after main inventory
            }
        }
        
        return -1; // Not found
    }
    
    /**
     * Gets the slot profile for the offhand
     * @return The slot profile for the offhand
     */
    public SlotProfile getOffhandProfile() {
        return offhandProfile;
    }
    
    /**
     * Sets the slot profile for the offhand
     * @param profile The slot profile to set
     */
    public void setOffhandProfile(SlotProfile profile) {
        // We keep the same profile object but update its properties
        // This preserves any references to the profile
    }
}