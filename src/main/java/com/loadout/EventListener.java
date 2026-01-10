package com.loadout;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerPickupItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public class EventListener {
    
    private ItemRouter itemRouter;
    
    public EventListener() {
        this.itemRouter = new ItemRouter();
    }
    
    /**
     * Registers all event listeners
     */
    public void registerEvents() {
        // Register item pickup event
        PlayerPickupItemCallback.EVENT.register((player, itemStack, unused) -> {
            if (player instanceof ClientPlayerEntity) {
                LoadoutConfig config = AutoConfig.getConfigHolder(LoadoutConfig.class).getConfig();
                if (config.activationMode == LoadoutConfig.ActivationMode.PICKUP_ONLY) {
                    // Route the picked up item with a small delay to ensure it's in the inventory
                    scheduleItemRouting((ClientPlayerEntity) player, itemStack);
                }
            }
            return ActionResult.PASS;
        });
    }
    
    /**
     * Schedules item routing with a small delay to ensure the item is properly in the inventory
     */
    private void scheduleItemRouting(ClientPlayerEntity player, ItemStack itemStack) {
        MinecraftClient.getInstance().execute(() -> {
            itemRouter.routeItem(player, itemStack);
        });
    }
}