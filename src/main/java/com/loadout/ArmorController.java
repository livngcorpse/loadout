package com.loadout;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
// PlayerInventory is accessed through player.getInventory()
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class ArmorController {
    private final SlotProfile[] armorProfiles;
    
    public ArmorController() {
        // Initialize 4 slot profiles for armor (head, chest, legs, feet)
        armorProfiles = new SlotProfile[4];
        for (int i = 0; i < 4; i++) {
            armorProfiles[i] = new SlotProfile(i);
        }
    }
    
    /**
     * Equips armor based on the configured slot profiles
     * @param player The player whose armor to equip
     */
    public void equipArmor(ClientPlayerEntity player) {
        // Check if it's safe to organize the loadout
        if (!SafetyChecker.isSafeToOrganize(player)) {
            return;
        }
        
        PlayerInventory inventory = player.getInventory();
        
        // Process each armor slot
        for (int i = 0; i < 4; i++) {
            EquipmentSlot equipmentSlot = getEquipmentSlotByIndex(i);
            SlotProfile profile = armorProfiles[i];
            
            // Skip locked slots
            if (profile.isLocked()) {
                continue;
            }
            
            // Get current armor in the slot
            ItemStack currentArmor = inventory.getArmorStack(equipmentSlot.getEntitySlotId());
            
            // Scan inventory for matching armor items
            List<ItemStack> matchingArmor = InventoryScanner.scanInventory(inventory, profile);
            
            // Filter to only armor items that fit this slot
            matchingArmor.removeIf(stack -> !isArmorForSlot(stack, equipmentSlot));
            
            // If single item enforcement is enabled, filter out armor types already equipped
            if (profile.isEnforceSingleItem()) {
                filterArmorTypesAlreadyEquipped(matchingArmor, inventory);
            }
            
            // Rank armor based on profile criteria
            matchingArmor = ItemEvaluator.rankItems(matchingArmor, profile);
            
            // If we found matching armor, equip the best one
            if (!matchingArmor.isEmpty()) {
                ItemStack bestArmor = matchingArmor.get(0);
                // Find the slot where this armor is currently located
                int sourceSlot = findItemSlot(inventory, bestArmor);
                if (sourceSlot != -1) {
                    // Calculate the target armor slot index for screen handler
                    // Armor slots in screen handler are indexed after main inventory and hotbar
                    int hotbarSize = 9;
                    int mainInventorySize = inventory.main.size() - hotbarSize;
                    int targetSlot = hotbarSize + mainInventorySize + equipmentSlot.getEntitySlotId();
                    
                    // Move the best armor to the armor slot
                    ItemSwapper.moveItem(player, sourceSlot, targetSlot);
                    
                    // If there was armor in the slot, move it to an empty slot
                    if (!currentArmor.isEmpty()) {
                        int emptySlot = ItemSwapper.findEmptySlot(inventory);
                        if (emptySlot != -1) {
                            ItemSwapper.moveItem(player, targetSlot, emptySlot);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Filters out armor types that are already equipped in other slots
     * @param armorItems The list of armor items to filter
     * @param inventory The player's inventory
     */
    private void filterArmorTypesAlreadyEquipped(List<ItemStack> armorItems, PlayerInventory inventory) {
        // Create a set of armor types that are already equipped
        Set<String> equippedArmorTypes = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            ItemStack armorStack = inventory.getArmorStack(i);
            if (!armorStack.isEmpty() && armorStack.getItem() instanceof ArmorItem) {
                ArmorItem armorItem = (ArmorItem) armorStack.getItem();
                Identifier itemId = Registries.ITEM.getId(armorItem);
                equippedArmorTypes.add(itemId.toString());
            }
        }
        
        // Remove armor items that are already equipped
        armorItems.removeIf(stack -> {
            if (stack.getItem() instanceof ArmorItem) {
                Identifier itemId = Registries.ITEM.getId(stack.getItem());
                return equippedArmorTypes.contains(itemId.toString());
            }
            return false;
        });
    }
    
    /**
     * Finds the slot containing a specific item stack
     * @param inventory The player's inventory
     * @param targetItem The item stack to find
     * @return The slot index, or -1 if not found
     */
    private int findItemSlot(PlayerInventory inventory, ItemStack targetItem) {
        // Check main inventory including hotbar
        for (int i = 0; i < inventory.main.size(); i++) {
            ItemStack stack = inventory.main.get(i);
            if (ItemStack.areEqual(stack, targetItem)) {
                return i;
            }
        }
        
        // Check offhand
        ItemStack offhandStack = inventory.offHand.get(0);
        if (ItemStack.areEqual(offhandStack, targetItem)) {
            return PlayerInventory.OFF_HAND_SLOT;
        }
        
        // Check armor slots
        for (int i = 0; i < inventory.armor.size(); i++) {
            ItemStack armorStack = inventory.armor.get(i);
            if (ItemStack.areEqual(armorStack, targetItem)) {
                // Armor slots in screen handler are indexed after main inventory and hotbar
                int hotbarSize = 9;
                int mainInventorySize = inventory.main.size() - hotbarSize;
                return hotbarSize + mainInventorySize + i;
            }
        }
        
        return -1; // Not found
    }
    
    /**
     * Gets the equipment slot for a given index
     * @param index The armor slot index (0-3)
     * @return The corresponding EquipmentSlot
     */
    private EquipmentSlot getEquipmentSlotByIndex(int index) {
        switch (index) {
            case 0: return EquipmentSlot.HEAD;
            case 1: return EquipmentSlot.CHEST;
            case 2: return EquipmentSlot.LEGS;
            case 3: return EquipmentSlot.FEET;
            default: throw new IllegalArgumentException("Invalid armor slot index: " + index);
        }
    }
    
    /**
     * Checks if an item is armor for the specified equipment slot
     * @param stack The item stack to check
     * @param slot The equipment slot to check against
     * @return True if the item is armor for that slot, false otherwise
     */
    private boolean isArmorForSlot(ItemStack stack, EquipmentSlot slot) {
        Item item = stack.getItem();
        if (item instanceof ArmorItem) {
            return ((ArmorItem) item).getSlotType() == slot;
        }
        return false;
    }
    
    /**
     * Gets the slot profile for a specific armor slot
     * @param slotIndex The index of the armor slot (0-3)
     * @return The slot profile for that slot
     */
    public SlotProfile getArmorProfile(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= 4) {
            throw new IllegalArgumentException("Armor slot index must be between 0 and 3");
        }
        return armorProfiles[slotIndex];
    }
    
    /**
     * Sets the slot profile for a specific armor slot
     * @param slotIndex The index of the armor slot (0-3)
     * @param profile The slot profile to set
     */
    public void setArmorProfile(int slotIndex, SlotProfile profile) {
        if (slotIndex < 0 || slotIndex >= 4) {
            throw new IllegalArgumentException("Armor slot index must be between 0 and 3");
        }
        armorProfiles[slotIndex] = profile;
    }
}