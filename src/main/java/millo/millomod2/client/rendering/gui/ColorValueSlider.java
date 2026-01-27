package millo.millomod2.client.rendering.gui;

import millo.millomod2.client.menus.ColorsMenu;
import millo.millomod2.menu.elements.ClickableElement;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.awt.*;

public class ColorValueSlider extends ClickableElement<ColorValueSlider> implements ColorElement {

    private final ColorsMenu colorsMenu;
    private final float[] hsb;

    private boolean dragging = false;

    public ColorValueSlider(ColorsMenu colorsMenu, int width, int height, float[] hsb) {
        super(0, 0, width, height, Text.empty());
        this.colorsMenu = colorsMenu;
        this.hsb = hsb;
    }

    public void updateColor(Color color) {
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.fillGradient(getX(), getY(), getRight(), getBottom(),
                Color.getHSBColor(hsb[0], hsb[1], 1).hashCode(),
                Color.getHSBColor(hsb[0], hsb[1], 0).hashCode()
        );
        super.renderWidget(context, mouseX, mouseY, deltaTicks);

        int handleY = (int) ((1f - hsb[2]) * (getHeight() - 1) + getY()) + 1;
        context.fill(getX() - 2, handleY - 1, getRight() + 3, handleY, 0xffffffff);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (!isMouseOver(click.x(), click.y())) return false;
        dragging = true;
        updateFromMouse(click.y());
        return true;
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        if (!dragging) return false;
        updateFromMouse(click.y());
        return true;
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (!dragging) return false;
        updateFromMouse(click.y());
        dragging = false;
        return true;
    }

    private void updateFromMouse(double mouseY) {
        hsb[2] = 1f - (float) (mouseY - getY()) / getHeight();
        hsb[2] = Math.clamp(hsb[2], 0f, 1f);
        colorsMenu.updateFromHSB(true);
    }

}
