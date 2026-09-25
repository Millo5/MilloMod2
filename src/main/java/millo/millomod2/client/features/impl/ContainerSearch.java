package millo.millomod2.client.features.impl;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.config.FeatureConfig;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.addons.Configurable;
import millo.millomod2.client.features.addons.ContainerMod;
import millo.millomod2.client.features.addons.Toggleable;
import millo.millomod2.client.mixin.render.accessors.HandledScreenAccessor;
import millo.millomod2.client.mixin.render.accessors.ScreenAccessor;
import millo.millomod2.client.util.RenderInfo;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

public class ContainerSearch extends Feature implements Toggleable, Configurable, ContainerMod {
    private EditBox searchBox;

    private int xOffset, yOffset;

    @Override
    public String getId() {
        return "container_search";
    }

    @Override
    public void setupConfig(FeatureConfig config) {
        config.addBoolean("always_show", false);
        config.addBoolean("enter_click", true);
    }

    private boolean isShown() {
        return searchBox != null && searchBox.isVisible();
    }

    @Override
    public <T extends AbstractContainerMenu> void containerInit(AbstractContainerScreen<T> handledScreen, CallbackInfo ci) {
        searchBox = null;
        if (!isEnabled()) return;

        if (!(handledScreen.getMenu() instanceof ChestMenu containerHandler)) return;
        if (!(containerHandler.getContainer() instanceof SimpleContainer)) return;

        ScreenAccessor screen = (ScreenAccessor) handledScreen;
        HandledScreenAccessor container = (HandledScreenAccessor) handledScreen;

        int searchBoxWidth = 96;
        searchBox = new EditBox(MilloMod.MC.font,
                container.getBackgroundWidth() - searchBoxWidth - 8,
                -16,
                searchBoxWidth,
                16,
                Component.literal(""));
        searchBox.setHint(Component.literal("Search.."));
        searchBox.setVisible(false);

        xOffset = container.getX();
        yOffset = container.getY();

        if (config.getBoolean("always_show")) {
            showSearchBox(screen);
        }
    }

    @Override
    public void containerMouseClicked(MouseButtonEvent click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        if (!isEnabled()) return;
        if (!isShown()) return;

        MouseButtonEvent relativeClick = new MouseButtonEvent(
                click.x() - xOffset,
                click.y() - yOffset,
                click.buttonInfo()
        );

        if (searchBox.mouseClicked(relativeClick, doubled)) {
            focusSearchBox();
            cir.setReturnValue(true);
        }
    }

    private void showSearchBox(ScreenAccessor screen) {
        if (!isShown()) {
            screen.iAddSelectableChild(searchBox);
            searchBox.setVisible(true);
        }
    }

    @Override
    public <T extends AbstractContainerMenu> void containerRender(T handler, RenderInfo info) {
        if (!isEnabled() || searchBox == null) return;
        boolean isChestScreen = handler instanceof ChestMenu containerHandler
                && containerHandler.getContainer() instanceof SimpleContainer;

        if (!isChestScreen) return;
        if (!isShown() && config.getBoolean("always_show")) {
            showSearchBox((MilloMod.MC.screen instanceof ScreenAccessor screen) ? screen : null);
        }

        if (!isShown()) return;
        searchBox.render(info.context(), info.mouseX(), info.mouseY(), info.deltaTime());
    }

    @Override
    public void containerDrawSlot(GuiGraphics context, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        if (!isEnabled() || !isShown()) return;

        String searchTerm = searchBox.getValue().trim();
        if (searchTerm.isEmpty()) return;

        final int color;
        String itemName = slot.getItem().getHoverName().getString().toLowerCase();
        String[] searchTerms = searchTerm.toLowerCase().split(" ");
        if (Arrays.stream(searchTerms).allMatch(itemName::contains)) color = 0x40ffffff;
        else color = 0x80000000;

        context.fillGradient(slot.x, slot.y, slot.x + 16, slot.y + 16, color, color);
    }

    @Override
    public <T extends AbstractContainerMenu> void containerKeyPressed(T handler, KeyEvent input, CallbackInfoReturnable<Boolean> cir) {
        if (!isEnabled()) return;
        if (MilloMod.MC.screen == null) return;
        if (searchBox == null) return;

        int keyCode = input.key();

        if (input.modifiers() == 2 && keyCode == 70) {
            focusSearchBox();
            cir.cancel();
        }

        if (!isShown()) return;

        if (searchBox.isFocused()) {
            searchBox.keyPressed(input);
            cir.setReturnValue(true);

            if (keyCode == 257 && config.getBoolean("enter_click")) {
                String searchTerm = searchBox.getValue().trim();
                if (searchTerm.isEmpty()) return;
                searchBox.setFocused(false);

                String[] searchTerms = searchTerm.toLowerCase().split(" ");

                if (MilloMod.MC.gameMode == null) return;

                for (Slot slot : handler.slots) {
                    String itemName = slot.getItem().getHoverName().getString().toLowerCase();
                    if (Arrays.stream(searchTerms).allMatch(itemName::contains)) {
                        MilloMod.MC.gameMode.handleInventoryMouseClick(handler.containerId, slot.index, 0, ClickType.PICKUP, MilloMod.MC.player);
                        break;
                    }
                }
            }
        }

        if (keyCode == 257 || keyCode == 256) {
            searchBox.setFocused(false);
        }
    }

    private void focusSearchBox() {
        if (searchBox == null) return;

        showSearchBox((MilloMod.MC.screen instanceof ScreenAccessor screen) ? screen : null);

        searchBox.setEditable(true);
        searchBox.setCursorPosition(0);
        searchBox.setHighlightPos(searchBox.getValue().length());
        searchBox.setFocused(true);

        if (MilloMod.MC.screen != null) MilloMod.MC.screen.setFocused(searchBox);
    }

    @Override
    public void containerClose(CallbackInfo ci) {
        if (!isEnabled()) return;
        if (!isShown()) return;

        searchBox.setValue("");
        searchBox.setFocused(false);
        searchBox.setVisible(false);
        searchBox = null;
    }
}
