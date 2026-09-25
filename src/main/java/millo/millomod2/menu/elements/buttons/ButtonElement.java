package millo.millomod2.menu.elements.buttons;

import java.util.function.Consumer;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class ButtonElement extends AbstractButton<ButtonElement> {

    private Consumer<ButtonElement> onPress;
    private Consumer<ClickInfo> onClick;

    protected ButtonElement(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    public static ButtonElement create(int width, int height) {
        return new ButtonElement(0, 0, width, height, Component.empty());
    }

    protected ButtonElement self() {
        return this;
    }

    @Override
    public void onClick(MouseButtonEvent click, boolean doubled) {
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

    public record ClickInfo(MouseButtonEvent click, boolean doubled) { }
}
