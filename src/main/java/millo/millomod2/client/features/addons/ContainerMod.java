package millo.millomod2.client.features.addons;

import millo.millomod2.client.util.RenderInfo;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface ContainerMod {

    default <T extends ScreenHandler> void containerRender(T handler, RenderInfo info) {}

    default <T extends ScreenHandler> void containerInit(HandledScreen<T> handledScreen, CallbackInfo ci) {}

    default void containerTick(CallbackInfo ci) {}

    default void containerMouseClicked(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {}

    default void containerMouseReleased(Click click, CallbackInfoReturnable<Boolean> cir) {}

    default <T extends ScreenHandler> void containerKeyPressed(T handler, KeyInput input, CallbackInfoReturnable<Boolean> cir) {}

    default void containerClose(CallbackInfo ci) {}

    default void containerDrawSlot(DrawContext context, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {}

    default boolean containerSlotClick(int slotId, int button, SlotActionType actionType, PlayerEntity player) { return false; }

}

