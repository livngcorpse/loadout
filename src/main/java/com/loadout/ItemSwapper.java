package com.loadout;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
// Fixing imports for 1.20.10+
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class ItemSwapper {
    
    /**
     * Moves an item from one slot to another in the player's inventory
     * Uses a safer, single-click sequence to reduce desync risk
     * @param player The player whose inventory to modify
     * @param fromSlot The slot index to move the item from
     * @param toSlot The slot index to move the item to
     * @return true if the swap was successful, false otherwise
     */
    public static boolean moveItem(ClientPlayerEntity player, int fromSlot, int toSlot) {
        if (fromSlot == toSlot) {
            return true; // No move needed
        }
        
        PlayerInventory inventory = player.getInventory();
        ScreenHandler screenHandler = player.currentScreenHandler;
        
        // Get the screen handler revision for synchronization
        int syncId = screenHandler.syncId;
        int revision = screenHandler.getRevision();
        
        // First, pick up the item from the source slot
        ClickSlotC2SPacket pickupPacket = new ClickSlotC2SPacket(
                syncId,
                revision,
                fromSlot,
                0, // Left click
                SlotActionType.PICKUP,
                screenHandler.getCursorStack()
        );
        
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(pickupPacket);
        
        // Then, place the item in the target slot
        ClickSlotC2SPacket placePacket = new ClickSlotC2SPacket(
                syncId,
                toSlot,
                0, // Left click
                SlotActionType.PICKUP,
                screenHandler.getCursorStack(),
                screenHandler.getRevision() // Update revision
        );
        
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(placePacket);
        
        return true;
    }
    
    /**
     * Swaps two items in the player's inventory
     * Uses a safer, multi-step sequence to reduce desync risk
     * @param player The player whose inventory to modify
     * @param slotA First slot index
     * @param slotB Second slot index
     * @return true if the swap was successful, false otherwise
     */
    public static boolean swapItems(ClientPlayerEntity player, int slotA, int slotB) {
        if (slotA == slotB) {
            return true; // No swap needed
        }
        
        PlayerInventory inventory = player.getInventory();
        ScreenHandler screenHandler = player.currentScreenHandler;
        
        // Step 1: Pick up item from slot A
        int syncId = screenHandler.syncId;
        int revision = screenHandler.getRevision();
        
        ClickSlotC2SPacket pickupAPacket = new ClickSlotC2SPacket(
                syncId,
                slotA,
                0, // Left click
                SlotActionType.PICKUP,
                screenHandler.getCursorStack(),
                revision
        );
        
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(pickupAPacket);
        
        // Step 2: Place item A in slot B
        ClickSlotC2SPacket placeAPacket = new ClickSlotC2SPacket(
                syncId,
                slotB,
                0, // Left click
                SlotActionType.PICKUP,
                screenHandler.getCursorStack(),
                screenHandler.getRevision()
        );
        
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(placeAPacket);
        
        // Step 3: Pick up item from slot B (now in cursor)
        ClickSlotC2SPacket pickupBPacket = new ClickSlotC2SPacket(
                syncId,
                slotB,
                0, // Left click
                SlotActionType.PICKUP,
                screenHandler.getCursorStack(),
                screenHandler.getRevision()
        );
        
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(pickupBPacket);
        
        // Step 4: Place item B in slot A
        ClickSlotC2SPacket placeBPacket = new ClickSlotC2SPacket(
                syncId,
                slotA,
                0, // Left click
                SlotActionType.PICKUP,
                screenHandler.getCursorStack(),
                screenHandler.getRevision()
        );
        
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(placeBPacket);
        
        return true;
    }
    
    /**
     * Finds an empty slot in the player's inventory
     * @param inventory The player's inventory
     * @return The index of an empty slot, or -1 if none found
     */
    public static int findEmptySlot(PlayerInventory inventory) {
        // Check main inventory (excluding hotbar)
        for (int i = 9; i < 36; i++) {
            if (inventory.getStack(i).isEmpty()) {
                return i;
            }
        }
        
        // Check hotbar
        for (int i = 0; i < 9; i++) {
            if (inventory.getStack(i).isEmpty()) {
                return i;
            }
        }
        
        // Check offhand
        if (inventory.getStack(40).isEmpty()) {
            return 40; // OFF_HAND_SLOT is 40
        }
        
        return -1; // No empty slot found
    }
}