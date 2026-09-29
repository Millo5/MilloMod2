package millo.millomod2.client.features.addons;

import millo.millomod2.client.util.RenderInfo;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public interface ContainerMod {

    default <T extends AbstractContainerMenu> void containerRender(T handler, RenderInfo info) {}

    default <T extends AbstractContainerMenu> void containerInit(AbstractContainerScreen<T> handledScreen, CallbackInfo ci) {}

    default void containerTick(CallbackInfo ci) {}

    default void containerMouseClicked(MouseButtonEvent click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {}

    default void containerMouseReleased(MouseButtonEvent click, CallbackInfoReturnable<Boolean> cir) {}

    default <T extends AbstractContainerMenu> void containerKeyPressed(T handler, KeyEvent input, CallbackInfoReturnable<Boolean> cir) {}

    default void containerClose(CallbackInfo ci) {}

    default void containerDrawSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {}

    default boolean containerSlotClick(int slotId, int button, ContainerInput actionType, Player player) { return false; }

}

