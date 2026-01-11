package millo.millomod2.client.config.value;

import millo.millomod2.client.rendering.gui.NumberSliderElement;
import net.minecraft.client.gui.widget.ClickableWidget;

public class IntegerRangeConfigValue extends IntegerConfigValue {

    private final int min, max;

    public IntegerRangeConfigValue(Integer defaultValue, int min, int max) {
        super(defaultValue);
        this.min = min;
        this.max = max;
    }

    @Override
    public void setValue(Integer value) {
        super.setValue(Math.max(min, Math.min(max, value)));
    }

    @Override
    public ClickableWidget createWidget() {
        return NumberSliderElement.create(value, min, max, 1, (value) -> setValue(value.intValue()));
    }

    //    @Override
//    public Widget<?> createWidget() {
//        return new WidgetNumberSlider(value, min, max, 1, (value) -> setValue(value.intValue()));
//    }
}
