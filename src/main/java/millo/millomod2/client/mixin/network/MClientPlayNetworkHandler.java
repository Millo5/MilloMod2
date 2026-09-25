package millo.millomod2.client.mixin.network;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.addons.ChatSendInjector;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MClientPlayNetworkHandler {


    @Inject(method = "sendChat", at = @At(value = "HEAD"), cancellable = true)
    private void sendChatMessage(String content, CallbackInfo ci) {
        FeatureHandler.forEach(f -> {
            if (f instanceof ChatSendInjector send) if (send.onSendMessage(content)) ci.cancel();
        });
    }

    @Redirect(method = "handleLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/toasts/ToastManager;addToast(Lnet/minecraft/client/gui/components/toasts/Toast;)V"))
    private void onGameJoinSuppressToast(net.minecraft.client.gui.components.toasts.ToastManager instance, net.minecraft.client.gui.components.toasts.Toast toast) {
        // Suppress the "Joined the game" toast by doing nothing
    }

}
