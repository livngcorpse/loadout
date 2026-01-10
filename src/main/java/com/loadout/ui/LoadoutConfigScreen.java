package com.loadout.ui;

import com.loadout.LoadoutClient;
import com.loadout.LoadoutConfig;
import com.loadout.SlotRoutingConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class LoadoutConfigScreen {
    
    public static Screen create(Screen parent) {
        // Only register the config once, get the existing holder
        ConfigHolder<LoadoutConfig> holder = AutoConfig.getConfigHolder(LoadoutConfig.class);
        LoadoutConfig config = holder.getConfig();
        
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("text.autoconfig.loadout.title"))
                .setSavingRunnable(() -> {
                    // Save slot routing configs when config is saved
                    SlotRoutingConfig[] configs = config.getSlotRoutingConfigs();
                    LoadoutClient.saveSlotRoutingConfigs(configs);
                });
        
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        
        // General category
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("text.autoconfig.loadout.category.general"));
        
        general.addEntry(entryBuilder.startEnumSelector(
                        Text.translatable("text.autoconfig.loadout.option.activationMode"),
                        LoadoutConfig.ActivationMode.class,
                        config.activationMode)
                .setDefaultValue(LoadoutConfig.ActivationMode.PICKUP_ONLY)
                .setSaveConsumer(newValue -> config.activationMode = newValue)
                .build());
        
        // Slot routing configuration
        SlotRoutingConfig[] configs = config.getSlotRoutingConfigs();
        
        // Hotbar slot configuration (slots 0-8)
        for (int i = 0; i < 9; i++) {
            addSlotRoutingConfiguration(builder, configs[i], "Hotbar Slot " + (i + 1));
        }
        
        // Armor slot configuration (slots 9-12)
        String[] armorNames = {"Helmet", "Chestplate", "Leggings", "Boots"};
        for (int i = 0; i < 4; i++) {
            addSlotRoutingConfiguration(builder, configs[i + 9], armorNames[i] + " Slot");
        }
        
        // Offhand configuration (slot 13)
        addSlotRoutingConfiguration(builder, configs[13], "Offhand Slot");
        
        return builder.build();
    }
    
    private static void addSlotRoutingConfiguration(ConfigBuilder builder, SlotRoutingConfig config, String slotName) {
        ConfigEntryBuilder entryBuilder = builder.getEntryBuilder();
        
        // Item Category selector
        builder.getOrCreateCategory(Text.literal(slotName)).addEntry(
            entryBuilder.startEnumSelector(
                Text.translatable("text.autoconfig.loadout.option.itemCategory"),
                SlotRoutingConfig.ItemCategory.class,
                config.getItemCategory())
            .setDefaultValue(SlotRoutingConfig.ItemCategory.NONE)
            .setSaveConsumer(newValue -> config.setItemCategory(newValue))
            .build()
        );
        
        // Replacement Mode selector
        builder.getOrCreateCategory(Text.literal(slotName)).addEntry(
            entryBuilder.startEnumSelector(
                Text.translatable("text.autoconfig.loadout.option.replacementMode"),
                SlotRoutingConfig.ReplacementMode.class,
                config.getReplacementMode())
            .setDefaultValue(SlotRoutingConfig.ReplacementMode.NEVER)
            .setSaveConsumer(newValue -> config.setReplacementMode(newValue))
            .build()
        );
        
        // Locked toggle
        builder.getOrCreateCategory(Text.literal(slotName)).addEntry(
            entryBuilder.startBooleanToggle(
                Text.translatable("text.autoconfig.loadout.option.locked"),
                config.isLocked())
            .setDefaultValue(false)
            .setSaveConsumer(newValue -> config.setLocked(newValue))
            .build()
        );
    }
}