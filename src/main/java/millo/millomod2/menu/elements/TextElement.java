package millo.millomod2.menu.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.menu.FadeElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class TextElement extends TextWidget implements FadeElement {

    private TextElement(Text message) {
        super(message, MilloMod.MC.textRenderer);
    }

    public static TextElement create(Text message) {
        return new TextElement(message);
    }

    public static TextElement create(String message) {
        return new TextElement(Text.of(message));
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        getFade().progress(deltaTicks);
        context.getMatrices().pushMatrix();
        getFade().applyTranslation(context.getMatrices());
        super.renderWidget(context, mouseX, mouseY, deltaTicks);
        context.getMatrices().popMatrix();
    }

    private final Fade fade = new Fade(Fade.Direction.RIGHT);
    @Override
    public Fade getFade() {
        return fade;
    }
}
