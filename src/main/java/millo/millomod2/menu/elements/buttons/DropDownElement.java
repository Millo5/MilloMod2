package millo.millomod2.menu.elements.buttons;

import millo.millomod2.client.MilloMod;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.flex.FlexElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.joml.Vector2f;

import java.util.function.Consumer;

public class DropDownElement extends AbstractButton<DropDownElement> {

    private final ListElement optionsList;
    private int offsetY = 0;

    private int screenX = 0;
    private int screenBottom = 0;

    protected DropDownElement(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);

        optionsList = ListElement.create(width, 100)
                .background(0xCC222222);
    }

    @Override
    protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
        var pos = context.pose().transformPosition(getX(), getBottom(), new Vector2f());
        screenX = (int) pos.x;
        screenBottom = (int) pos.y;
        super.renderWidget(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        optionsList.setWidth(width);
        for (AbstractWidget child : optionsList.getChildren()) {
            child.setWidth(width);
        }
    }

    @Override
    public void onClick(MouseButtonEvent click, boolean doubled) {
        if (MilloMod.MC.screen instanceof Menu menu) {
            menu.openContextMenu(optionsList, screenX, screenBottom + offsetY);
        }
    }

    public static DropDownElement create(int width, int height) {
        return new DropDownElement(0, 0, width, height, Component.empty());
    }

    @Override
    protected DropDownElement self() {
        return this;
    }

    public DropDownElement addOption(Component label, Consumer<ButtonElement> onSelect) {
        ButtonElement optionButton = ButtonElement.create(optionsList.getWidth(), 14)
                .message(label)
                .onPress(button -> {
                    onSelect.accept(button);
                    if (MilloMod.MC.screen instanceof Menu menu) {
                        menu.closeContextMenu();
                    }
                });
        optionsList.addChild(optionButton);
        return this;
    }

    public ListElement getOptions() {
        return optionsList;
    }

    public DropDownElement addSpacer() {
        optionsList.addChild(ButtonElement.create(optionsList.getWidth(), 1).background(0xAA666666));
        return this;
    }

    public DropDownElement offsetY(int offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    public DropDownElement addHeader(Component text) {
        FlexElement<?> container = FlexElement.create(optionsList.getWidth(), 15)
                .padding(2)
                .background(0);
        container.addChild(
                TextElement.create(text)
                        .align(TextElement.TextAlignment.LEFT)
        );
        optionsList.addChild(container);
        return this;
    }
}
