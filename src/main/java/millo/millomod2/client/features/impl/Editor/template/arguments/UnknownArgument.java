package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;

public class UnknownArgument extends Argument<UnknownArgument> {

    private final String id;

    public UnknownArgument(String id) {
        this.id = id;
    }

    @Override
    public Text getDisplayText() {
        return Text.literal(id).setStyle(Styles.BUG.getStyle());
    }

    @Override
    public UnknownArgument from(ArgumentItemData data) {
        return this;
    }
}
