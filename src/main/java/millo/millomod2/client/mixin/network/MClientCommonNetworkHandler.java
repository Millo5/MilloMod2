package millo.millomod2.client.mixin.network;

import millo.millomod2.client.features.FeatureHandler;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonPacketListenerImpl.class)
public class MClientCommonNetworkHandler {

    @Inject(method = "send", at = @At("HEAD"), cancellable = true)
    private void sendPacket(Packet<?> packet, CallbackInfo ci) {
        if (FeatureHandler.getPacketEventBus().postSend(packet)) ci.cancel();
    }

}
