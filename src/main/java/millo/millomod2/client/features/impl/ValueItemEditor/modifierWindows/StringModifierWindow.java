package millo.millomod2.client.features.impl.ValueItemEditor.modifierWindows;

import millo.millomod2.client.features.impl.ValueItemEditor.ModifierWindow;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextFieldElement;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;

public class StringModifierWindow extends ModifierWindow {

    private String value;
    private final Consumer<String> setter;
    private final Styles style;
    private TextFieldElement field;

    public StringModifierWindow(String value, Consumer<String> setter, Styles style) {
        this.value = value;
        this.setter = setter;
        this.style = style;
    }

    @Override
    protected AbstractWidget getElement() {
        field = new TextFieldElement(200, 20, Component.literal(value));
        field.setMaxLength(10000);
        field.setValue(value);
        field.setResponder(str -> {
            value = str;
            setter.accept(str);
        });
        field.moveCursorTo(value.length(), false);
        return field;
    }

    @Override
    protected AbstractWidget getDefaultFocus() {
        return field;
    }

    @Override
    protected String getTitle() {
        return "Simple Value";
    }

    @Override
    public void applyToItem(ItemStack stack) {
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(value).setStyle(style.getStyle().withItalic(false)));
    }

}
