package millo.millomod2.client.mixin.network;

import millo.millomod2.client.features.impl.DevMovement;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class MClientPlayerEntity {

    @Shadow
    protected abstract void sendSprintingPacket();

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void sendMovementPackets(CallbackInfo ci) {
        if (!DevMovement.getInstance().sendMovementPackets()) return;

        this.sendSprintingPacket();
        ci.cancel();
    }

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    private void pushOutOfBlocks(double x, double z, CallbackInfo ci) {
        if (DevMovement.getInstance().isNoClipping()) ci.cancel();
    }

//    @Inject(method = "shouldSlowDown", at = @At("HEAD"), cancellable = true)
//    private void shouldSlowDown(CallbackInfoReturnable<Boolean> cir) {
//        if (DevMovement.getInstance().isEnabled()) cir.cancel();
//    }

    @Inject(method = "shouldAutoJump", at = @At("HEAD"), cancellable = true)
    private void shouldAutoJump(CallbackInfoReturnable<Boolean> cir) {
        if (DevMovement.getInstance().isNoClipping()) cir.cancel();
    }

}
