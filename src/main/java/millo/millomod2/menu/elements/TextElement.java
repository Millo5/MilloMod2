package millo.millomod2.menu.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.mixin.render.accessors.ClickableWidgetAccessor;
import millo.millomod2.client.util.SoundUtil;
import millo.millomod2.menu.FadeElement;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import org.joml.Vector2f;

import java.util.function.Supplier;

public class TextElement extends TextWidget implements FadeElement {

    private TextAlignment alignment = TextAlignment.LEFT;
    private Supplier<Boolean> onClick = null;

    private int xOffset = 0;
    private int yOffset = 0;

    private int highlight = 0;
    private int highlightStart = 0;
    private int highlightEnd = 0;

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

        context.getMatrices().translate(xOffset, yOffset);

        if (highlight != 0) {
            context.fill(getX(), getY() - 1, getRight(), getBottom(), 0x40000000 | highlight);
            int highlightXStart = getX() + MilloMod.MC.textRenderer.getWidth(getMessage().getString().substring(0, highlightStart));
            int highlightXWidth = getX() + MilloMod.MC.textRenderer.getWidth(getMessage().getString().substring(0, highlightEnd)) - highlightXStart;
//            context.fill(highlightXStart, getY() - 1, highlightXStart + highlightXWidth, getBottom(), 0x80000000 | highlight);
            context.drawStrokedRectangle(highlightXStart, getY()-1, highlightXWidth, getHeight()+1, 0xFF000000 | highlight);
        }

        super.renderWidget(context, mouseX, mouseY, deltaTicks);

        if (onClick != null && hovered) {
            context.fill(getX(), getBottom() - 1, getRight(), getBottom(), 0xFFFFFFFF);
        }

        ClickableWidgetAccessor accessor = (ClickableWidgetAccessor) this;
        if (accessor.getTooltipState().getTooltip() != null) {
            var pos = context.getMatrices().transformPosition(mouseX, mouseY, new Vector2f());
            accessor.getTooltipState().render(context, (int)pos.x, (int)pos.y, hovered, isFocused(), getNavigationFocus());
        }

        context.getMatrices().popMatrix();
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (!isMouseOver(click.x(), click.y())) return false;
        if (onClick == null) return false;

        if (onClick.get()) {
            SoundUtil.playClickSound();
        }

        return true;
    }

    @Override
    public void draw(DrawnTextConsumer textConsumer) {
        super.draw(textConsumer);
    }

    public TextElement align(TextAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public TextElement onClickListener(Supplier<Boolean> runnable) {
        this.onClick = runnable;
        this.active = true;
        return this;
    }

    private final Fade fade = new Fade();
    @Override
    public Fade getFade() {
        return fade;
    }

    public TextElement offset(int x, int y) {
        xOffset = x;
        yOffset = y;
        return this;
    }

    public void setHighlight(int highlight, int start, int end) {
        this.highlight = highlight;
        this.highlightStart = start;
        this.highlightEnd = end;
    }

    public enum TextAlignment {
        LEFT,
        CENTER,
        RIGHT
    }
}
