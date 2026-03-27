package millo.millomod2.client.mixin.network;

import millo.millomod2.client.features.impl.DevMovement.DevMovement;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MPlayerEntity {

    @Inject(method = "getOffGroundSpeed", at = @At("HEAD"), cancellable = true)
    private void getOffGroundSpeed(CallbackInfoReturnable<Float> cir) {
        Float spd = DevMovement.getInstance().getOffGroundSpeed();
        if (spd != null) {
            cir.setReturnValue(spd);
        }
    }


    @Inject(method = "canChangeIntoPose", at = @At("HEAD"), cancellable = true)
    private void canChangeIntoPose(EntityPose pose, CallbackInfoReturnable<Boolean> cir) {
        if (DevMovement.getInstance().isNoClipping()) cir.setReturnValue(true);
    }

}
