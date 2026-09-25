package millo.millomod2.client.mixin.render;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.addons.UICharTyped;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.input.CharacterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerEventHandler.class)
public interface MParentElement {

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void charTyped(CharacterEvent input, CallbackInfoReturnable<Boolean> cir) {
        for (UICharTyped uiCharTyped : FeatureHandler.getFeaturesOf(UICharTyped.class)) {
            if (uiCharTyped.charTyped(input)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }


}
