package millo.millomod2.client.mixin.core;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.addons.MouseScrollable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(net.minecraft.client.Mouse.class)
public class MMouse {

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    public void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        FeatureHandler.forEach(f -> {
            if (f instanceof MouseScrollable scrollable) if (scrollable.onScroll(vertical)) ci.cancel();
        });
    }

}