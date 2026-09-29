package millo.millomod2.menu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;

public abstract class PopUpMenu extends Menu {
    private final Screen parent;

    public PopUpMenu(Screen parent) {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
        parent.extractRenderState(graphics, mouseX, mouseY, deltaTicks);

        graphics.fill(0, 0, this.width, this.height, 0x88000000); // Semi-transparent background

        super.extractRenderState(graphics, mouseX, mouseY, deltaTicks);
    }

}
