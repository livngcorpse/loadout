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
        
        // Apply loaded configuration data
        for (SlotRoutingConfigData data : slotRoutingConfigs) {
            if (data.slotIndex >= 0 && data.slotIndex < configs.length) {
                data.applyTo(configs[data.slotIndex]);
            }
        }
        
        return configs;
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