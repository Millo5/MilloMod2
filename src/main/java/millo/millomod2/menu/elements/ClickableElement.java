package millo.millomod2.menu.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.mixin.render.accessors.ClickableWidgetAccessor;
import millo.millomod2.menu.FadeElement;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.joml.Vector2f;

public abstract class ClickableElement<T extends ClickableElement<?>> extends AbstractWidget implements FadeElement {

    protected int background = 0;
    private Border border = new Border();

    public ClickableElement(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    public T background(int color) {
        this.background = color;
        return (T) this;
    }
    public T border(Border border) {
        this.border = border;
        return (T) this;
    }
    public T position(int x, int y) {
        this.setX(x);
        this.setY(y);
        return (T) this;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float deltaTicks) {
        graphics.fill(getX(), getY(), getRight(), getBottom(), getFade().getColor(background));
        border.render(graphics, this);

        ClickableWidgetAccessor accessor = (ClickableWidgetAccessor) this;
        if (accessor.getTooltipState().get() != null) {
            Vector2f pos = graphics.pose().transformPosition(mouseX, mouseY, new Vector2f());
            accessor.getTooltipState().refreshTooltipForNextRenderPass(graphics, (int)pos.x, (int)pos.y, isHovered, isFocused(), getRectangle());
        }
    }

    private final Fade fade = new Fade(Fade.Direction.UP);
    @Override
    public Fade getFade() {
        return fade;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {}

    public static class Border {
        private int color = 0;
        private boolean full = false;
        private boolean top = false;
        private boolean bottom = false;
        private boolean left = false;
        private boolean right = false;

        public Border() {}

        public Border top(int color) {
            this.color = color;
            top = true;
            return this;
        }
        public Border bottom(int color) {
            this.color = color;
            bottom = true;
            return this;
        }
        public Border left(int color) {
            this.color = color;
            left = true;
            return this;
        }
        public Border right(int color) {
            this.color = color;
            right = true;

            return this;
        }
        public Border full(int color) {
            this.color = color;
            full = true;
            return this;
        }

        public void render(GuiGraphicsExtractor graphics, ClickableElement<?> element) {
            int color = element.getFade().getColor(this.color);
            if (full) {
                graphics.outline(element.getX(), element.getY(), element.getWidth(), element.getHeight(), color);
                return;
            }

            if (top) graphics.fill(element.getX(), element.getY() + 1, element.getRight(), element.getY(), color);
            if (bottom) graphics.fill(element.getX(), element.getBottom() + 1, element.getRight(), element.getBottom(), color);
            if (left) graphics.fill(element.getX(), element.getY(), element.getX() + 1, element.getBottom(), color);
            if (right) graphics.fill(element.getRight() + 1, element.getY(), element.getRight(), element.getBottom(), color);
        }
    }

    protected Font getTextRenderer() {
        return MilloMod.MC.font;
    }

    public Border getBorder() {
        return border;
    }

    public int getBackground() {
        return background;
    }
}
