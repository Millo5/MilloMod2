package millo.millomod2.client.config.value;

import millo.millomod2.client.config.ConfigValue;
import millo.millomod2.menu.elements.TextFieldElement;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class FloatConfigValue extends ConfigValue<Float> {

    public FloatConfigValue(Float defaultValue) {
        super(defaultValue);
    }

    @Override
    public ClickableWidget createWidget() {
        String value = String.valueOf(this.value).replaceAll(",", ".");
        TextFieldElement element = new TextFieldElement(150, 20, Text.literal(value));
        element.setText(value);
        element.setChangedListener((str) -> {
            try {
                setValue(Float.parseFloat(str));
            } catch (NumberFormatException e) {
                // Ignore invalid input
            }
        });
        return element;
    }

    @Override
    public void deserialize(Object obj) {
        if (obj instanceof Number n) setValue(n.floatValue());
    }

}
