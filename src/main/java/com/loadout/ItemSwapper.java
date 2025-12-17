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
        
        // Send packet to move item
        int revision = screenHandler.getRevision();
        ItemStack carriedItem = inventory.getStack(fromSlot).copy();
        
        // Send click packet to pick up the item
        ClickSlotC2SPacket pickupPacket = new ClickSlotC2SPacket(
                screenHandler.syncId,
                revision,
                fromSlot,
                0, // Left click
                SlotActionType.PICKUP,
                carriedItem,
                screenHandler.getCreativeStack()
        );
        
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(pickupPacket);
        
        // Send click packet to place the item
        revision = screenHandler.getRevision();
        ClickSlotC2SPacket placePacket = new ClickSlotC2SPacket(
                screenHandler.syncId,
                revision,
                toSlot,
                0, // Left click
                SlotActionType.PICKUP,
                ItemStack.EMPTY,
                screenHandler.getCreativeStack()
        );
        
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(placePacket);
        
        return true;
    }
    
    /**
     * Swaps two items in the player's inventory
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
        
        // Get the items to swap
        ItemStack itemA = inventory.getStack(slotA).copy();
        ItemStack itemB = inventory.getStack(slotB).copy();
        
        // Move item A to cursor
        int revision = screenHandler.getRevision();
        ClickSlotC2SPacket pickupAPacket = new ClickSlotC2SPacket(
                screenHandler.syncId,
                revision,
                slotA,
                0, // Left click
                SlotActionType.PICKUP,
                itemA,
                screenHandler.getCreativeStack()
        );
        
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(pickupAPacket);
        
        // Move item A to slot B
        revision = screenHandler.getRevision();
        ClickSlotC2SPacket placeAPacket = new ClickSlotC2SPacket(
                screenHandler.syncId,
                revision,
                slotB,
                0, // Left click
                SlotActionType.PICKUP,
                itemB,
                screenHandler.getCreativeStack()
        );
        
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(placeAPacket);
        
        // Move item B to cursor
        revision = screenHandler.getRevision();
        ClickSlotC2SPacket pickupBPacket = new ClickSlotC2SPacket(
                screenHandler.syncId,
                revision,
                slotB,
                0, // Left click
                SlotActionType.PICKUP,
                itemB,
                screenHandler.getCreativeStack()
        );
        
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(pickupBPacket);
        
        // Move item B to slot A
        revision = screenHandler.getRevision();
        ClickSlotC2SPacket placeBPacket = new ClickSlotC2SPacket(
                screenHandler.syncId,
                revision,
                slotA,
                0, // Left click
                SlotActionType.PICKUP,
                ItemStack.EMPTY,
                screenHandler.getCreativeStack()
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
        for (int i = 9; i < inventory.main.size(); i++) {
            if (inventory.main.get(i).isEmpty()) {
                return i;
            }
        }
        
        // Check hotbar
        for (int i = 0; i < 9; i++) {
            if (inventory.main.get(i).isEmpty()) {
                return i;
            }
        }
        
        // Check offhand
        if (inventory.offHand.get(0).isEmpty()) {
            return PlayerInventory.OFF_HAND_SLOT;
        }
        
        return -1; // No empty slot found
    }
}