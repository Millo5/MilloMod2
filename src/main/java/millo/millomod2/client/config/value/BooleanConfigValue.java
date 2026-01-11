package millo.millomod2.client.config.value;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.config.ConfigValue;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;

public class BooleanConfigValue extends ConfigValue<Boolean> {

    public BooleanConfigValue(Boolean defaultValue) {
        super(defaultValue);
    }

    @Override
    public ClickableWidget createWidget() {
//        return TextElement.create("Button!");
        return ButtonWidget.builder(
                MilloMod.translatable(value ? "true" : "false"),
                button -> {
                    value = !value;
                    button.setMessage(MilloMod.translatable(value ? "true" : "false"));
                }
        ).build();
    }
//
//    @Override
//    public Widget<?> createWidget() {
//        return new ButtonWidget()
//                .withText(MilloMod.translatable(value ? "true" : "false"))
//                .withClickEvent((widget, x, y, active) -> {
//                    if (!widget.hovered()) return;
//                    value = !value;
//                    widget.withText(MilloMod.translatable(value ? "true" : "false"));
//                });
//    }

    @Override
    public void deserialize(Object obj) {
        if (obj instanceof Boolean b) setValue(b);
    }


}
