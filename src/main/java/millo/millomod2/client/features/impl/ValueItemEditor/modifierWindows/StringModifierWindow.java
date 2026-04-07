package millo.millomod2.client.features.impl.ValueItemEditor.modifierWindows;

import millo.millomod2.client.features.impl.ValueItemEditor.ModifierWindow;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextFieldElement;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

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
    protected ClickableWidget getElement() {
        field = new TextFieldElement(200, 20, Text.literal(value));
        field.setChangedListener(str -> {
            value = str;
            setter.accept(str);
        });
        field.setCursor(value.length(), false);
        return field;
    }

    @Override
    protected ClickableWidget getDefaultFocus() {
        return field;
    }

    @Override
    protected String getTitle() {
        return "Simple Value";
    }

    @Override
    public void applyToItem(ItemStack stack) {
        stack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(value).setStyle(style.getStyle().withItalic(false)));
    }

}
