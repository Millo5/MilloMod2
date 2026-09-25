package millo.millomod2.menu;

import millo.millomod2.client.MilloMod;
import millo.millomod2.menu.elements.ConfirmationElement;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public abstract class Menu extends Screen {

    private final Screen parent;

    private AbstractWidget contextMenu = null;

    public Menu(Screen parent) {
        super(Component.empty());
        this.parent = parent;
    }

    @Override
    public void onClose() {
        setFocused(null);
        this.minecraft.setScreen(parent);
    }


    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        if (contextMenu != null) {
            if (contextMenu.isMouseOver(click.x(), click.y())) return contextMenu.mouseClicked(click, doubled);
            else closeContextMenu();
            return true;
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (contextMenu != null) {
            if (contextMenu.isMouseOver(mouseX, mouseY)) return contextMenu.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    public void closeContextMenu() {
        if (this.contextMenu != null) {
            removeWidget(this.contextMenu);
            this.contextMenu = null;
        }
    }


    public void openContextMenuAtCursor(AbstractWidget menu) {
        openContextMenu(menu,
                (int) MilloMod.MC.mouseHandler.getScaledXPos(MilloMod.MC.getWindow()),
                (int) MilloMod.MC.mouseHandler.getScaledYPos(MilloMod.MC.getWindow())
        );
    }

    public void openContextMenuAtCursor(AbstractWidget menu, int offsetX, int offsetY) {
        openContextMenu(menu,
                (int) MilloMod.MC.mouseHandler.getScaledXPos(MilloMod.MC.getWindow()) + offsetX,
                (int) MilloMod.MC.mouseHandler.getScaledYPos(MilloMod.MC.getWindow()) + offsetY
        );
    }

    public void openContextMenu(AbstractWidget menu, int x, int y) {
        menu.setX(x);
        menu.setY(y);
        openContextMenu(menu);
    }

    public void openContextMenu(AbstractWidget menu) {
        if (this.contextMenu != null) {
            closeContextMenu();
        }

        if (menu instanceof FadeElement fade) {
            fade.getFade().reset();
        }
        this.contextMenu = menu;
        addRenderableWidget(menu);
    }

    public void openConfirmationMenu(Component title, Component message, Runnable onConfirm) {
        openContextMenu(new ConfirmationElement(title, message, onConfirm),
                (width - 400) / 2, (height - 200) / 2);
    }

    public void open() {
        MilloMod.MC.schedule(() -> MilloMod.MC.setScreen(this));
    }

}
