package millo.millomod2.client.config.value;

import millo.millomod2.client.config.ConfigValue;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.client.gui.widget.ClickableWidget;

public class StringConfigValue extends ConfigValue<String> {

    public StringConfigValue(String defaultValue) {
        super(defaultValue);
    }

    @Override
    public ClickableWidget createWidget() {
        return TextElement.create(value);
    }

//    @Override
//    public Widget<?> createWidget() {
//        return TextWidget.create().withText(Text.literal(value));
//    }

    @Override
    public void deserialize(Object obj) {
        if (obj instanceof String s) setValue(s);
    }

}
