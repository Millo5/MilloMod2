package millo.millomod2.menu.elements.buttons;

import net.minecraft.client.gui.Click;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class ButtonElement extends AbstractButton<ButtonElement> {

    private Consumer<ButtonElement> onPress;
    private Consumer<ClickInfo> onClick;

    protected ButtonElement(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    public static ButtonElement create(int width, int height) {
        return new ButtonElement(0, 0, width, height, Text.empty());
    }

    protected ButtonElement self() {
        return this;
    }

    @Override
    public void onClick(Click click, boolean doubled) {
        if (onClick != null) {
            onClick.accept(new ClickInfo(click, doubled));
        }
        if (onPress != null) {
            onPress.accept(this);
        }
    }

    public ButtonElement onPress(Consumer<ButtonElement> onPress) {
        this.onPress = onPress;
        return this;
    }

    public ButtonElement onClick(Consumer<ClickInfo> onClick) {
        this.onClick = onClick;
        return this;
    }

    public record ClickInfo(Click click, boolean doubled) { }
}
