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
    private static HotbarController hotbarController;
    private static ArmorController armorController;
    private static OffhandController offhandController;
    private static EventListener eventListener;
    
    @Override
    public void onInitializeClient() {
        // Register the config
        AutoConfig.register(LoadoutConfig.class, GsonConfigSerializer::new);
        
        // Initialize controllers
        hotbarController = new HotbarController();
        armorController = new ArmorController();
        offhandController = new OffhandController();
        eventListener = new EventListener();
        
        // Load slot profiles from config
        loadSlotProfiles();
        
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
    
    /**
     * Loads slot profiles from the configuration
     */
    private static void loadSlotProfiles() {
        LoadoutConfig config = AutoConfig.getConfigHolder(LoadoutConfig.class).getConfig();
        
        // Load hotbar slot profiles
        for (int i = 0; i < Math.min(9, config.hotbarSlots.size()); i++) {
            LoadoutConfig.SlotProfileData data = config.hotbarSlots.get(i);
            SlotProfile profile = hotbarController.getSlotProfile(i);
            data.applyTo(profile);
        }
        
        // Load armor slot profiles
        for (int i = 0; i < Math.min(4, config.armorSlots.size()); i++) {
            LoadoutConfig.SlotProfileData data = config.armorSlots.get(i);
            SlotProfile profile = armorController.getArmorProfile(i);
            data.applyTo(profile);
        }
        
        // Load offhand slot profile
        if (config.offhandSlot != null) {
            config.offhandSlot.applyTo(offhandController.getOffhandProfile());
        }
    }
    
    /**
     * Saves slot profiles to the configuration
     */
    public static void saveSlotProfiles() {
        LoadoutConfig config = AutoConfig.getConfigHolder(LoadoutConfig.class).getConfig();
        
        // Save hotbar slot profiles
        config.hotbarSlots.clear();
        for (int i = 0; i < 9; i++) {
            SlotProfile profile = hotbarController.getSlotProfile(i);
            config.hotbarSlots.add(new LoadoutConfig.SlotProfileData(profile));
        }
        
        // Save armor slot profiles
        config.armorSlots.clear();
        for (int i = 0; i < 4; i++) {
            SlotProfile profile = armorController.getArmorProfile(i);
            config.armorSlots.add(new LoadoutConfig.SlotProfileData(profile));
        }
        
        // Save offhand slot profile
        config.offhandSlot = new LoadoutConfig.SlotProfileData(offhandController.getOffhandProfile());
        
        // Save the config
        AutoConfig.getConfigHolder(LoadoutConfig.class).save();
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