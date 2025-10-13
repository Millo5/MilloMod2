package millo.millomod2.client.config.value;

import millo.millomod2.client.config.ConfigValue;
import net.minecraft.text.Text;
import net.sapfii.sapscreens.screens.widgets.TextDisplayWidget;
import net.sapfii.sapscreens.screens.widgets.Widget;

public class IntegerConfigValue extends ConfigValue<Integer> {

    public IntegerConfigValue(Integer defaultValue) {
        super(defaultValue);
    }

    @Override
    public Widget<?> createWidget() {
        return new TextDisplayWidget(Text.literal(""+value), 10, Widget.Alignment.LEFT);
    }

    @Override
    public void deserialize(Object obj) {
        if (obj instanceof Number n) setValue(n.intValue());
    }

}
