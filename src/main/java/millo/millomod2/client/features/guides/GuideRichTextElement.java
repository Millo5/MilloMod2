package millo.millomod2.client.features.guides;

import millo.millomod2.client.MilloMod;
import millo.millomod2.menu.elements.ClickableElement;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/** A non-interactive, wrapped paragraph with markdown-like inline code snippets. */
public class GuideRichTextElement extends ClickableElement<GuideRichTextElement> {

    private static final int LINE_HEIGHT = 10;
    private static final int SNIPPET_PADDING = 3;

    private final List<GuideSectionText.Segment> segments;
    private List<Line> lines = List.of();

    private GuideRichTextElement(List<GuideSectionText.Segment> segments) {
        super(0, 0, 0, LINE_HEIGHT, Text.empty());
        this.segments = segments;
        active = false;
    }

    public static GuideRichTextElement create(List<GuideSectionText.Segment> segments) {
        return new GuideRichTextElement(segments);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        lines = wrap(MilloMod.MC.textRenderer, Math.max(1, width));
        setHeight(Math.max(LINE_HEIGHT, lines.size() * LINE_HEIGHT));
    }

    private List<Line> wrap(TextRenderer renderer, int width) {
        ArrayList<Line> wrapped = new ArrayList<>();
        Line line = new Line();
        for (GuideSectionText.Segment segment : segments) {
            if (segment.snippet()) {
                line = append(renderer, width, wrapped, line, new Part(segment.text(), true));
                continue;
            }
            for (String word : segment.text().split("(?<=\\s)|(?=\\s)")) {
                line = append(renderer, width, wrapped, line, new Part(word, false));
            }
        }
        if (!line.parts.isEmpty()) wrapped.add(line);
        return wrapped;
    }

    private Line append(TextRenderer renderer, int maxWidth, List<Line> wrapped, Line line, Part part) {
        int partWidth = renderer.getWidth(part.text) + (part.snippet ? SNIPPET_PADDING * 2 : 0);
        if (!line.parts.isEmpty() && line.width + partWidth > maxWidth) {
            wrapped.add(line);
            line = new Line();
        }
        line.parts.add(part);
        line.width += partWidth;
        return line;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        int y = getY();
        for (Line line : lines) {
            int x = getX();
            for (Part part : line.parts) {
                int textWidth = MilloMod.MC.textRenderer.getWidth(part.text);
                if (part.snippet) {
                    context.fill(x, y - 2, x + textWidth + SNIPPET_PADDING * 2, y + 10, 0x30ffffff);
                    context.drawText(MilloMod.MC.textRenderer, part.text, x + SNIPPET_PADDING, y, 0xFFFFE080, true);
                    x += textWidth + SNIPPET_PADDING * 2;
                } else {
                    context.drawText(MilloMod.MC.textRenderer, part.text, x, y, 0xFFFFFFFF, true);
                    x += textWidth;
                }
            }
            y += LINE_HEIGHT;
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        return false;
    }

    private static class Line {
        private final ArrayList<Part> parts = new ArrayList<>();
        private int width;
    }

    private record Part(String text, boolean snippet) {}
}
