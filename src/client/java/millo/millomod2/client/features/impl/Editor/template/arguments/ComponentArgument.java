package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;

public class ComponentArgument extends Argument<ComponentArgument> {

    private String component;

    @Override
    protected Text getDisplayText() {
        return Text.literal(component).setStyle(Styles.COMPONENT.getStyle());
    }

    @Override
    public ComponentArgument from(ArgumentItemData data) {
        this.component = data.name;
        return this;
    }
}
