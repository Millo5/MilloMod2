package millo.millomod2.client.config.value;

import com.google.gson.internal.LinkedTreeMap;
import millo.millomod2.client.MilloMod;
import millo.millomod2.client.config.ConfigValue;
import millo.millomod2.client.features.FeaturePosition;
import millo.millomod2.client.features.addons.Positional;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import net.minecraft.client.gui.widget.ClickableWidget;

public class PositionalConfigValue extends ConfigValue<FeaturePosition> {

    private final Positional feat;

    public PositionalConfigValue(Positional feat, FeaturePosition defaultValue) {
        super(defaultValue);
        this.feat = feat;
    }

    @Override
    public ClickableWidget createWidget() {
        return ButtonElement.create(200, 20)
                .message(MilloMod.translatable("reset"))
                .onPress((b) -> {
                    setValue(feat.defaultPosition());
                });
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
