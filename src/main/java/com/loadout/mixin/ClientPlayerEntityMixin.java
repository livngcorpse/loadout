package com.loadout.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    
    @Inject(method = "attack", at = @At("HEAD"))
    private void loadout$attack(CallbackInfo ci) {
        loadout$lastAttackedTicks = 20; // 1 second cooldown
    }
    
    @Inject(method = "getItemUseCooldown", at = @At("RETURN"), cancellable = true)
    private void loadout$getItemUseCooldown(CallbackInfoReturnable<Integer> cir) {
        // This injection might not work as intended, but it's an example of how we could extend functionality
    }
}