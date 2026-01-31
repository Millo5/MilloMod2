package millo.millomod2.client.mixin.core;

import millo.millomod2.client.features.impl.DevMovement;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MLivingEntity {

    @Inject(method = "getJumpVelocity()F", at = @At("HEAD"), cancellable = true)
    private void getJumpVelocity(CallbackInfoReturnable<Float> cir) {
        var vel = DevMovement.getInstance().getJumpVelocity();
        if (vel != null) {
            cir.setReturnValue(vel);
        }
    }

}
