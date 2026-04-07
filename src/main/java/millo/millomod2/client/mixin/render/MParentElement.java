package millo.millomod2.client.mixin.render;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.addons.UICharTyped;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.input.CharInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParentElement.class)
public interface MParentElement {

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void charTyped(CharInput input, CallbackInfoReturnable<Boolean> cir) {
        for (UICharTyped uiCharTyped : FeatureHandler.getFeaturesOf(UICharTyped.class)) {
            if (uiCharTyped.charTyped(input)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }


}
