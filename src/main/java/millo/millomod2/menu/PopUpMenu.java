package millo.millomod2.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public abstract class PopUpMenu extends Menu {
    private final Screen parent;

    public PopUpMenu(Screen parent) {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
        parent.render(context, mouseX, mouseY, deltaTicks);

        context.fill(0, 0, this.width, this.height, 0x88000000); // Semi-transparent background

        super.render(context, mouseX, mouseY, deltaTicks);
    }

}
