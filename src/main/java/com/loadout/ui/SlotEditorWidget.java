package com.loadout.ui;

import com.loadout.SlotProfile;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class SlotEditorWidget {
    
    /**
     * Adds slot configuration entries to the config builder
     * @param builder The config builder
     * @param profile The slot profile to configure
     * @param slotName The name of the slot (e.g., "Hotbar Slot 1", "Helmet Slot")
     */
    public static void addSlotConfiguration(ConfigBuilder builder, SlotProfile profile, String slotName) {
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory slotCategory = builder.getOrCreateCategory(Text.literal(slotName));
        
        // Allowed items
        slotCategory.addEntry(entryBuilder.startStrList(
                        Text.translatable("text.autoconfig.loadout.option.allowedItems"),
                        profile.getAllowedItems().toArray(new String[0]))
                .setDefaultValue(new String[0])
                .setSaveConsumer(newValue -> {
                    profile.getAllowedItems().clear();
                    for (String item : newValue) {
                        profile.addAllowedItem(item);
                    }
                })
                .build());
        
        // Material priority
        slotCategory.addEntry(entryBuilder.startEnumSelector(
                        Text.translatable("text.autoconfig.loadout.option.materialPriority"),
                        SlotProfile.MaterialPriority.class,
                        profile.getMaterialPriority())
                .setDefaultValue(SlotProfile.MaterialPriority.NONE)
                .setSaveConsumer(newValue -> profile.setMaterialPriority(newValue))
                .build());
        
        // Durability preference
        slotCategory.addEntry(entryBuilder.startEnumSelector(
                        Text.translatable("text.autoconfig.loadout.option.durabilityPreference"),
                        SlotProfile.DurabilityPreference.class,
                        profile.getDurabilityPreference())
                .setDefaultValue(SlotProfile.DurabilityPreference.NONE)
                .setSaveConsumer(newValue -> profile.setDurabilityPreference(newValue))
                .build());
        
        // Consider enchantments
        slotCategory.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("text.autoconfig.loadout.option.considerEnchantments"),
                        profile.isConsiderEnchantments())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> profile.setConsiderEnchantments(newValue))
                .build());
        
        // Enforce single item
        slotCategory.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("text.autoconfig.loadout.option.enforceSingleItem"),
                        profile.isEnforceSingleItem())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> profile.setEnforceSingleItem(newValue))
                .build());
        
        // Locked
        slotCategory.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("text.autoconfig.loadout.option.locked"),
                        profile.isLocked())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> profile.setLocked(newValue))
                .build());
    }
}