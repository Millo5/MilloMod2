package millo.millomod2.menu.elements.buttons;

import millo.millomod2.menu.elements.ClickableElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.Alignment;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public abstract class AbstractButton<T extends AbstractButton<T>> extends ClickableElement<T> {

    private int hoverBackgroundColor = -1;
    private Alignment textAlignment = Alignment.CENTER;

    public AbstractButton(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    protected abstract T self();


    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (!this.isInteractable()) return false;
//        if (!this.isValidClickButton(click.buttonInfo())) return false;

        if (isMouseOver(click.x(), click.y())) {
            this.playDownSound(MinecraftClient.getInstance().getSoundManager());
            onClick(click, doubled);
            return true;
        }

        return false;
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.ui(SoundEvents.ENTITY_GLOW_ITEM_FRAME_ADD_ITEM, 1.0F));
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        boolean hovered = isMouseOver(mouseX, mouseY);

        int background = this.background;
        if (hoverBackgroundColor >= 0) background(hovered ? hoverBackgroundColor : this.background);
        super.renderWidget(context, mouseX, mouseY, deltaTicks);
        this.background = background;

        int textColor = hovered ? 0xFFFFFFAA : 0xFFFFFFFF;

        int textX = switch (textAlignment) {
            case LEFT -> getX() + 4;
            case RIGHT -> getX() + getWidth() - 4 - MinecraftClient.getInstance().textRenderer.getWidth(getMessage());
            case CENTER -> getX() + (getWidth() - MinecraftClient.getInstance().textRenderer.getWidth(getMessage())) / 2;
        };
        context.drawText(MinecraftClient.getInstance().textRenderer, getMessage(), textX, getY() + (getHeight() - 8) / 2, textColor, true);
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

    public int getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }

}
