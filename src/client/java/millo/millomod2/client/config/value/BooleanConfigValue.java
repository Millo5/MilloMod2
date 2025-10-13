package millo.millomod2.client.config.value;

import millo.millomod2.client.config.ConfigValue;
import net.minecraft.text.Text;
import net.sapfii.sapscreens.screens.widgets.ButtonWidget;
import net.sapfii.sapscreens.screens.widgets.Widget;

public class BooleanConfigValue extends ConfigValue<Boolean> {

    public BooleanConfigValue(Boolean defaultValue) {
        super(defaultValue);
    }

    @Override
    public Widget<?> createWidget() {
        return new ButtonWidget(Text.translatable(value ? "true" : "false"), buttonWidget -> {
            value = !value;
            buttonWidget.withText(Text.translatable(value ? "true" : "false"));
        });
    }

    @Override
    public void deserialize(Object obj) {
        if (obj instanceof Boolean b) setValue(b);
    }


}
