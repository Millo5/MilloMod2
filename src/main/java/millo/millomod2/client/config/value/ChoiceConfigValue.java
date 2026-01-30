package millo.millomod2.client.config.value;

import millo.millomod2.client.config.ConfigValue;
import millo.millomod2.menu.elements.buttons.DropDownElement;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class ChoiceConfigValue extends ConfigValue<String> {

    private final String[] choices;

//    private FolderWidget folder;

    public ChoiceConfigValue(String defaultValue, String[] choices) {
        super(defaultValue);
        this.choices = choices;
    }

    @Override
    public ClickableWidget createWidget() {
        DropDownElement element = DropDownElement.create(150, 20)
                .message(Text.literal(value))
                .background(0x33000000)
                .offsetY(3);

        for (String choice : choices) {
            element.addOption(Text.literal(choice), (button) -> {
                setValue(choice);
                element.setMessage(Text.literal(choice));
            });
        }
        return element;

//        return TextElement.create("Choice Config Value");
    }

//    @Override
//    public Widget<?> createWidget() {
////        return new TextDisplayWidget(Text.literal(key + ": " + value + "(" + String.join(", ", choices) +")"),
////                10, Widget.Alignment.LEFT);
//
//        // ⏺ ○
//
//        folder = new FolderWidget()
//                .withTitle(MilloMod.translatable("choice", value))
//                .withDimensions(10, 40 * Math.min(choices.length, 4), true);
//
//        for (String choice : choices) {
//            folder.addWidget(new ButtonWidget()
//                    .withText(Text.literal(choice.equals(value) ? "⏺ " : "○ ").append(MilloMod.translatable("choice", choice)))
//                    .withClickEvent((widget, x, y, active) -> {
//                        if (!widget.hovered()) return;
//                        setValue(choice);
//                    })
//                    .withDimensions(0, 40, true)
//            );
//        }
//        refreshButtons();
//        return folder;
//    }

    @Override
    public void deserialize(Object obj) {
        if (obj instanceof String s) {
            for (String choice : choices) {
                if (choice.equals(s)) {
                    setValue(s);
                    return;
                }
            }
        }
    }

//    @Override
//    public void setValue(String value) {
//        super.setValue(value);
////        refreshButtons();
//    }

//
//    private void refreshButtons() {
//        if (folder == null) return;
//
//        folder.withTitle(MilloMod.translatable("choice", value));
//        for (int i = 0; i < choices.length; i++) {
//            String choice = choices[i];
//            Widget<? extends Widget<?>> widget = folder.getWidgets().get(i);
//            if (widget instanceof ButtonWidget buttonWidget) {
//                buttonWidget.withText(Text.literal(choice.equals(value) ? "⏺ " : "○ ").append(MilloMod.translatable("choice", choice)));
//            }
//        }
//    }
}
