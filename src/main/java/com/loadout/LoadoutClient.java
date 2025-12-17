package com.loadout;

import com.loadout.ui.LoadoutConfigScreen;
import me.shedaniel.autoconfig.AutoConfig;
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
    private static HotbarController hotbarController;
    private static ArmorController armorController;
    private static OffhandController offhandController;
    private static EventListener eventListener;
    
    @Override
    public void onInitializeClient() {
        // Initialize controllers
        hotbarController = new HotbarController();
        armorController = new ArmorController();
        offhandController = new OffhandController();
        eventListener = new EventListener();
        
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
                // Handle manual loadout reload
                if (client.player != null) {
                    // Organize the player's loadout
                    hotbarController.organizeHotbar(client.player);
                    armorController.equipArmor(client.player);
                    offhandController.setOffhandItem(client.player);
                }
            }
            
            // Handle event listener timers
            eventListener.onClientTick(client);
        });
        
        // Register events
        eventListener.registerEvents();
        
        System.out.println("Loadout mod initialized!");
    }
    
    public static HotbarController getHotbarController() {
        return hotbarController;
    }
    
    public static ArmorController getArmorController() {
        return armorController;
    }
    
    public static OffhandController getOffhandController() {
        return offhandController;
    }
}