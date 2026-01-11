package millo.millomod2.client.rendering.gui;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.util.style.GUIStyle;
import millo.millomod2.menu.FadeElement;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;

import java.text.DecimalFormat;
import java.util.function.Consumer;

public class NumberSliderElement extends ClickableWidget implements FadeElement {

    private DecimalFormat df = new DecimalFormat("#.##");

    private double value;
    private final double min;
    private final double max;
    private final double step;
    private final Consumer<Double> onChange;

    private boolean dragging = false;

    private NumberSliderElement(double value, double min, double max, double step, Consumer<Double> onChange) {
        super(0, 0, 100, 20, null);
        this.value = value;
        this.min = min;
        this.max = max;
        this.step = step;
        this.onChange = onChange;
    }

    public static ClickableWidget create(int value, int min, int max, double step, Consumer<Double> onChange) {
        return new NumberSliderElement(value, min, max, step, onChange);
    }

//    @Override
//    public void render(DrawContext context, float mouseX, float mouseY, float delta, Widget<?> renderer) {
//        ((WidgetPosAccessor) position).iUpdateAnchors(renderer);
//        hovered = isHovered(mouseX, mouseY, renderer);
//
//        if (dragging) {
//            updateValueFromMouse(mouseX);
//        }
//
//        TextRenderer textRenderer = MilloMod.MC.textRenderer;
//        String text = df.format(value);
//        int textWidth = textRenderer.getWidth(text);
//
//        double progress = (value - min) / (max - min);
//        int middle = y() + height() / 2;
//
//        context.fill(x(), middle -1, x() + (width() - textWidth) / 2 - 2, middle + 1, GUIStyle.GUIDE);
//        context.fill(x() + (width() + textWidth) / 2 + 2, middle -1, x() + width(), middle + 1, GUIStyle.GUIDE);
//        context.fill(x(), y(), (int) (x() + progress * width()), y() + height(), GUIStyle.ACCENT);
//
//        context.drawText(textRenderer, text, x() + (width() - textWidth) / 2, middle - 4, 0xFFFFFFFF, false);
//    }

    private boolean hovered = false;

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        getFade().progress(deltaTicks);
        context.getMatrices().pushMatrix();
        getFade().applyTranslation(context.getMatrices());

        hovered = isMouseOver(mouseX, mouseY);

        if (dragging) {
            updateValueFromMouse(mouseX);
            onChange.accept(value);
        }

        TextRenderer textRenderer = MilloMod.MC.textRenderer;
        String text = df.format(value);
        int textWidth = textRenderer.getWidth(text);

        double progress = (value - min) / (max - min);
        int middle = getY() + getHeight() / 2;

        context.fill(getX(), middle -1, getX() + (getWidth() - textWidth) / 2 - 2, middle + 1, GUIStyle.GUIDE);
        context.fill(getX() + (getWidth() + textWidth) / 2 + 2, middle -1, getX() + getWidth(), middle + 1, GUIStyle.GUIDE);
        context.fill(getX(), getY(), (int) (getX() + progress * getWidth()), getX() + getHeight(), GUIStyle.ACCENT);

        int textColor = hovered ? 0xFFFFFFAA : 0xFFFFFFFF;
        context.drawText(textRenderer, text, getX() + (getWidth() - textWidth) / 2, middle - 4, textColor, false);

        context.getMatrices().popMatrix();
    }


//    @Override
//    protected void render(DrawContext drawContext, float mouseX, float mouseY, int erm, float delta) {
//        hovered = isHovered(mouseX, mouseY);
//
//        if (dragging) {
//            updateValueFromMouse(mouseX);
//            onChange.accept(value);
//        }
//
//        TextRenderer textRenderer = MilloMod.MC.textRenderer;
//        String text = df.format(value);
//        int textWidth = textRenderer.getWidth(text);
//
//        double progress = (value - min) / (max - min);
//        int middle = top() + height() / 2;
//
//        drawContext.fill(left(), middle - 1, left() + (width() - textWidth) / 2 - 2, middle + 1, GUIStyle.GUIDE);
//        drawContext.fill(left() + (width() + textWidth) / 2 + 2, middle - 1, right(), middle + 1, GUIStyle.GUIDE);
//        drawContext.fill(left(), top(), (int) (left() + progress * width()), bottom(), GUIStyle.ACCENT);
//    }


    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (!active || !visible) return false;
        if (!isValidClickButton(click.buttonInfo())) return false;
        if (!isMouseOver(click.x(), click.y())) return false;

        dragging = true;
        updateValueFromMouse(click.x());
        return true;
    }

    @Override
    public boolean mouseReleased(Click click) {
        dragging = false;
        return true;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

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
