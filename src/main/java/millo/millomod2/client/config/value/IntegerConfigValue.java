package millo.millomod2.client.config.value;

import millo.millomod2.client.config.ConfigValue;
import millo.millomod2.menu.elements.TextFieldElement;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public class IntegerConfigValue extends ConfigValue<Integer> {

    public IntegerConfigValue(Integer defaultValue) {
        super(defaultValue);
    }

    @Override
    public AbstractWidget createWidget() {
        TextFieldElement element = new TextFieldElement(150, 20, Component.literal(String.valueOf(value)));
        element.setResponder((str) -> {
            try {
                setValue(Integer.parseInt(str));
            } catch (NumberFormatException e) {
                // Ignore invalid input
            }
        });
        return element;
    }

    @Override
    public void deserialize(Object obj) {
        if (obj instanceof Number n) setValue(n.intValue());
    }

}
