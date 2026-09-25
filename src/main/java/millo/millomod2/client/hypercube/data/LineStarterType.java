package millo.millomod2.client.hypercube.data;

import millo.millomod2.client.util.style.Styles;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum LineStarterType {
    NONE(Styles.ANY, "txt"),
    PLAYER_EVENT(Styles.PLAYER_EVENT, "pev"),
    ENTITY_EVENT(Styles.ENTITY_EVENT, "eev"),
    PROCESS(Styles.PROCESS, "prc"),
    FUNCTION(Styles.FUNCTION, "fun"),
    GAME_EVENT(Styles.GAME_EVENT, "gev")
    ;

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

    public MutableComponent getPrefix() {
        return Component.literal(name().toLowerCase()).setStyle(style.getStyle());
    }
}
