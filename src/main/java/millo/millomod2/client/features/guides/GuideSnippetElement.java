package millo.millomod2.client.features.guides;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.util.SoundUtil;
import millo.millomod2.menu.elements.ClickableElement;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;

/** A copyable one-line snippet or multi-line code block. */
public class GuideSnippetElement extends ClickableElement<GuideSnippetElement> {

    private static final int PADDING = 5;
    private static final int LINE_HEIGHT = 10;

    private final String snippet;
    private final String[] lines;

    private GuideSnippetElement(String snippet, String tooltip) {
        super(0, 0, 0, snippet.stripTrailing().split("\\R", -1).length * LINE_HEIGHT + PADDING * 2, Text.empty());
        this.snippet = snippet.stripTrailing();
        this.lines = this.snippet.split("\\R", -1);
        setTooltip(Tooltip.of(Text.literal(tooltip)));
    }

    public static GuideSnippetElement create(String snippet, String tooltip) {
        return new GuideSnippetElement(snippet, tooltip);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        background(isMouseOver(mouseX, mouseY) ? 0xA0303030 : 0x60000000);
        super.renderWidget(context, mouseX, mouseY, deltaTicks);

        int y = getY() + PADDING;
        for (String line : lines) {
            context.drawText(MilloMod.MC.textRenderer, line, getX() + PADDING, y, 0xFFFFE080, true);
            y += LINE_HEIGHT;
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (!isMouseOver(click.x(), click.y())) return false;
        MilloMod.MC.keyboard.setClipboard(snippet);
        SoundUtil.playClickSound();
        return true;
    }
}
