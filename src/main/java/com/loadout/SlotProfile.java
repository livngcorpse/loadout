package com.loadout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SlotProfile {
    private int slotIndex;
    private Set<String> allowedItems;
    private MaterialPriority materialPriority;
    private DurabilityPreference durabilityPreference;
    private boolean considerEnchantments;
    private boolean enforceSingleItem;
    private boolean locked;
    
    public SlotProfile(int slotIndex) {
        this.slotIndex = slotIndex;
        this.allowedItems = new HashSet<>();
        this.materialPriority = MaterialPriority.NONE;
        this.durabilityPreference = DurabilityPreference.NONE;
        this.considerEnchantments = false;
        this.enforceSingleItem = false;
        this.locked = false;
    }
    
    // Getters and setters
    public int getSlotIndex() {
        return slotIndex;
    }
    
    public void setSlotIndex(int slotIndex) {
        this.slotIndex = slotIndex;
    }
    
    public Set<String> getAllowedItems() {
        return allowedItems;
    }
    
    public void setAllowedItems(Set<String> allowedItems) {
        this.allowedItems = allowedItems;
    }
    
    public void addAllowedItem(String itemId) {
        this.allowedItems.add(itemId);
    }
    
    public void removeAllowedItem(String itemId) {
        this.allowedItems.remove(itemId);
    }
    
    public boolean isItemAllowed(String itemId) {
        return this.allowedItems.isEmpty() || this.allowedItems.contains(itemId);
    }
    
    public MaterialPriority getMaterialPriority() {
        return materialPriority;
    }
    
    public void setMaterialPriority(MaterialPriority materialPriority) {
        this.materialPriority = materialPriority;
    }
    
    public DurabilityPreference getDurabilityPreference() {
        return durabilityPreference;
    }
    
    public void setDurabilityPreference(DurabilityPreference durabilityPreference) {
        this.durabilityPreference = durabilityPreference;
    }
    
    public boolean isConsiderEnchantments() {
        return considerEnchantments;
    }
    
    public void setConsiderEnchantments(boolean considerEnchantments) {
        this.considerEnchantments = considerEnchantments;
    }
    
    public boolean isEnforceSingleItem() {
        return enforceSingleItem;
    }
    
    public void setEnforceSingleItem(boolean enforceSingleItem) {
        this.enforceSingleItem = enforceSingleItem;
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
    public enum MaterialPriority {
        NONE,
        WOOD,
        STONE,
        IRON,
        GOLD,
        DIAMOND,
        NETHERITE
    }
    
    public enum DurabilityPreference {
        NONE,
        HIGHEST,
        LOWEST
    }
}