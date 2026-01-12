package millo.millomod2.menu.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.menu.FadeElement;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class TextFieldElement extends TextFieldWidget implements FadeElement {

    public TextFieldElement(int width, int height, Text text) {
        super(MilloMod.MC.textRenderer, width, height, text);

        setDrawsBackground(false);
    }

    public TextFieldElement(TextRenderer textRenderer, int x, int y, int w, int h, MutableText text) {
        super(textRenderer, x, y, w, h, text);
        setDrawsBackground(false);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        getFade().progress(deltaTicks * 20f);

        context.getMatrices().pushMatrix();
        getFade().applyTranslation(context.getMatrices());
        int color = getFade().getColor(0, 0, 0, 150);
        int underlineColor = getFade().getColor(51, 51, 51, 150);
        if (isHovered()) {
            color = getFade().getColor(12, 11, 9, 150);
            underlineColor = getFade().getColor(151, 151, 151, 150);
        }
        if (isFocused()) underlineColor = getFade().getColor(200, 200, 200, 150);

        context.fill(getX(), getY(), getRight(), getBottom(), color);
        context.fill(getX(), getBottom() - 1, getRight(), getBottom(), underlineColor);

        context.getMatrices().translate(4, 4);
        super.renderWidget(context, mouseX, mouseY, deltaTicks);
        context.getMatrices().popMatrix();
    }

    private final Fade fade = new Fade();
    @Override
    public Fade getFade() {
        return fade;
    }
}
