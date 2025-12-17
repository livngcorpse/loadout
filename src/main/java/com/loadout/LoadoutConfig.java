package com.loadout;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@Config(name = "loadout")
public class LoadoutConfig implements ConfigData {
    
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ActivationMode activationMode = ActivationMode.MANUAL_ONLY;
    
    public boolean enableHotbarManagement = false;
    public boolean enableArmorManagement = false;
    public boolean enableOffhandManagement = false;
    
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int respawnDelayTicks = 20;
    
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int cooldownTicks = 10;
    
    // Slot profiles for persistence
    public List<SlotProfileData> hotbarSlots = new ArrayList<>();
    public List<SlotProfileData> armorSlots = new ArrayList<>();
    public SlotProfileData offhandSlot = new SlotProfileData();
    
    public enum ActivationMode {
        MANUAL_ONLY,
        RESPAWN_ONLY,
        PICKUP_ONLY,
        ALL_EVENTS
    }
    
    // Data class for serializing SlotProfile
    public static class SlotProfileData {
        public int slotIndex = 0;
        public String[] allowedItems = new String[0];
        public SlotProfile.MaterialPriority materialPriority = SlotProfile.MaterialPriority.NONE;
        public SlotProfile.DurabilityPreference durabilityPreference = SlotProfile.DurabilityPreference.NONE;
        public boolean considerEnchantments = false;
        public boolean enforceSingleItem = false;
        public boolean locked = false;
        
        public SlotProfileData() {}
        
        public SlotProfileData(SlotProfile profile) {
            this.slotIndex = profile.getSlotIndex();
            this.allowedItems = profile.getAllowedItems().toArray(new String[0]);
            this.materialPriority = profile.getMaterialPriority();
            this.durabilityPreference = profile.getDurabilityPreference();
            this.considerEnchantments = profile.isConsiderEnchantments();
            this.enforceSingleItem = profile.isEnforceSingleItem();
            this.locked = profile.isLocked();
        }
        
        public void applyTo(SlotProfile profile) {
            profile.setSlotIndex(slotIndex);
            profile.getAllowedItems().clear();
            for (String item : allowedItems) {
                profile.addAllowedItem(item);
            }
            profile.setMaterialPriority(materialPriority);
            profile.setDurabilityPreference(durabilityPreference);
            profile.setConsiderEnchantments(considerEnchantments);
            profile.setEnforceSingleItem(enforceSingleItem);
            profile.setLocked(locked);
        }
    }
}