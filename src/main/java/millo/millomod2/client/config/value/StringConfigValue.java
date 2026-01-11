package millo.millomod2.client.config.value;

import millo.millomod2.client.config.ConfigValue;
import millo.millomod2.menu.elements.TextFieldElement;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class StringConfigValue extends ConfigValue<String> {

    public StringConfigValue(String defaultValue) {
        super(defaultValue);
    }

    @Override
    public ClickableWidget createWidget() {
        TextFieldElement element = new TextFieldElement(150, 20, Text.literal(value));
        element.setChangedListener(this::setValue);
        return element;
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
