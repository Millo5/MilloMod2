package millo.millomod2.client.rendering.gui;

import millo.millomod2.client.menus.ColorsMenu;
import millo.millomod2.menu.elements.ClickableElement;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class ColorSquare extends ClickableElement<ColorSquare> {

    private final int color;
    private ColorsMenu colorsMenu;
    private final boolean isPartOfSaved;

    public ColorSquare(int width, int height, int color, ColorsMenu colorsMenu, boolean isPartOfSaved) {
        super(0, 0, width, height, Component.empty());
        this.color = color;
        this.colorsMenu = colorsMenu;
        this.isPartOfSaved = isPartOfSaved;

        background(color);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        if (!isMouseOver(click.x(), click.y())) return false;
        if (click.button() == 0) {
            colorsMenu.setColor(color);
        } else if (isPartOfSaved) {
            colorsMenu.removeSavedColor(color);
        }
        return true;
    }


    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, deltaTicks);

        if (isMouseOver(mouseX, mouseY)) {
            graphics.outline(getX(), getY(), getWidth(), getHeight(), 0xffffffff);
        }
    }

    public void setMenu(ColorsMenu colorsMenu) {
        this.colorsMenu = colorsMenu;
    }
}
