package millo.millomod2.menu.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class ConfirmationElement extends ListElement {

    private final Runnable onConfirm;

    public ConfirmationElement(Text title, Text message, Runnable onConfirm) {
        super(0, 0, 400, 200);

        this.onConfirm = onConfirm;

        background(0xCC000000);
        crossAlign(CrossAxisAlignment.STRETCH);
        gap(15);

        FlexElement<?> buttonContainer = FlexElement.create(width, 20)
                    .direction(ElementDirection.ROW)
                    .mainAlign(MainAxisAlignment.SPACE_BETWEEN)
                    .crossAlign(CrossAxisAlignment.CENTER);

        ButtonElement confirmButton = ButtonElement.create(80, 20)
                .hoverBackground(0xFF000000)
                .onPress(button -> {
                    onConfirm.run();
                    if (MilloMod.MC.currentScreen instanceof Menu menu) {
                        menu.closeContextMenu();
                    }
                })
                .message(Text.literal("Confirm"));

        ButtonElement cancelButton = ButtonElement.create(80, 20)
                .hoverBackground(0xFF000000)
                .onPress(button -> {
                    if (MilloMod.MC.currentScreen instanceof Menu menu) {
                        menu.closeContextMenu();
                    }
                })
                .message(Text.literal("Cancel"));

        buttonContainer.addChildren(cancelButton, confirmButton);

        addChildren(
                TextElement.create(title)
                        .offset(0, 10)
                        .align(TextElement.TextAlignment.CENTER),
                TextElement.create(message)
                        .offset(10, 5)
                        .setMaxWidth(width - 20, TextWidget.TextOverflow.SCROLLING),
                buttonContainer
        );
    }

}
