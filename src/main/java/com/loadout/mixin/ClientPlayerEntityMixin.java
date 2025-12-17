package com.loadout.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Unique
    private int loadout$lastAttackedTicks = 0;
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void loadout$tick(CallbackInfo ci) {
        if (loadout$lastAttackedTicks > 0) {
            loadout$lastAttackedTicks--;
        }
    }
    
    @Inject(method = "swingHand", at = @At("HEAD"))
    private void loadout$swingHand(CallbackInfo ci) {
        loadout$lastAttackedTicks = 20; // 1 second cooldown
    }
    
    @Inject(method = "closeHandledScreen", at = @At("HEAD"))
    private void loadout$closeHandledScreen(CallbackInfo ci) {
        // This injection might not work as intended, but it's an example of how we could extend functionality
    }
    
    // Add a getter method for accessing the field from our code
    @Unique
    public int loadout$getLastAttackedTicks() {
        return loadout$lastAttackedTicks;
    }
}