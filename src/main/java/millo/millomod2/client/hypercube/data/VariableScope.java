package millo.millomod2.client.hypercube.data;

import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Style;

public enum VariableScope {

    UNSAVED(Styles.UNSAVED.getStyle()),
    SAVED(Styles.SAVED.getStyle()),
    LOCAL(Styles.LOCAL.getStyle()),
    LINE(Styles.LINE.getStyle());

    private final Style style;
    VariableScope(Style style) {
        this.style = style;
    }

    public Style getStyle() {
            return style;
        }

}
