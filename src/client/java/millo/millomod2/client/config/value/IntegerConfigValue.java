package millo.millomod2.client.config.value;

import millo.millomod2.client.config.ConfigValue;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.client.gui.widget.ClickableWidget;

public class IntegerConfigValue extends ConfigValue<Integer> {

    public IntegerConfigValue(Integer defaultValue) {
        super(defaultValue);
    }

    @Override
    public ClickableWidget createWidget() {
        return TextElement.create(String.valueOf(value));
    }

//    @Override
//    public Widget<?> createWidget() {
//        return TextWidget.create().withText(Text.literal(""+value));
//    }

    @Override
    public void deserialize(Object obj) {
        if (obj instanceof Number n) setValue(n.intValue());
    }

}
