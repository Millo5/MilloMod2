package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;

public class SimpleArgument extends Argument<SimpleArgument> {

    private final Type type;
    private String value;

    public SimpleArgument(Type type) {
        this.type = type;
    }

    @Override
    public Text getDisplayText() {
        return switch (type) {
            case TEXT -> Text.literal("\"" + value + "\"").setStyle(Styles.TEXT.getStyle());
            case NUMBER -> Text.literal(value).setStyle(Styles.NUMBER.getStyle());
        };
    }

    @Override
    public SimpleArgument from(ArgumentItemData data) {
        this.value = data.name;
        return this;
    }

    public enum Type {
        TEXT,
        NUMBER
    }
}
