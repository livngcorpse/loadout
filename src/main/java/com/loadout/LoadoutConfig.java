package com.loadout;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@Config(name = "loadout")
public class LoadoutConfig implements ConfigData {
    
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ActivationMode activationMode = ActivationMode.PICKUP_ONLY;
    
    // Slot routing configurations for persistence
    public List<SlotRoutingConfigData> slotRoutingConfigs = new ArrayList<>();
    
    public enum ActivationMode {
        MANUAL_ONLY,
        PICKUP_ONLY  // Only pickup mode is relevant for item routing
    }
    
    // Data class for serializing SlotRoutingConfig
    public static class SlotRoutingConfigData {
        public int slotIndex = 0;
        public ItemCategory itemCategory = ItemCategory.NONE;
        public String customItemId = "";
        public ReplacementMode replacementMode = ReplacementMode.NEVER;
        public boolean locked = false;
        
        public SlotRoutingConfigData() {}
        
        public SlotRoutingConfigData(SlotRoutingConfig config) {
            this.slotIndex = config.getSlotIndex();
            this.itemCategory = config.getItemCategory();
            this.customItemId = config.getCustomItemId();
            this.replacementMode = config.getReplacementMode();
            this.locked = config.isLocked();
        }
        
        public void applyTo(SlotRoutingConfig config) {
            config.setSlotIndex(slotIndex);
            config.setItemCategory(itemCategory);
            config.setCustomItemId(customItemId);
            config.setReplacementMode(replacementMode);
            config.setLocked(locked);
        }
        
        public enum ItemCategory {
            NONE, WEAPON, TOOL, ARMOR, FOOD, BLOCK, POTION, MISC, CUSTOM
        }
        
        public enum ReplacementMode {
            NEVER, SAME_TYPE_ONLY, ALWAYS
        }
    }
    
    /**
     * Gets the array of slot routing configurations
     * Index mapping: 0-8 hotbar, 9-12 armor, 13 offhand
     */
    public SlotRoutingConfig[] getSlotRoutingConfigs() {
        SlotRoutingConfig[] configs = new SlotRoutingConfig[14]; // 9 hotbar + 4 armor + 1 offhand
        
        // Initialize all configs
        for (int i = 0; i < configs.length; i++) {
            configs[i] = new SlotRoutingConfig(i);
        }
        
        // Set default configurations for a suggested setup
        setDefaultConfigurations(configs);
        
        // Apply loaded configuration data
        for (SlotRoutingConfigData data : slotRoutingConfigs) {
            if (data.slotIndex >= 0 && data.slotIndex < configs.length) {
                data.applyTo(configs[data.slotIndex]);
            }
        }
        
        return configs;
    }
    
    /**
     * Sets default configurations for a suggested setup
     * Slot 0: Weapon
     * Slot 1: Food
     * Slot 2: Tools
     * Slot 3: Blocks
     * Slot 4: Offhand (Totem/Chorus)
     * Slot 5: Potions
     * Slot 6: Arrows/Bows
     * Slot 7-8: Miscellaneous
     * Armor slots: Automatic routing
     */
    private void setDefaultConfigurations(SlotRoutingConfig[] configs) {
        // Hotbar defaults
        configs[0].setItemCategory(SlotRoutingConfig.ItemCategory.WEAPON);  // Weapon slot
        configs[0].setReplacementMode(SlotRoutingConfig.ReplacementMode.ALWAYS);
        
        configs[1].setItemCategory(SlotRoutingConfig.ItemCategory.FOOD);    // Food slot
        configs[1].setReplacementMode(SlotRoutingConfig.ReplacementMode.NEVER);
        
        configs[2].setItemCategory(SlotRoutingConfig.ItemCategory.TOOL);    // Tool slot
        configs[2].setReplacementMode(SlotRoutingConfig.ReplacementMode.SAME_TYPE_ONLY);
        
        configs[3].setItemCategory(SlotRoutingConfig.ItemCategory.BLOCK);   // Building blocks
        configs[3].setReplacementMode(SlotRoutingConfig.ReplacementMode.NEVER);
        
        configs[4].setItemCategory(SlotRoutingConfig.ItemCategory.MISC);    // Offhand utilities (Totems, Chorus Fruit)
        configs[4].setReplacementMode(SlotRoutingConfig.ReplacementMode.SAME_TYPE_ONLY);
        
        configs[5].setItemCategory(SlotRoutingConfig.ItemCategory.POTION); // Potions
        configs[5].setReplacementMode(SlotRoutingConfig.ReplacementMode.NEVER);
        
        configs[6].setItemCategory(SlotRoutingConfig.ItemCategory.MISC);    // Arrows/Bows
        configs[6].setReplacementMode(SlotRoutingConfig.ReplacementMode.NEVER);
        
        // Slots 7-8 remain as NONE (flexible slots)
        
        // Armor defaults
        configs[9].setItemCategory(SlotRoutingConfig.ItemCategory.ARMOR);   // Helmet
        configs[9].setReplacementMode(SlotRoutingConfig.ReplacementMode.SAME_TYPE_ONLY);
        
        configs[10].setItemCategory(SlotRoutingConfig.ItemCategory.ARMOR);  // Chestplate
        configs[10].setReplacementMode(SlotRoutingConfig.ReplacementMode.SAME_TYPE_ONLY);
        
        configs[11].setItemCategory(SlotRoutingConfig.ItemCategory.ARMOR); // Leggings
        configs[11].setReplacementMode(SlotRoutingConfig.ReplacementMode.SAME_TYPE_ONLY);
        
        configs[12].setItemCategory(SlotRoutingConfig.ItemCategory.ARMOR);  // Boots
        configs[12].setReplacementMode(SlotRoutingConfig.ReplacementMode.SAME_TYPE_ONLY);
        
        // Offhand defaults
        configs[13].setItemCategory(SlotRoutingConfig.ItemCategory.MISC);    // Offhand
        configs[13].setReplacementMode(SlotRoutingConfig.ReplacementMode.SAME_TYPE_ONLY);
    }
    
    /**
     * Updates the configuration data from the routing configs
     */
    public void updateFromSlotRoutingConfigs(SlotRoutingConfig[] configs) {
        slotRoutingConfigs.clear();
        for (SlotRoutingConfig config : configs) {
            slotRoutingConfigs.add(new SlotRoutingConfigData(config));
        }
    }
}