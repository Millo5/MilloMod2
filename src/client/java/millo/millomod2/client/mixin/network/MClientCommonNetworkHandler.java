package millo.millomod2.client.mixin.network;

import millo.millomod2.client.features.FeatureHandler;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonNetworkHandler.class)
public class MClientCommonNetworkHandler {

    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    private void sendPacket(Packet<?> packet, CallbackInfo ci) {
        if (FeatureHandler.getPacketHandler().onSendPacket(packet)) ci.cancel();
    }

}
