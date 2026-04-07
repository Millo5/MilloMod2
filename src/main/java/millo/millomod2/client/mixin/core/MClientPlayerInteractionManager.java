package millo.millomod2.client.mixin.core;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.addons.ContainerMod;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class MClientPlayerInteractionManager {

    @Inject(method = "clickSlot", at = @At("HEAD"), cancellable = true)
    private void clickSlot(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        ScreenHandler screenHandler = player.currentScreenHandler;
        if (syncId != screenHandler.syncId) return;

        for (ContainerMod mod : FeatureHandler.getFeaturesOf(ContainerMod.class)) {
            if (mod.containerSlotClick(slotId, button, actionType, player)) {
                ci.cancel();
                return;
            }
        }
    }


}
