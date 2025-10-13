package millo.millomod2.client.config.value;

import millo.millomod2.client.config.ConfigValue;
import millo.millomod2.client.config.ConfigValuePair;
import net.minecraft.text.Text;
import net.sapfii.sapscreens.screens.widgets.*;

public class ChoiceConfigValue extends ConfigValue<String> {

    private final String[] choices;

    private WidgetListBox folder;

    public ChoiceConfigValue(String defaultValue, String[] choices) {
        super(defaultValue);
        this.choices = choices;
    }

    @Override
    public Widget<?> createWidget() {
//        return new TextDisplayWidget(Text.literal(key + ": " + value + "(" + String.join(", ", choices) +")"),
//                10, Widget.Alignment.LEFT);

        // ⏺ ○

        folder = new WidgetListFolder()
                .withTitle(Text.translatable(value))
                .useParentDimensions(true, false)
                .withDimensions(10, 40 * Math.min(choices.length, 4));

        for (String choice : choices) {
            folder.addWidget(new ButtonWidget(Text.literal(choice.equals(value) ? "⏺ " : "○ ").append(Text.translatable(choice)), buttonWidget -> {
                setValue(choice);
            }));
        }
        return folder;
    }

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

    @Override
    public void setValue(String value) {
        super.setValue(value);
        if (folder == null) return;
        ((WidgetListFolder) folder).withTitle(Text.translatable(value));
        for (int i = 0; i < choices.length; i++) {
            String choice = choices[i];
            Widget<? extends Widget<?>> widget = folder.getChildren().get(i);
            if (widget instanceof ButtonWidget buttonWidget) {
                buttonWidget.withText(Text.literal(choice.equals(value) ? "⏺ " : "○ ").append(Text.translatable(choice)));
            }
        }
    }
}
