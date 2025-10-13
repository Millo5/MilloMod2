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
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

public class ContainerSearch extends Feature implements Toggleable, Configurable, ContainerMod {
    private final int SEARCH_BOX_WIDTH = 96;
    private TextFieldWidget searchBox;

    private boolean shown = false;

    private int xOffset, yOffset;

    @Override
    public String getId() {
        return "container_search";
    }

    @Override
    public void setupConfig(FeatureConfig config) {
        config.addBoolean("always_show", true);
    }

    @Override
    public <T extends ScreenHandler> void containerInit(HandledScreen<T> handledScreen, CallbackInfo ci) {
        searchBox = null;
        if (!isEnabled()) return;

        if (!(handledScreen.getScreenHandler() instanceof GenericContainerScreenHandler containerHandler)) return;
        if (!(containerHandler.getInventory() instanceof SimpleInventory)) return;

        ScreenAccessor screen = (ScreenAccessor) handledScreen;
        HandledScreenAccessor container = (HandledScreenAccessor) handledScreen;

        searchBox = new TextFieldWidget(MilloMod.MC.textRenderer,
                container.getBackgroundWidth() - SEARCH_BOX_WIDTH - 8,
                -16,
                SEARCH_BOX_WIDTH,
                16,
                Text.literal(""));
        searchBox.setPlaceholder(Text.literal("Search.."));

        xOffset = container.getX();
        yOffset = container.getY();

        if (config.getBoolean("always_show")) {
            showSearchBox(screen);
        }
    }

    @Override
    public void containerMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!isEnabled()) return;
        if (searchBox == null || !shown) return;

        if (searchBox.mouseClicked(mouseX - xOffset, mouseY - yOffset, button)) {
            focusSearchBox();
            cir.setReturnValue(true);
        }
    }

    private void showSearchBox(ScreenAccessor screen) {
        if (!shown) {
            screen.iAddSelectableChild(searchBox);
        }
        shown = true;
    }

    @Override
    public <T extends ScreenHandler> void containerRender(T handler, RenderInfo info) {
        if (!isEnabled()) return;
        boolean isChestScreen = handler instanceof GenericContainerScreenHandler containerHandler
                && containerHandler.getInventory() instanceof SimpleInventory;

        if (!isChestScreen) return;
        shown = shown || config.getBoolean("always_show");

        if (!shown) return;
        searchBox.render(info.context(), info.mouseX(), info.mouseY(), info.deltaTime());
    }

    @Override
    public void containerDrawSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        if (!isEnabled() || !shown) return;

        String searchTerm = searchBox.getText().trim();
        if (searchTerm.isEmpty()) return;

        final int color;
        String itemName = slot.getStack().getName().getString().toLowerCase();
        String[] searchTerms = searchTerm.toLowerCase().split(" ");
        if (Arrays.stream(searchTerms).allMatch(itemName::contains)) color = 0x40ffffff;
        else color = 0x80000000;

        context.fillGradient(slot.x, slot.y, slot.x + 16, slot.y + 16, color, color);
    }

    @Override
    public void containerKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!isEnabled()) return;
        if (MilloMod.MC.currentScreen == null) return;
        if (searchBox == null) return;

        if (modifiers == 2 && keyCode == 70) {
            focusSearchBox();
            cir.cancel();
        }

        if (!shown) return;

        if (searchBox.isFocused()) {
            searchBox.keyPressed(keyCode, scanCode, modifiers);
            cir.setReturnValue(true);
        }

        if (keyCode == 257 || keyCode == 256) {
            searchBox.setFocused(false);
        }
    }

    private void focusSearchBox() {
        shown = true;

        searchBox.setEditable(true);
        searchBox.setSelectionStart(0);
        searchBox.setSelectionEnd(searchBox.getText().length());

        if (MilloMod.MC.currentScreen != null) MilloMod.MC.currentScreen.setFocused(searchBox);
    }

    @Override
    public void containerClose(CallbackInfo ci) {
        if (!isEnabled()) return;
        if (searchBox == null) return;

        searchBox.setText("");
        shown = false;
    }
}
