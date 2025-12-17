package com.loadout;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class SafetyChecker {
    
    /**
     * Checks if the player is currently in combat
     * @param player The player to check
     * @return true if the player is in combat, false otherwise
     */
    public static boolean isInCombat(ClientPlayerEntity player) {
        // Check if the player has recently attacked an entity
        // Using our custom mixin to track attack cooldown
        // if (player.getLastAttackedTicks() < 20) { // Within last second
        //     return true;
        // }
        
        // Check if the player is being attacked
        if (player.hurtTime > 0) {
            return true;
        }
        
        // Check if the player is targeting an entity that can be attacked
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHit = (EntityHitResult) client.crosshairTarget;
            Entity target = entityHit.getEntity();
            if (target instanceof LivingEntity && target.isAlive()) {
                // Check if the target is close enough to be a threat
                double distance = player.distanceTo(target);
                if (distance < 4.0) { // Within 4 blocks
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Checks if the player is currently using an item
     * @param player The player to check
     * @return true if the player is using an item, false otherwise
     */
    public static boolean isUsingItem(ClientPlayerEntity player) {
        return player.isUsingItem();
    }
    
    /**
     * Checks if it's safe to organize the player's loadout
     * @param player The player to check
     * @return true if it's safe to organize the loadout, false otherwise
     */
    public static boolean isSafeToOrganize(ClientPlayerEntity player) {
        // Check if the player is in combat
        if (isInCombat(player)) {
            return false;
        }
        
        // Check if the player is using an item
        if (isUsingItem(player)) {
            return false;
        }
        
        // Check if the player is dead or in an invalid state
        if (player.isDead() || !player.isAlive()) {
            return false;
        }
        
        // Check if the player is in creative mode (optional - might want to skip organization)
        if (player.isCreative()) {
            // Could optionally return false here if we don't want to organize in creative mode
        }
        
        // Check if the player is in spectator mode
        if (player.isSpectator()) {
            return false;
        }
        
        return true;
    }
}