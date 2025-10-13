package millo.millomod2.client.features.addons;

import millo.millomod2.client.util.RenderInfo;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface ContainerMod {

    default <T extends ScreenHandler> void containerRender(T handler, RenderInfo info) {}

    default <T extends ScreenHandler> void containerInit(HandledScreen<T> handledScreen, CallbackInfo ci) {}

    default void containerTick(CallbackInfo ci) {}

    default void containerMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {}

    default void containerKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {}

    default void containerClose(CallbackInfo ci) {}

    default void containerDrawSlot(DrawContext context, Slot slot, CallbackInfo ci) {}

}
