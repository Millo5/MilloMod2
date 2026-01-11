package millo.millomod2.client.hypercube.data;

import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public enum LineStarterType {
    NONE(Styles.ANY, "txt"),
    PLAYER_EVENT(Styles.PLAYER_EVENT, "pev"),
    ENTITY_EVENT(Styles.ENTITY_EVENT, "eev"),
    PROCESS(Styles.PROCESS, "prc"),
    FUNCTION(Styles.FUNCTION, "fun");

    private final Styles style;
    private final String extension;

    LineStarterType(Styles style, String extension) {
        this.style = style;
        this.extension = extension;
    }

    public Styles getStyle() {
        return style;
    }

    public String getExtension() {
        return extension;
    }

    public int getColor() {
        return style.getColor();
    }

    public MutableText getPrefix() {
        return Text.literal(name().toLowerCase()).setStyle(style.getStyle());
    }
}
