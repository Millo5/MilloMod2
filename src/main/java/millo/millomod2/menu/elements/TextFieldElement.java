package millo.millomod2.menu.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.menu.FadeElement;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class TextFieldElement extends EditBox implements FadeElement {

    public TextFieldElement(int width, int height, Component text) {
        super(MilloMod.MC.font, width, height, text);
        setValue(text.getString());

        setBordered(false);
    }

    public TextFieldElement(Font textRenderer, int x, int y, int w, int h, MutableComponent text) {
        super(textRenderer, x, y, w, h, text);
        setBordered(false);
    }

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
        getFade().progress(deltaTicks * 20f);

        context.pose().pushMatrix();
        getFade().applyTranslation(context.pose());
        int color = getFade().getColor(0, 0, 0, 150);
        int underlineColor = getFade().getColor(51, 51, 51, 150);
        if (isHovered()) {
            color = getFade().getColor(12, 11, 9, 150);
            underlineColor = getFade().getColor(151, 151, 151, 150);
        }
        if (isFocused()) underlineColor = getFade().getColor(200, 200, 200, 150);

        context.fill(getX(), getY(), getRight(), getBottom(), color);
        context.fill(getX(), getBottom() - 1, getRight(), getBottom(), underlineColor);

        context.pose().translate(4, height / 2f - 4);
        super.renderWidget(context, mouseX, mouseY, deltaTicks);
        context.pose().popMatrix();
    }

    private final Fade fade = new Fade();
    @Override
    public Fade getFade() {
        return fade;
    }
}
