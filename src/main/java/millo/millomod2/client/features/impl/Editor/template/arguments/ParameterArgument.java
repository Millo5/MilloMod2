package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;

public class ParameterArgument extends Argument<ParameterArgument> {

    private String parameter;
    private String type;
    private boolean optional;
    private boolean plural;

    @Override
    public Text getDisplayText() {
        StringBuilder display = new StringBuilder();
        if (optional) display.append("[");
        display.append(type);
        if (plural) display.append("...");
        display.append(" ").append(parameter);
        if (optional) display.append("]");

        Styles style;
        try {
            style = Styles.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            style = Styles.PARAMETER;
        }

        return Text.literal(display.toString()).setStyle(style.getStyle());
    }

    @Override
    public ParameterArgument from(ArgumentItemData data) {
        this.parameter = data.name;
        this.type = data.type;
        this.optional = data.optional;
        this.plural = data.plural;
        return this;
    }
}
