package millo.millomod2.menu;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

public abstract class PopUpMenu extends Menu {
    private final Screen parent;

    public PopUpMenu(Screen parent) {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        parent.render(context, mouseX, mouseY, deltaTicks);

        context.fill(0, 0, this.width, this.height, 0x88000000); // Semi-transparent background

        super.render(context, mouseX, mouseY, deltaTicks);
    }

}
