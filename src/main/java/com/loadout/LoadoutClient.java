package com.loadout;

import com.loadout.ui.LoadoutConfigScreen;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class LoadoutClient implements ClientModInitializer {
    public static final String MOD_ID = "loadout";
    
    private static KeyBinding reloadLoadoutKey;
    private static EventListener eventListener;
    private static LoadoutConfig config;
    
    @Override
    public void onInitializeClient() {
        // Register the config
        AutoConfig.register(LoadoutConfig.class, GsonConfigSerializer::new);
        
        // Initialize event listener
        eventListener = new EventListener();
        
        // Load config
        config = AutoConfig.getConfigHolder(LoadoutConfig.class).get();
        
        // Register keybindings
        reloadLoadoutKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.loadout.reload_loadout",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN, // No default keybind
                "category.loadout.loadout"
        ));
        
        // Register tick events
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (reloadLoadoutKey.wasPressed()) {
                // Handle manual loadout reload - in the new system this is just for debugging
                System.out.println("Manual reload triggered, but not implemented in new system");
            }
        });
        
        // Register events
        eventListener.registerEvents();
        
        System.out.println("Loadout mod initialized with new routing system!");
    }
    
    /**
     * Gets the current config
     */
    public static LoadoutConfig getConfig() {
        return config;
    }
    
    /**
     * Saves slot routing configs to the configuration
     */
    public static void saveSlotRoutingConfigs(SlotRoutingConfig[] configs) {
        LoadoutConfig config = AutoConfig.getConfigHolder(LoadoutConfig.class).getConfig();
        
        // Update the config with the new routing configs
        config.updateFromSlotRoutingConfigs(configs);
        
        // Save the config
        AutoConfig.getConfigHolder(LoadoutConfig.class).save();
    }
    
    public static EventListener getEventListener() {
        return eventListener;
    }
}