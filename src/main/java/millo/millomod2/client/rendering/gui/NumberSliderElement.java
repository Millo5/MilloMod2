package millo.millomod2.client.rendering.gui;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.util.style.GUIStyle;
import millo.millomod2.menu.FadeElement;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;

import java.text.DecimalFormat;
import java.util.function.Consumer;

public class NumberSliderElement extends AbstractWidget implements FadeElement {

    private DecimalFormat df = new DecimalFormat("#.##");

    private double value;
    private final double min;
    private final double max;
    private final double step;
    private final Consumer<Double> onChange;

    private boolean dragging = false;

    public NumberSliderElement(double value, double min, double max, double step, Consumer<Double> onChange) {
        super(0, 0, 100, 20, null);
        this.value = value;
        this.min = min;
        this.max = max;
        this.step = step;
        this.onChange = onChange;
    }

    private boolean isHovered = false;

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
        getFade().progress(deltaTicks);
        graphics.pose().pushMatrix();
        getFade().applyTranslation(graphics.pose());

        isHovered = isMouseOver(mouseX, mouseY);

        if (dragging) {
            updateValueFromMouse(mouseX);
            onChange.accept(value);
        }

        Font textRenderer = MilloMod.MC.font;
        String text = df.format(value);
        int textWidth = textRenderer.width(text);

        double progress = (value - min) / (max - min);
        int middle = getY() + getHeight() / 2;

        graphics.fill(getX(), middle -1, getX() + (getWidth() - textWidth) / 2 - 2, middle + 1, GUIStyle.GUIDE);
        graphics.fill(getX() + (getWidth() + textWidth) / 2 + 2, middle -1, getRight(), middle + 1, GUIStyle.GUIDE);
        graphics.fill(getX(), getY(), (int) (getX() + progress * getWidth()), getBottom(), GUIStyle.ACCENT);

        int textColor = isHovered ? 0xFFFFFFAA : 0xFFFFFFFF;
        graphics.text(textRenderer, text, getX() + (getWidth() - textWidth) / 2, middle - 4, textColor, false);

        graphics.pose().popMatrix();
    }


    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        if (!active || !visible) return false;
        if (!isValidClickButton(click.buttonInfo())) return false;
        if (!isMouseOver(click.x(), click.y())) return false;

        dragging = true;
        updateValueFromMouse(click.x());
        return true;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        dragging = false;
        return true;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {

    }

    private void updateValueFromMouse(double mouseX) {
        double relativeX = mouseX - getX();
        double progress = Math.max(0, Math.min(1, relativeX / getWidth()));
        double newValue = min + progress * (max - min);
        newValue = Math.round(newValue / step) * step;
        value = newValue;
    }

    private final Fade fade = new Fade();
    @Override
    public Fade getFade() {
        return fade;
    }

}
