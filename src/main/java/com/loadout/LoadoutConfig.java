package com.loadout;

import com.loadout.ui.LoadoutConfigScreen;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import net.minecraft.client.gui.screen.Screen;

@Config(name = LoadoutClient.MOD_ID)
public class LoadoutConfig implements ConfigData {
    
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ActivationMode activationMode = ActivationMode.MANUAL_ONLY;
    
    @ConfigEntry.Gui.Tooltip
    public boolean enableHotbarManagement = false;
    
    @ConfigEntry.Gui.Tooltip
    public boolean enableArmorManagement = false;
    
    @ConfigEntry.Gui.Tooltip
    public boolean enableOffhandManagement = false;
    
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int respawnDelayTicks = 20;
    
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int cooldownTicks = 10;
    
    public enum ActivationMode implements SelectionListEntry.Translatable {
        MANUAL_ONLY,
        RESPAWN_ONLY,
        PICKUP_ONLY,
        ALL_EVENTS;
        
        @Override
        public String getKey() {
            return "text.autoconfig.loadout.option.activationMode." + this.name().toLowerCase();
        }
    }
    
    public Screen getConfigScreen(Screen parent) {
        return LoadoutConfigScreen.create(parent);
    }
}