package millo.millomod2.client.mixin.network;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayNetworkHandler.class)
public class MClientPlayNetworkHandler {

    @Redirect(method = "onGameJoin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/ToastManager;add(Lnet/minecraft/client/toast/Toast;)V"))
    private void onGameJoinSuppressToast(net.minecraft.client.toast.ToastManager instance, net.minecraft.client.toast.Toast toast) {
        // Suppress the "Joined the game" toast by doing nothing
    }

}
