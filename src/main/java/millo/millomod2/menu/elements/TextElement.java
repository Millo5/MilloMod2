package millo.millomod2.menu.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.menu.FadeElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class TextElement extends TextWidget implements FadeElement {

    private TextAlignment alignment = TextAlignment.LEFT;

    private TextElement(Text message) {
        super(message, MilloMod.MC.textRenderer);
        setWidth(MilloMod.MC.textRenderer.getWidth(message));
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

        int textWidth = MilloMod.MC.textRenderer.getWidth(this.getMessage());
        if (alignment == TextAlignment.CENTER) {
            context.getMatrices().translate((this.width - textWidth) / 2f, 0);
        } else if (alignment == TextAlignment.RIGHT) {
            context.getMatrices().translate(this.width - textWidth, 0);
        }

        super.renderWidget(context, mouseX, mouseY, deltaTicks);
        context.getMatrices().popMatrix();
    }

    public TextElement align(TextAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    private final Fade fade = new Fade();
    @Override
    public Fade getFade() {
        return fade;
    }

    public enum TextAlignment {
        LEFT,
        CENTER,
        RIGHT
    }
}
