package com.loadout.ui;

import com.loadout.LoadoutClient;
import com.loadout.LoadoutConfig;
import com.loadout.SlotProfile;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
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
                .setTitle(Text.translatable("text.autoconfig.loadout.title"));
        
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        
        // General category
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("text.autoconfig.loadout.category.general"));
        
        general.addEntry(entryBuilder.startEnumSelector(
                        Text.translatable("text.autoconfig.loadout.option.activationMode"),
                        LoadoutConfig.ActivationMode.class,
                        config.activationMode)
                .setDefaultValue(LoadoutConfig.ActivationMode.MANUAL_ONLY)
                .setSaveConsumer(newValue -> config.activationMode = newValue)
                .build());
        
        general.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("text.autoconfig.loadout.option.enableHotbarManagement"),
                        config.enableHotbarManagement)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.enableHotbarManagement = newValue)
                .build());
        
        general.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("text.autoconfig.loadout.option.enableArmorManagement"),
                        config.enableArmorManagement)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.enableArmorManagement = newValue)
                .build());
        
        general.addEntry(entryBuilder.startBooleanToggle(
                        Text.translatable("text.autoconfig.loadout.option.enableOffhandManagement"),
                        config.enableOffhandManagement)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.enableOffhandManagement = newValue)
                .build());
        
        general.addEntry(entryBuilder.startIntField(
                        Text.translatable("text.autoconfig.loadout.option.respawnDelayTicks"),
                        config.respawnDelayTicks)
                .setDefaultValue(20)
                .setMin(0)
                .setMax(100)
                .setSaveConsumer(newValue -> config.respawnDelayTicks = newValue)
                .build());
        
        general.addEntry(entryBuilder.startIntField(
                        Text.translatable("text.autoconfig.loadout.option.cooldownTicks"),
                        config.cooldownTicks)
                .setDefaultValue(10)
                .setMin(0)
                .setMax(100)
                .setSaveConsumer(newValue -> config.cooldownTicks = newValue)
                .build());
        
        // Hotbar slot configuration
        for (int i = 0; i < 9; i++) {
            SlotProfile profile = LoadoutClient.getHotbarController().getSlotProfile(i);
            SlotEditorWidget.addSlotConfiguration(builder, profile, "Hotbar Slot " + (i + 1));
        }
        
        // Armor slot configuration
        String[] armorNames = {"Helmet", "Chestplate", "Leggings", "Boots"};
        for (int i = 0; i < 4; i++) {
            SlotProfile profile = LoadoutClient.getArmorController().getArmorProfile(i);
            SlotEditorWidget.addSlotConfiguration(builder, profile, armorNames[i] + " Slot");
        }
        
        // Offhand configuration
        SlotProfile offhandProfile = LoadoutClient.getOffhandController().getOffhandProfile();
        SlotEditorWidget.addSlotConfiguration(builder, offhandProfile, "Offhand Slot");
        
        return builder.build();
    }
}