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
            // TODO: Implement offhand item setting logic
            // This would involve moving the item to the offhand slot
        }
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