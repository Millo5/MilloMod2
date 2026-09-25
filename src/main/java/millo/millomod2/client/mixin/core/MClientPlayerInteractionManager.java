package millo.millomod2.client.mixin.core;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.addons.ContainerMod;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MClientPlayerInteractionManager {

    @Inject(method = "handleInventoryMouseClick", at = @At("HEAD"), cancellable = true)
    private void clickSlot(int syncId, int slotId, int button, ClickType actionType, Player player, CallbackInfo ci) {
        AbstractContainerMenu screenHandler = player.containerMenu;
        if (syncId != screenHandler.containerId) return;

        for (ContainerMod mod : FeatureHandler.getFeaturesOf(ContainerMod.class)) {
            if (mod.containerSlotClick(slotId, button, actionType, player)) {
                ci.cancel();
                return;
            }
        }
    }


}
