package com.loadout;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class MaterialMapper {
    private static final Map<String, SlotProfile.MaterialPriority> MATERIAL_PRIORITY_MAP = new HashMap<>();
    
    static {
        // Wood tools
        MATERIAL_PRIORITY_MAP.put("wooden_sword", SlotProfile.MaterialPriority.WOOD);
        MATERIAL_PRIORITY_MAP.put("wooden_pickaxe", SlotProfile.MaterialPriority.WOOD);
        MATERIAL_PRIORITY_MAP.put("wooden_axe", SlotProfile.MaterialPriority.WOOD);
        MATERIAL_PRIORITY_MAP.put("wooden_shovel", SlotProfile.MaterialPriority.WOOD);
        MATERIAL_PRIORITY_MAP.put("wooden_hoe", SlotProfile.MaterialPriority.WOOD);
        
        // Stone tools
        MATERIAL_PRIORITY_MAP.put("stone_sword", SlotProfile.MaterialPriority.STONE);
        MATERIAL_PRIORITY_MAP.put("stone_pickaxe", SlotProfile.MaterialPriority.STONE);
        MATERIAL_PRIORITY_MAP.put("stone_axe", SlotProfile.MaterialPriority.STONE);
        MATERIAL_PRIORITY_MAP.put("stone_shovel", SlotProfile.MaterialPriority.STONE);
        MATERIAL_PRIORITY_MAP.put("stone_hoe", SlotProfile.MaterialPriority.STONE);
        
        // Iron tools and armor
        MATERIAL_PRIORITY_MAP.put("iron_sword", SlotProfile.MaterialPriority.IRON);
        MATERIAL_PRIORITY_MAP.put("iron_pickaxe", SlotProfile.MaterialPriority.IRON);
        MATERIAL_PRIORITY_MAP.put("iron_axe", SlotProfile.MaterialPriority.IRON);
        MATERIAL_PRIORITY_MAP.put("iron_shovel", SlotProfile.MaterialPriority.IRON);
        MATERIAL_PRIORITY_MAP.put("iron_hoe", SlotProfile.MaterialPriority.IRON);
        MATERIAL_PRIORITY_MAP.put("iron_helmet", SlotProfile.MaterialPriority.IRON);
        MATERIAL_PRIORITY_MAP.put("iron_chestplate", SlotProfile.MaterialPriority.IRON);
        MATERIAL_PRIORITY_MAP.put("iron_leggings", SlotProfile.MaterialPriority.IRON);
        MATERIAL_PRIORITY_MAP.put("iron_boots", SlotProfile.MaterialPriority.IRON);
        
        // Gold tools and armor
        MATERIAL_PRIORITY_MAP.put("golden_sword", SlotProfile.MaterialPriority.GOLD);
        MATERIAL_PRIORITY_MAP.put("golden_pickaxe", SlotProfile.MaterialPriority.GOLD);
        MATERIAL_PRIORITY_MAP.put("golden_axe", SlotProfile.MaterialPriority.GOLD);
        MATERIAL_PRIORITY_MAP.put("golden_shovel", SlotProfile.MaterialPriority.GOLD);
        MATERIAL_PRIORITY_MAP.put("golden_hoe", SlotProfile.MaterialPriority.GOLD);
        MATERIAL_PRIORITY_MAP.put("golden_helmet", SlotProfile.MaterialPriority.GOLD);
        MATERIAL_PRIORITY_MAP.put("golden_chestplate", SlotProfile.MaterialPriority.GOLD);
        MATERIAL_PRIORITY_MAP.put("golden_leggings", SlotProfile.MaterialPriority.GOLD);
        MATERIAL_PRIORITY_MAP.put("golden_boots", SlotProfile.MaterialPriority.GOLD);
        
        // Diamond tools and armor
        MATERIAL_PRIORITY_MAP.put("diamond_sword", SlotProfile.MaterialPriority.DIAMOND);
        MATERIAL_PRIORITY_MAP.put("diamond_pickaxe", SlotProfile.MaterialPriority.DIAMOND);
        MATERIAL_PRIORITY_MAP.put("diamond_axe", SlotProfile.MaterialPriority.DIAMOND);
        MATERIAL_PRIORITY_MAP.put("diamond_shovel", SlotProfile.MaterialPriority.DIAMOND);
        MATERIAL_PRIORITY_MAP.put("diamond_hoe", SlotProfile.MaterialPriority.DIAMOND);
        MATERIAL_PRIORITY_MAP.put("diamond_helmet", SlotProfile.MaterialPriority.DIAMOND);
        MATERIAL_PRIORITY_MAP.put("diamond_chestplate", SlotProfile.MaterialPriority.DIAMOND);
        MATERIAL_PRIORITY_MAP.put("diamond_leggings", SlotProfile.MaterialPriority.DIAMOND);
        MATERIAL_PRIORITY_MAP.put("diamond_boots", SlotProfile.MaterialPriority.DIAMOND);
        
        // Netherite tools and armor
        MATERIAL_PRIORITY_MAP.put("netherite_sword", SlotProfile.MaterialPriority.NETHERITE);
        MATERIAL_PRIORITY_MAP.put("netherite_pickaxe", SlotProfile.MaterialPriority.NETHERITE);
        MATERIAL_PRIORITY_MAP.put("netherite_axe", SlotProfile.MaterialPriority.NETHERITE);
        MATERIAL_PRIORITY_MAP.put("netherite_shovel", SlotProfile.MaterialPriority.NETHERITE);
        MATERIAL_PRIORITY_MAP.put("netherite_hoe", SlotProfile.MaterialPriority.NETHERITE);
        MATERIAL_PRIORITY_MAP.put("netherite_helmet", SlotProfile.MaterialPriority.NETHERITE);
        MATERIAL_PRIORITY_MAP.put("netherite_chestplate", SlotProfile.MaterialPriority.NETHERITE);
        MATERIAL_PRIORITY_MAP.put("netherite_leggings", SlotProfile.MaterialPriority.NETHERITE);
        MATERIAL_PRIORITY_MAP.put("netherite_boots", SlotProfile.MaterialPriority.NETHERITE);
    }
    
    /**
     * Gets the material priority for an item stack
     * @param stack The item stack to check
     * @return The material priority for the item
     */
    public static SlotProfile.MaterialPriority getMaterialPriority(ItemStack stack) {
        if (stack.isEmpty()) {
            return SlotProfile.MaterialPriority.NONE;
        }
        
        // Get the item identifier
        Identifier itemId = Registries.ITEM.getId(stack.getItem());
        String itemName = itemId.getPath();
        
        // Check if we have a mapping for this item
        if (MATERIAL_PRIORITY_MAP.containsKey(itemName)) {
            return MATERIAL_PRIORITY_MAP.get(itemName);
        }
        
        // Check if it's an armor item
        if (stack.getItem() instanceof ArmorItem) {
            ArmorItem armorItem = (ArmorItem) stack.getItem();
            String materialName = armorItem.getMaterial().getName();
            switch (materialName) {
                case "leather":
                    return SlotProfile.MaterialPriority.NONE; // Leather is not in our priority system
                case "chainmail":
                    return SlotProfile.MaterialPriority.IRON; // Chainmail is equivalent to iron
                case "gold":
                    return SlotProfile.MaterialPriority.GOLD;
                case "iron":
                    return SlotProfile.MaterialPriority.IRON;
                case "diamond":
                    return SlotProfile.MaterialPriority.DIAMOND;
                case "netherite":
                    return SlotProfile.MaterialPriority.NETHERITE;
                case "turtle":
                    return SlotProfile.MaterialPriority.NONE; // Turtle shell is special
            }
        }
        
        // Check if it's a tool item
        if (stack.getItem() instanceof ToolItem) {
            ToolItem toolItem = (ToolItem) stack.getItem();
            String materialName = toolItem.getMaterial().getName();
            switch (materialName) {
                case "wood":
                    return SlotProfile.MaterialPriority.WOOD;
                case "stone":
                    return SlotProfile.MaterialPriority.STONE;
                case "iron":
                    return SlotProfile.MaterialPriority.IRON;
                case "gold":
                    return SlotProfile.MaterialPriority.GOLD;
                case "diamond":
                    return SlotProfile.MaterialPriority.DIAMOND;
                case "netherite":
                    return SlotProfile.MaterialPriority.NETHERITE;
            }
        }
        
        // Default to none if we can't determine the material
        return SlotProfile.MaterialPriority.NONE;
    }
    
    /**
     * Compares two items based on material priority
     * @param a First item
     * @param b Second item
     * @param priority The desired material priority
     * @return Negative if a is preferred, positive if b is preferred, 0 if equal
     */
    public static int compareByMaterialPriority(ItemStack a, ItemStack b, SlotProfile.MaterialPriority priority) {
        if (priority == SlotProfile.MaterialPriority.NONE) {
            return 0;
        }
        
        SlotProfile.MaterialPriority materialA = getMaterialPriority(a);
        SlotProfile.MaterialPriority materialB = getMaterialPriority(b);
        
        // Convert priority enum to numeric value for comparison
        int priorityValue = getMaterialPriorityValue(priority);
        int valueA = getMaterialPriorityValue(materialA);
        int valueB = getMaterialPriorityValue(materialB);
        
        // If we want higher priority materials, higher values are better
        // If we want lower priority materials, lower values are better
        return Integer.compare(Math.abs(valueA - priorityValue), Math.abs(valueB - priorityValue));
    }
    
    /**
     * Converts a material priority enum to a numeric value
     * @param priority The material priority
     * @return The numeric value (higher = better material)
     */
    private static int getMaterialPriorityValue(SlotProfile.MaterialPriority priority) {
        switch (priority) {
            case NONE: return 0;
            case WOOD: return 1;
            case STONE: return 2;
            case IRON: return 3;
            case GOLD: return 4;
            case DIAMOND: return 5;
            case NETHERITE: return 6;
            default: return 0;
        }
    }
}