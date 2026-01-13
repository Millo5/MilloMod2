package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class VariableArgument extends Argument<VariableArgument> {

    private String name;
    private Scope scope;

    @Override
    public Text getDisplayText() {
        return Text.literal(name).setStyle(Styles.VAR.getStyle());
    }

    @Override
    public Text getTooltip() {
        return Text.literal(scope.name()).setStyle(scope.getStyle());
    }

    @Override
    public VariableArgument from(ArgumentItemData data) {
        this.name = data.name;
        this.scope = Scope.valueOf(data.scope.toUpperCase());
        return this;
    }


    @SuppressWarnings("unused")
    public enum Scope {
        UNSAVED(Styles.UNSAVED.getStyle()),
        SAVED(Styles.SAVED.getStyle()),
        LOCAL(Styles.LOCAL.getStyle()),
        LINE(Styles.LINE.getStyle());

        private final Style style;
        Scope(Style style) {
            this.style = style;
        }

        public Style getStyle() {
            return style;
        }
    }

}
