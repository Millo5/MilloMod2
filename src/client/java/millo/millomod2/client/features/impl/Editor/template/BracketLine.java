package millo.millomod2.client.features.impl.Editor.template;

import net.minecraft.text.Text;

public class BracketLine implements CodeLine {

    private final boolean open;
    private final String type;

    public BracketLine(boolean open, String type) {
        this.open = open;
        this.type = type;
    }

    @Override
    public Text getDisplayText() {
        return Text.literal(open ? "{" : "}");
    }
}
