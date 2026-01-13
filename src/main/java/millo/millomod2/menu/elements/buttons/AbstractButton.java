package millo.millomod2.menu.elements.buttons;

import millo.millomod2.client.MilloMod;
import millo.millomod2.menu.elements.ClickableElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public abstract class AbstractButton<T extends AbstractButton<T>> extends ClickableElement<T> {

    private int hoverBackgroundColor = -1;

    public AbstractButton(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    protected abstract T self();


    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (!this.isInteractable()) return false;
        if (!this.isValidClickButton(click.buttonInfo())) return false;

        if (isMouseOver(click.x(), click.y())) {
            this.playDownSound(MinecraftClient.getInstance().getSoundManager());
            onClick(click, doubled);
            return true;
        }

        return false;
    }


    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        boolean hovered = isMouseOver(mouseX, mouseY);

        int background = this.background;
        if (hoverBackgroundColor >= 0) background(hovered ? hoverBackgroundColor : background);
        super.renderWidget(context, mouseX, mouseY, deltaTicks);
        this.background = background;

        int textColor = hovered ? 0xFFFFFFAA : 0xFFFFFFFF;
        context.drawCenteredTextWithShadow(MilloMod.MC.textRenderer, getMessage(), getX() + getWidth() / 2, getY() + (getHeight() - 8) / 2, textColor);
    }

    public T message(Text message) {
        setMessage(message);
        return self();
    }

    public T position(int x, int y) {
        setPosition(x, y);
        return self();
    }

    public T hoverBackground(int color) {
        this.hoverBackgroundColor = color;
        return self();
    }

    public T removeHoverBackground() {
        this.hoverBackgroundColor = -1;
        return self();
    }
}
