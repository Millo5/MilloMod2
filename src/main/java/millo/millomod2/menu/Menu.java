package millo.millomod2.menu;

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

    public void closeContextMenu() {
        if (this.contextMenu != null) {
            remove(this.contextMenu);
            this.contextMenu = null;
        }
    }


    public void openContextMenu(ClickableWidget menu, int x, int y) {
        menu.setX(x);
        menu.setY(y);
        openContextMenu(menu);
    }

    public void openContextMenu(ClickableWidget menu) {
        if (this.contextMenu != null) {
            remove(this.contextMenu);
        }

        if (menu instanceof FadeElement fade) {
            fade.getFade().reset();
        }
        this.contextMenu = menu;
        addDrawableChild(menu);
    }


}
