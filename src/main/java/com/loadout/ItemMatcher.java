package com.loadout;

import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/**
 * Simple item matching utility for the routing system
 * Replaces the complex ItemEvaluator with simple type checking
 */
public class ItemMatcher {
    
    /**
     * Checks if an item matches a specific category
     * @param itemStack The item to check
     * @param category The category to match against
     * @return true if the item matches the category, false otherwise
     */
    public static boolean matchesCategory(ItemStack itemStack, SlotRoutingConfig.ItemCategory category) {
        if (itemStack.isEmpty()) {
            return false;
        }
        
        switch (category) {
            case NONE:
                return false; // No routing
            case WEAPON:
                return isWeapon(itemStack);
            case TOOL:
                return isTool(itemStack);
            case ARMOR:
                return isArmor(itemStack);
            case FOOD:                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
                return isFood(itemStack);
            case BLOCK:
                return isBlock(itemStack);
            case POTION:
                return isPotion(itemStack);
            case MISC:
                return !isWeapon(itemStack) && !isTool(itemStack) && 
                       !isArmor(itemStack) && !isFood(itemStack) && 
                       !isBlock(itemStack) && !isPotion(itemStack);
            case CUSTOM:
                // For custom, we'd need to check against specific item ID
                // This would be handled by the caller with a specific item check
                return false; // Placeholder - actual custom matching would be done elsewhere
            default:
                return false;
        }
    }
    
    /**
     * Checks if two items are of the same type (same item class)
     * @param itemA First item
     * @param itemB Second item
     * @return true if items are the same type, false otherwise
     */
    public static boolean isSameItemType(ItemStack itemA, ItemStack itemB) {
        if (itemA.isEmpty() || itemB.isEmpty()) {
            return itemA.isEmpty() && itemB.isEmpty();
        }
        
        return itemA.getItem() == itemB.getItem();
    }
    
    /**
     * Checks if an item is a weapon
     * @param itemStack The item to check
     * @return true if the item is a weapon, false otherwise
     */
    private static boolean isWeapon(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item instanceof SwordItem || item instanceof AxeItem;
    }
    
    /**
     * Checks if an item is a tool
     * @param itemStack The item to check
     * @return true if the item is a tool, false otherwise
     */
    private static boolean isTool(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item instanceof PickaxeItem || 
               item instanceof ShovelItem || 
               item instanceof HoeItem;
    }
    
    /**
     * Checks if an item is armor
     * @param itemStack The item to check
     * @return true if the item is armor, false otherwise
     */
    private static boolean isArmor(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item instanceof ArmorItem;
    }
    
    /**
     * Checks if an item is food
     * @param itemStack The item to check
     * @return true if the item is food, false otherwise
     */
    private static boolean isFood(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item.isFood();
    }
    
    /**
     * Checks if an item is a block
     * @param itemStack The item to check
     * @return true if the item is a block, false otherwise
     */
    private static boolean isBlock(ItemStack itemStack) {
        Item item = itemStack.getItem();
        // Check if the item is a block item (can be placed as a block)
        return item instanceof BlockItem;
    }
    
    /**
     * Checks if an item is a potion
     * @param itemStack The item to check
     * @return true if the item is a potion, false otherwise
     */
    private static boolean isPotion(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item instanceof PotionItem || item instanceof TippedArrowItem;
    }
}