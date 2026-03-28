package millo.millomod2.client.config.value;

import com.google.gson.internal.LinkedTreeMap;
import millo.millomod2.client.config.ConfigValue;
import millo.millomod2.client.features.FeaturePosition;
import net.minecraft.client.gui.widget.ClickableWidget;

public class PositionalConfigValue extends ConfigValue<FeaturePosition> {

    public PositionalConfigValue(FeaturePosition defaultValue) {
        super(defaultValue);
    }

    @Override
    public ClickableWidget createWidget() {
        return null;
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public void deserialize(Object obj) {
        if (obj instanceof LinkedTreeMap<?, ?> map) {
            value.setX((int) (double) map.get("x"));
            value.setY((int) (double) map.get("y"));
            value.setAnchor(FeaturePosition.Anchor.valueOf((String) map.get("anchor")));
        }
    }
}
