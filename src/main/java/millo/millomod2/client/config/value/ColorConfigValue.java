package millo.millomod2.client.config.value;

import millo.millomod2.client.config.ConfigValue;
import millo.millomod2.menu.elements.TextFieldElement;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class ColorConfigValue extends ConfigValue<Integer> {

    public ColorConfigValue(int defaultValue) {
        super(defaultValue);
    }

    @Override
    public ClickableWidget createWidget() {
        TextFieldElement element = new TextFieldElement(150, 20, Text.literal("#" + Integer.toHexString(value).toUpperCase()));
        element.setChangedListener((str) -> {
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
