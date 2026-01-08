package com.loadout;

import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple category mapping utility for the routing system
 * Maps items to their appropriate categories for routing
 */
public class CategoryMapper {
    private static final Map<String, SlotRoutingConfig.ItemCategory> ITEM_CATEGORY_MAP = new HashMap<>();
    
    static {
        // Weapon items
        ITEM_CATEGORY_MAP.put("wooden_sword", SlotRoutingConfig.ItemCategory.WEAPON);
        ITEM_CATEGORY_MAP.put("stone_sword", SlotRoutingConfig.ItemCategory.WEAPON);
        ITEM_CATEGORY_MAP.put("iron_sword", SlotRoutingConfig.ItemCategory.WEAPON);
        ITEM_CATEGORY_MAP.put("golden_sword", SlotRoutingConfig.ItemCategory.WEAPON);
        ITEM_CATEGORY_MAP.put("diamond_sword", SlotRoutingConfig.ItemCategory.WEAPON);
        ITEM_CATEGORY_MAP.put("netherite_sword", SlotRoutingConfig.ItemCategory.WEAPON);
        
        ITEM_CATEGORY_MAP.put("wooden_axe", SlotRoutingConfig.ItemCategory.WEAPON);
        ITEM_CATEGORY_MAP.put("stone_axe", SlotRoutingConfig.ItemCategory.WEAPON);
        ITEM_CATEGORY_MAP.put("iron_axe", SlotRoutingConfig.ItemCategory.WEAPON);
        ITEM_CATEGORY_MAP.put("golden_axe", SlotRoutingConfig.ItemCategory.WEAPON);
        ITEM_CATEGORY_MAP.put("diamond_axe", SlotRoutingConfig.ItemCategory.WEAPON);
        ITEM_CATEGORY_MAP.put("netherite_axe", SlotRoutingConfig.ItemCategory.WEAPON);
        
        // Tool items
        ITEM_CATEGORY_MAP.put("wooden_pickaxe", SlotRoutingConfig.ItemCategory.TOOL);
        ITEM_CATEGORY_MAP.put("stone_pickaxe", SlotRoutingConfig.ItemCategory.TOOL);
        ITEM_CATEGORY_MAP.put("iron_pickaxe", SlotRoutingConfig.ItemCategory.TOOL);
        ITEM_CATEGORY_MAP.put("golden_pickaxe", SlotRoutingConfig.ItemCategory.TOOL);
        ITEM_CATEGORY_MAP.put("diamond_pickaxe", SlotRoutingConfig.ItemCategory.TOOL);
        ITEM_CATEGORY_MAP.put("netherite_pickaxe", SlotRoutingConfig.ItemCategory.TOOL);
        
        ITEM_CATEGORY_MAP.put("wooden_shovel", SlotRoutingConfig.ItemCategory.TOOL);
        ITEM_CATEGORY_MAP.put("stone_shovel", SlotRoutingConfig.ItemCategory.TOOL);
        ITEM_CATEGORY_MAP.put("iron_shovel", SlotRoutingConfig.ItemCategory.TOOL);
        ITEM_CATEGORY_MAP.put("golden_shovel", SlotRoutingConfig.ItemCategory.TOOL);
        ITEM_CATEGORY_MAP.put("diamond_shovel", SlotRoutingConfig.ItemCategory.TOOL);
        ITEM_CATEGORY_MAP.put("netherite_shovel", SlotRoutingConfig.ItemCategory.TOOL);
        
        ITEM_CATEGORY_MAP.put("wooden_hoe", SlotRoutingConfig.ItemCategory.TOOL);
        ITEM_CATEGORY_MAP.put("stone_hoe", SlotRoutingConfig.ItemCategory.TOOL);
        ITEM_CATEGORY_MAP.put("iron_hoe", SlotRoutingConfig.ItemCategory.TOOL);
        ITEM_CATEGORY_MAP.put("golden_hoe", SlotRoutingConfig.ItemCategory.TOOL);
        ITEM_CATEGORY_MAP.put("diamond_hoe", SlotRoutingConfig.ItemCategory.TOOL);
        ITEM_CATEGORY_MAP.put("netherite_hoe", SlotRoutingConfig.ItemCategory.TOOL);
        
        // Armor items
        ITEM_CATEGORY_MAP.put("leather_helmet", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("leather_chestplate", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("leather_leggings", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("leather_boots", SlotRoutingConfig.ItemCategory.ARMOR);
        
        ITEM_CATEGORY_MAP.put("chainmail_helmet", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("chainmail_chestplate", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("chainmail_leggings", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("chainmail_boots", SlotRoutingConfig.ItemCategory.ARMOR);
        
        ITEM_CATEGORY_MAP.put("iron_helmet", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("iron_chestplate", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("iron_leggings", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("iron_boots", SlotRoutingConfig.ItemCategory.ARMOR);
        
        ITEM_CATEGORY_MAP.put("golden_helmet", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("golden_chestplate", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("golden_leggings", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("golden_boots", SlotRoutingConfig.ItemCategory.ARMOR);
        
        ITEM_CATEGORY_MAP.put("diamond_helmet", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("diamond_chestplate", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("diamond_leggings", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("diamond_boots", SlotRoutingConfig.ItemCategory.ARMOR);
        
        ITEM_CATEGORY_MAP.put("netherite_helmet", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("netherite_chestplate", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("netherite_leggings", SlotRoutingConfig.ItemCategory.ARMOR);
        ITEM_CATEGORY_MAP.put("netherite_boots", SlotRoutingConfig.ItemCategory.ARMOR);
        
        ITEM_CATEGORY_MAP.put("turtle_helmet", SlotRoutingConfig.ItemCategory.ARMOR);
        
        // Food items
        ITEM_CATEGORY_MAP.put("apple", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("golden_apple", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("enchanted_golden_apple", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("mushroom_stew", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("bread", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("porkchop", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("cooked_porkchop", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("cod", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("salmon", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("tropical_fish", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("pufferfish", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("cooked_cod", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("cooked_salmon", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("cookie", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("melon_slice", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("dried_kelp", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("beef", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("cooked_beef", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("chicken", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("cooked_chicken", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("rotten_flesh", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("spider_eye", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("carrot", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("potato", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("baked_potato", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("poisonous_potato", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("golden_carrot", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("pumpkin_pie", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("rabbit", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("cooked_rabbit", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("rabbit_stew", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("mutton", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("cooked_mutton", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("chorus_fruit", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("beetroot", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("beetroot_soup", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("sweet_berries", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("glow_berries", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("honey_bottle", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("suspicious_stew", SlotRoutingConfig.ItemCategory.FOOD);
        ITEM_CATEGORY_MAP.put("sweet_berry_bush", SlotRoutingConfig.ItemCategory.FOOD);
        
        // Potion items
        ITEM_CATEGORY_MAP.put("potion", SlotRoutingConfig.ItemCategory.POTION);
        ITEM_CATEGORY_MAP.put("splash_potion", SlotRoutingConfig.ItemCategory.POTION);
        ITEM_CATEGORY_MAP.put("lingering_potion", SlotRoutingConfig.ItemCategory.POTION);
        ITEM_CATEGORY_MAP.put("tipped_arrow", SlotRoutingConfig.ItemCategory.POTION);
    }
    
    /**
     * Gets the category for an item stack
     * @param itemStack The item stack to check
     * @return The category for the item
     */
    public static SlotRoutingConfig.ItemCategory getCategory(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return SlotRoutingConfig.ItemCategory.NONE;
        }
        
        // Get the item identifier
        Identifier itemId = Registries.ITEM.getId(itemStack.getItem());
        String itemName = itemId.getPath();
        
        // Check if we have a mapping for this item
        if (ITEM_CATEGORY_MAP.containsKey(itemName)) {
            return ITEM_CATEGORY_MAP.get(itemName);
        }
        
        // Check if it's an armor item
        if (itemStack.getItem() instanceof ArmorItem) {
            return SlotRoutingConfig.ItemCategory.ARMOR;
        }
        
        // Check if it's a weapon item
        if (itemStack.getItem() instanceof SwordItem || itemStack.getItem() instanceof AxeItem) {
            return SlotRoutingConfig.ItemCategory.WEAPON;
        }
        
        // Check if it's a tool item
        if (itemStack.getItem() instanceof ToolItem) {
            return SlotRoutingConfig.ItemCategory.TOOL;
        }
        
        // Check if it's food
        if (itemStack.getItem().isFood()) {
            return SlotRoutingConfig.ItemCategory.FOOD;
        }
        
        // Check if it's a block
        if (itemStack.getItem() instanceof BlockItem) {
            return SlotRoutingConfig.ItemCategory.BLOCK;
        }
        
        // Check if it's a potion
        if (itemStack.getItem() instanceof PotionItem || itemStack.getItem() instanceof TippedArrowItem) {
            return SlotRoutingConfig.ItemCategory.POTION;
        }
        
        // Default to misc if we can't determine the category
        return SlotRoutingConfig.ItemCategory.MISC;
    }
}