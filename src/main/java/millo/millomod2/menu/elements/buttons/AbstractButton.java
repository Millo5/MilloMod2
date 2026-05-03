package millo.millomod2.menu.elements.buttons;

import millo.millomod2.client.util.SoundUtil;
import millo.millomod2.menu.elements.ClickableElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.Alignment;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public abstract class AbstractButton<T extends AbstractButton<T>> extends ClickableElement<T> {

    private int hoverBackgroundColor = -1;
    private Alignment textAlignment = Alignment.CENTER;
    private boolean muted = false;

    public AbstractButton(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    protected abstract T self();


    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (!this.isInteractable()) return false;
//        if (!this.isValidClickButton(click.buttonInfo())) return false;

        if (isMouseOver(click.x(), click.y())) {
            if (!muted) SoundUtil.playClickSound();
            onClick(click, doubled);
            return true;
        }

        return false;
    }


    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        boolean hovered = isMouseOver(mouseX, mouseY);

        int background = this.background;
        if (hoverBackgroundColor >= 0) background(hovered ? hoverBackgroundColor : this.background);
        super.renderWidget(context, mouseX, mouseY, deltaTicks);
        this.background = background;

        int textX = switch (textAlignment) {
            case LEFT -> getX() + 4;
            case RIGHT -> getX() + getWidth() - 4 - MinecraftClient.getInstance().textRenderer.getWidth(getMessage());
            case CENTER -> getX() + (getWidth() - MinecraftClient.getInstance().textRenderer.getWidth(getMessage())) / 2;
        };
        context.drawText(MinecraftClient.getInstance().textRenderer, getMessage(), textX, getY() + (getHeight() - 8) / 2, getTextColor(), true);
    }

    protected int getTextColor() {
        return isHovered() ? 0xFFFFFFAA : 0xFFFFFFFF;
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

    public T textAlignment(Alignment alignment) {
        this.textAlignment = alignment;
        return self();
    }

    public T muted() {
        this.muted = true;
        return self();
    }

    public int getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }


}
