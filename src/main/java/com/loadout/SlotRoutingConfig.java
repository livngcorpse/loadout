package com.loadout;

/**
 * Simplified configuration for routing items to specific slots
 * Replaces the complex SlotProfile with a simpler routing-based approach
 */
public class SlotRoutingConfig {
    private int slotIndex;
    private ItemCategory itemCategory;
    private String customItemId;  // Used when itemCategory is CUSTOM
    private ReplacementMode replacementMode;
    private boolean locked;
    
    public SlotRoutingConfig(int slotIndex) {
        this.slotIndex = slotIndex;
        this.itemCategory = ItemCategory.NONE;
        this.customItemId = "";
        this.replacementMode = ReplacementMode.NEVER;
        this.locked = false;
    }
    
    // Getters and setters
    public int getSlotIndex() {
        return slotIndex;
    }
    
    public void setSlotIndex(int slotIndex) {
        this.slotIndex = slotIndex;
    }
    
    public ItemCategory getItemCategory() {
        return itemCategory;
    }
    
    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }
    
    public String getCustomItemId() {
        return customItemId;
    }
    
    public void setCustomItemId(String customItemId) {
        this.customItemId = customItemId;
    }
    
    public ReplacementMode getReplacementMode() {
        return replacementMode;
    }
    
    public void setReplacementMode(ReplacementMode replacementMode) {
        this.replacementMode = replacementMode;
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
    /**
     * Enum for item categories that determine where items should be routed
     */
    public enum ItemCategory {
        NONE,           // No routing (disabled)
        WEAPON,         // Swords, axes, etc.
        TOOL,           // Pickaxes, shovels, hoes, etc.
        ARMOR,          // Helmets, chestplates, etc.
        FOOD,           // Edible items
        BLOCK,          // Block items
        POTION,         // Potions
        MISC,           // Miscellaneous items
        CUSTOM          // Specific item ID
    }
    
    /**
     * Enum for replacement behavior when target slot is occupied
     */
    public enum ReplacementMode {
        NEVER,          // Never replace existing item
        SAME_TYPE_ONLY, // Only replace if same item type (e.g., diamond sword for diamond sword)
        ALWAYS          // Always replace with new item
    }
}