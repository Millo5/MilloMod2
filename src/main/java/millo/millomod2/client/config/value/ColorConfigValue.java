package millo.millomod2.client.config.value;

import millo.millomod2.client.config.ConfigValue;
import millo.millomod2.menu.elements.TextFieldElement;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public class ColorConfigValue extends ConfigValue<Integer> {

    public ColorConfigValue(int defaultValue) {
        super(defaultValue);
    }

    @Override
    public AbstractWidget createWidget() {
        TextFieldElement element = new TextFieldElement(150, 20, Component.literal("#" + Integer.toHexString(value).toUpperCase()));
        element.setResponder((str) -> {
            try {
                if (str.startsWith("#")) str = str.substring(1);
                setValue(Integer.parseInt(str, 16));
            } catch (NumberFormatException ignored) {}
        });
        return element;
    }

    @Override
    public void deserialize(Object obj) {
        if (obj instanceof Integer s) setValue(s);
    }

}
