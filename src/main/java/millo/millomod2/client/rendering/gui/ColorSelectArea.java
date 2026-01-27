package millo.millomod2.client.rendering.gui;

import millo.millomod2.client.menus.ColorsMenu;
import millo.millomod2.menu.elements.ClickableElement;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.awt.*;

public class ColorSelectArea extends ClickableElement<ColorSelectArea> implements ColorElement {

    private final ColorsMenu colorsMenu;
    private final float[] hsb;

    private boolean dragging;

    public ColorSelectArea(ColorsMenu colorsMenu, int width, int height, float[] hsb) {
        super(0, 0, width, height, Text.empty());
        this.colorsMenu = colorsMenu;
        this.hsb = hsb;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        background(0xff3300ff);

        for (int x = 0; x < getWidth(); x++) {
            float p = (float) x / getWidth();

            context.fillGradient(
                    getX() + x,
                    getY(),
                    getX() + x + 1,
                    getBottom(),
                    Color.getHSBColor(p, 1, hsb[2]).hashCode(),
                    Color.getHSBColor(p, 0, hsb[2]).hashCode()
            );

            int cusorColor = Color.black.hashCode();
            if (hsb[2] < 0.5f) cusorColor = Color.white.hashCode();

            int cursorX = (int) (getX() + getWidth() * hsb[0]);
            int cursorY = (int) (getY() + getHeight() * (1f - hsb[1]));
            context.fill(cursorX-1, cursorY, cursorX+2, cursorY+1, cusorColor);
            context.fill(cursorX, cursorY-1, cursorX+1, cursorY+2, cusorColor);

        }

//        super.renderWidget(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (!isMouseOver(click.x(), click.y())) return false;
        dragging = true;
        updateFromMouse(click);
        return true;
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        if (!dragging) return false;
        updateFromMouse(click);
        return true;
    }

    private void updateFromMouse(Click click) {
        double x = Math.clamp(click.x(), getX(), getRight());
        double y = Math.clamp(click.y(), getY(), getBottom());

        hsb[0] = (float) ((x - getX()) / getWidth());
        hsb[1] = 1f - (float) ((y - getY()) / getHeight());

        hsb[0] = Math.clamp(hsb[0], 0f, 1f);
        hsb[1] = Math.clamp(hsb[1], 0f, 1f);

        colorsMenu.updateFromHSB(true);
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (!dragging) return false;
        updateFromMouse(click);
        dragging = false;
        return true;
    }


    @Override
    public void updateColor(Color color) {
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
    }
}
