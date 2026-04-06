package millo.millomod2.menu;

import millo.millomod2.client.MilloMod;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public abstract class Menu extends Screen {

    private final Screen parent;

    private ClickableWidget contextMenu = null;

    public Menu(Screen parent) {
        super(Text.empty());
        this.parent = parent;
    }

    @Override
    public void close() {
        setFocused(null);
        this.client.setScreen(parent);
    }


    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
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
            remove(this.contextMenu);
            this.contextMenu = null;
        }
    }


    public void openContextMenuAtCursor(ClickableWidget menu) {
        openContextMenu(menu,
                (int) MilloMod.MC.mouse.getScaledX(MilloMod.MC.getWindow()),
                (int) MilloMod.MC.mouse.getScaledY(MilloMod.MC.getWindow())
        );
    }

    public void openContextMenuAtCursor(ClickableWidget menu, int offsetX, int offsetY) {
        openContextMenu(menu,
                (int) MilloMod.MC.mouse.getScaledX(MilloMod.MC.getWindow()) + offsetX,
                (int) MilloMod.MC.mouse.getScaledY(MilloMod.MC.getWindow()) + offsetY
        );
    }

    public void openContextMenu(ClickableWidget menu, int x, int y) {
        menu.setX(x);
        menu.setY(y);
        openContextMenu(menu);
    }

    public void openContextMenu(ClickableWidget menu) {
        if (this.contextMenu != null) {
            closeContextMenu();
        }

        if (menu instanceof FadeElement fade) {
            fade.getFade().reset();
        }
        this.contextMenu = menu;
        addDrawableChild(menu);
    }


    public void open() {
        MilloMod.MC.send(() -> MilloMod.MC.setScreen(this));
    }

}
