package millo.millomod2.client.features.guides;

import millo.millomod2.client.MilloMod;
import millo.millomod2.menu.elements.ClickableElement;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

/** A guide paragraph that wraps to the width assigned by its containing ListElement. */
public class WrappedTextElement extends ClickableElement<WrappedTextElement> {

    private static final int LINE_HEIGHT = 10;

    private final Component text;
    private List<FormattedCharSequence> lines = List.of();

    private WrappedTextElement(Component text) {
        super(0, 0, 0, LINE_HEIGHT, text);
        this.text = text;
        active = false;
    }

    public static WrappedTextElement create(Component text) {
        return new WrappedTextElement(text);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        Font renderer = MilloMod.MC.font;
        lines = renderer.split(text, Math.max(1, width));
        setHeight(Math.max(LINE_HEIGHT, lines.size() * LINE_HEIGHT));
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, deltaTicks);
        int y = getY();
        for (FormattedCharSequence line : lines) {
            graphics.text(MilloMod.MC.font, line, getX(), y, 0xFFFFFFFF, true);
            y += LINE_HEIGHT;
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        return false;
    }
}
