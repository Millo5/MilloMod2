package millo.millomod2.client.mixin.core;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.addons.ContainerMod;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MClientPlayerInteractionManager {

    @Inject(method = "handleContainerInput", at = @At("HEAD"), cancellable = true)
    private void clickSlot(int containerId, int slotNum, int buttonNum, ContainerInput containerInput, Player player, CallbackInfo ci) {
        AbstractContainerMenu screenHandler = player.containerMenu;
        if (containerId != screenHandler.containerId) return;

        for (ContainerMod mod : FeatureHandler.getFeaturesOf(ContainerMod.class)) {
            if (mod.containerSlotClick(slotNum, buttonNum, containerInput, player)) {
                ci.cancel();
                return;
            }
        }
    }


}
