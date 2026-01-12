package millo.millomod2.menu;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class Menu extends Screen {

    private final Screen parent;

    public Menu(Screen parent) {
        super(Text.empty());
        this.parent = parent;
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

}
