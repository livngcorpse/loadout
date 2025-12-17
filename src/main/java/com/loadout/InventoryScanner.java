package com.loadout;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class InventoryScanner {
    
    /**
     * Scans the player's inventory and returns all items that match the given slot profile
     * @param inventory The player's inventory
     * @param profile The slot profile to match against
     * @return List of matching items
     */
    public static List<ItemStack> scanInventory(PlayerInventory inventory, SlotProfile profile) {
        List<ItemStack> matchingItems = new ArrayList<>();
        
        // Scan main inventory (excluding hotbar)
        for (int i = 9; i < inventory.main.size(); i++) {
            ItemStack stack = inventory.main.get(i);
            if (!stack.isEmpty() && matchesProfile(stack, profile)) {
                matchingItems.add(stack.copy());
            }
        }
        
        // Scan offhand
        ItemStack offhandStack = inventory.offHand.get(0);
        if (!offhandStack.isEmpty() && matchesProfile(offhandStack, profile)) {
            matchingItems.add(offhandStack.copy());
        }
        
        // Scan armor slots
        for (ItemStack armorStack : inventory.armor) {
            if (!armorStack.isEmpty() && matchesProfile(armorStack, profile)) {
                matchingItems.add(armorStack.copy());
            }
        }
        
        return matchingItems;
    }
    
    /**
     * Checks if an item stack matches the given slot profile
     * @param stack The item stack to check
     * @param profile The slot profile to match against
     * @return True if the item matches the profile, false otherwise
     */
    private static boolean matchesProfile(ItemStack stack, SlotProfile profile) {
        // Check if slot is locked
        if (profile.isLocked()) {
            return false;
        }
        
        // Check allowed items
        Identifier itemId = Registries.ITEM.getId(stack.getItem());
        if (!profile.isItemAllowed(itemId.toString())) {
            return false;
        }
        
        // Check material priority - if a specific material is required, filter by it
        if (profile.getMaterialPriority() != SlotProfile.MaterialPriority.NONE) {
            SlotProfile.MaterialPriority itemMaterial = MaterialMapper.getMaterialPriority(stack);
            // If the item doesn't match the required material priority, exclude it
            // (unless we're allowing any material)
        }
        
        // For now, we'll include all items that pass the allowed items check
        // Additional filtering can be added here as needed
        
        return true;
    }
}