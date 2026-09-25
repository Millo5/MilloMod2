package millo.millomod2.menu.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.mixin.render.accessors.ClickableWidgetAccessor;
import millo.millomod2.client.util.SoundUtil;
import millo.millomod2.menu.FadeElement;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.joml.Vector2f;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class TextElement extends StringWidget implements FadeElement {

    private TextAlignment alignment = TextAlignment.LEFT;
    private Supplier<Boolean> onClick = null;
    private Supplier<@Nullable Component> tooltipSupplier = null;

    private int xOffset = 0;
    private int yOffset = 0;

    private int highlight = 0;
    private int highlightStart = 0;
    private int highlightEnd = 0;

    private TextElement(Component message) {
        super(message, MilloMod.MC.font);
        setWidth(MilloMod.MC.font.width(message));
    }

    public static TextElement create(Component message) {
        return new TextElement(message);
    }

    public static TextElement create(String message) {
        return new TextElement(Component.nullToEmpty(message));
    }

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
        getFade().progress(deltaTicks);
        if (isHovered && tooltipSupplier != null) {
            Component tooltip = tooltipSupplier.get();
            setTooltip(tooltip == null ? null : Tooltip.create(tooltip));
        }
        context.pose().pushMatrix();
        getFade().applyTranslation(context.pose());



        int textWidth = MilloMod.MC.font.width(this.getMessage());
        if (alignment == TextAlignment.CENTER) {
            context.pose().translate((this.width - textWidth) / 2f, 0);
        } else if (alignment == TextAlignment.RIGHT) {
            context.pose().translate(this.width - textWidth, 0);
        }

        context.pose().translate(xOffset, yOffset);

        if (highlight != 0) {
            context.fill(getX(), getY() - 1, getRight(), getBottom(), 0x40000000 | highlight);
            int highlightXStart = getX() + MilloMod.MC.font.width(getMessage().getString().substring(0, highlightStart));
            int highlightXWidth = getX() + MilloMod.MC.font.width(getMessage().getString().substring(0, highlightEnd)) - highlightXStart;
//            context.fill(highlightXStart, getY() - 1, highlightXStart + highlightXWidth, getBottom(), 0x80000000 | highlight);
            context.renderOutline(highlightXStart, getY()-1, highlightXWidth, getHeight()+1, 0xFF000000 | highlight);
        }

        super.renderWidget(context, mouseX, mouseY, deltaTicks);

        if (onClick != null && isHovered) {
            context.fill(getX(), getBottom() - 1, getRight(), getBottom(), 0xFFFFFFFF);
        }

        ClickableWidgetAccessor accessor = (ClickableWidgetAccessor) this;
        if (accessor.getTooltipState().get() != null) {
            var pos = context.pose().transformPosition(mouseX, mouseY, new Vector2f());
            accessor.getTooltipState().refreshTooltipForNextRenderPass(context, (int)pos.x, (int)pos.y, isHovered, isFocused(), getRectangle());
        }

        context.pose().popMatrix();
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        if (!isMouseOver(click.x(), click.y())) return false;
        if (onClick == null) return false;

        if (onClick.get()) {
            SoundUtil.playClickSound();
        }

        return true;
    }

    @Override
    public void visitLines(ActiveTextCollector textConsumer) {
        super.visitLines(textConsumer);
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

    public TextElement tooltip(Supplier<@Nullable Component> supplier) {
        tooltipSupplier = supplier;
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
