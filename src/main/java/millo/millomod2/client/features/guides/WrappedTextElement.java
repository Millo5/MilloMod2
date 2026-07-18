package millo.millomod2.client.features.guides;

import millo.millomod2.client.MilloMod;
import millo.millomod2.menu.elements.ClickableElement;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

/** A guide paragraph that wraps to the width assigned by its containing ListElement. */
public class WrappedTextElement extends ClickableElement<WrappedTextElement> {

    private static final int LINE_HEIGHT = 10;

    private final Text text;
    private List<OrderedText> lines = List.of();

    private WrappedTextElement(Text text) {
        super(0, 0, 0, LINE_HEIGHT, text);
        this.text = text;
        active = false;
    }

    public static WrappedTextElement create(Text text) {
        return new WrappedTextElement(text);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        TextRenderer renderer = MilloMod.MC.textRenderer;
        lines = renderer.wrapLines(text, Math.max(1, width));
        setHeight(Math.max(LINE_HEIGHT, lines.size() * LINE_HEIGHT));
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.renderWidget(context, mouseX, mouseY, deltaTicks);
        int y = getY();
        for (OrderedText line : lines) {
            context.drawText(MilloMod.MC.textRenderer, line, getX(), y, 0xFFFFFFFF, true);
            y += LINE_HEIGHT;
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        return false;
    }
}
