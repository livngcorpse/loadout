package com.loadout;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents; // Changed from server to client event
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerPickupItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public class EventListener {
    private static final int COOLDOWN_TICKS = 10;
    
    private HotbarController hotbarController;
    private ArmorController armorController;
    private OffhandController offhandController;
    
    private int respawnTimer = 0;
    private int cooldownTimer = 0;
    
    public EventListener() {
        this.hotbarController = new HotbarController();
        this.armorController = new ArmorController();
        this.offhandController = new OffhandController();
    }
    
    /**
     * Registers all event listeners
     */
    public void registerEvents() {
        // Register client tick event for timers
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            onClientTick(client);
        });
        
        // Register respawn event
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            // Set timer to organize loadout after initial join/spawn
            respawnTimer = AutoConfig.getConfigHolder(LoadoutConfig.class).getConfig().respawnDelayTicks;
        });
        
        // Register item pickup event
        PlayerPickupItemCallback.EVENT.register((player, itemStack, unused) -> {
            if (player instanceof ClientPlayerEntity) {
                // Trigger loadout organization (debounced)
                triggerLoadoutUpdate();
            }
            return ActionResult.PASS;
        });
    }
    
    /**
     * Called each client tick to handle timers
     */
    public void onClientTick(MinecraftClient client) {
        // Handle respawn timer
        if (respawnTimer > 0) {
            respawnTimer--;
            if (respawnTimer == 0 && client.player != null) {
                organizeLoadout(client.player);
            }
        }
        
        // Handle cooldown timer
        if (cooldownTimer > 0) {
            cooldownTimer--;
        }
    }
    
    /**
     * Triggers a loadout update with debouncing
     */
    private void triggerLoadoutUpdate() {
        // Only trigger if cooldown has expired
        if (cooldownTimer <= 0) {
            cooldownTimer = COOLDOWN_TICKS;
            // Trigger loadout organization for the current player
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                organizeLoadout(client.player);
            }
        }
    }
    
    /**
     * Organizes the player's loadout (hotbar, armor, offhand)
     * @param player The player whose loadout to organize
     */
    private void organizeLoadout(ClientPlayerEntity player) {
        // Organize hotbar
        hotbarController.organizeHotbar(player);
        
        // Equip armor
        armorController.equipArmor(player);
        
        // Set offhand item
        offhandController.setOffhandItem(player);
    }
}