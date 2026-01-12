package millo.millomod2.menu.elements.buttons;

import millo.millomod2.client.MilloMod;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.flex.FlexElement;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class DropDownElement extends AbstractButton<DropDownElement> {

    private final ListElement optionsList;
    private int offsetY = 0;

    protected DropDownElement(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);

        optionsList = ListElement.create(width, 100)
                .background(0xCC222222);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        optionsList.setWidth(width);
        for (ClickableWidget child : optionsList.getChildren()) {
            child.setWidth(width);
        }
    }

    @Override
    public void onClick(Click click, boolean doubled) {
        if (MilloMod.MC.currentScreen instanceof Menu menu) {
            menu.openContextMenu(optionsList, getX(), getBottom() + offsetY);
        }
    }

    public static DropDownElement create(int width, int height) {
        return new DropDownElement(0, 0, width, height, Text.empty());
    }

    @Override
    protected DropDownElement self() {
        return this;
    }

    public DropDownElement addOption(Text label, Consumer<ButtonElement> onSelect) {
        ButtonElement optionButton = ButtonElement.create(optionsList.getWidth(), 14)
                .message(label)
                .onPress(button -> {
                    onSelect.accept(button);
                    if (MilloMod.MC.currentScreen instanceof Menu menu) {
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

    public DropDownElement addHeader(Text text) {
        FlexElement container = FlexElement.create(optionsList.getWidth(), 15)
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
