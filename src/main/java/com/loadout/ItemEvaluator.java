package com.loadout;

import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public class ItemEvaluator {
    
    /**
     * Ranks a list of items based on the slot profile's criteria
     * @param items The list of items to rank
     * @param profile The slot profile with ranking criteria
     * @return The sorted list of items
     */
    public static List<ItemStack> rankItems(List<ItemStack> items, SlotProfile profile) {
        if (items.isEmpty()) {
            return items;
        }
        
        items.sort(new ItemComparator(profile));
        return items;
    }
    
    /**
     * Comparator for ranking items based on slot profile criteria
     */
    private static class ItemComparator implements Comparator<ItemStack> {
        private final SlotProfile profile;
        
        public ItemComparator(SlotProfile profile) {
            this.profile = profile;
        }
        
        @Override
        public int compare(ItemStack a, ItemStack b) {
            // Check if one item should be prioritized over another based on profile
            
            // Material priority
            int materialComparison = compareByMaterial(a, b);
            if (materialComparison != 0) {
                return materialComparison;
            }
            
            // Durability preference
            int durabilityComparison = compareByDurability(a, b);
            if (durabilityComparison != 0) {
                return durabilityComparison;
            }
            
            // Enchantment consideration
            int enchantmentComparison = compareByEnchantments(a, b);
            if (enchantmentComparison != 0) {
                return enchantmentComparison;
            }
            
            // Default to comparing by count (prefer stacks with more items)
            return Integer.compare(b.getCount(), a.getCount());
        }
        
        private int compareByMaterial(ItemStack a, ItemStack b) {
            // TODO: Implement material priority comparison
            // This would require mapping items to their material types
            return 0;
        }
        
        private int compareByDurability(ItemStack a, ItemStack b) {
            SlotProfile.DurabilityPreference preference = profile.getDurabilityPreference();
            
            if (preference == SlotProfile.DurabilityPreference.HIGHEST) {
                // Prefer items with higher durability/max durability ratio
                float durabilityRatioA = getDurabilityRatio(a);
                float durabilityRatioB = getDurabilityRatio(b);
                return Float.compare(durabilityRatioB, durabilityRatioA);
            } else if (preference == SlotProfile.DurabilityPreference.LOWEST) {
                // Prefer items with lower durability/max durability ratio
                float durabilityRatioA = getDurabilityRatio(a);
                float durabilityRatioB = getDurabilityRatio(b);
                return Float.compare(durabilityRatioA, durabilityRatioB);
            }
            
            return 0;
        }
        
        private int compareByEnchantments(ItemStack a, ItemStack b) {
            if (!profile.isConsiderEnchantments()) {
                return 0;
            }
            
            // Compare by enchantment count and level
            int enchantmentCountA = a.getEnchantments().size();
            int enchantmentCountB = b.getEnchantments().size();
            
            if (enchantmentCountA != enchantmentCountB) {
                return Integer.compare(enchantmentCountB, enchantmentCountA);
            }
            
            // TODO: More sophisticated enchantment comparison
            // Could consider enchantment levels, specific enchantments, etc.
            
            return 0;
        }
        
        private float getDurabilityRatio(ItemStack stack) {
            if (!stack.isDamageable()) {
                return 1.0f; // Non-damageable items are considered "fully durable"
            }
            
            int maxDamage = stack.getMaxDamage();
            if (maxDamage <= 0) {
                return 1.0f;
            }
            
            int damage = stack.getDamage();
            return (float)(maxDamage - damage) / maxDamage;
        }
    }
}