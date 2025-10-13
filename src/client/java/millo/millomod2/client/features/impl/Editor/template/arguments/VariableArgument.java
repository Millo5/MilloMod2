package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import net.minecraft.text.Text;

public class VariableArgument extends Argument<VariableArgument> {

    private String name;
    private Scope scope;

    @Override
    protected Text getDisplayText() {
        return Text.literal(scope.name().toLowerCase() + "." + name);
    }

    @Override
    public VariableArgument from(ArgumentItemData data) {
        this.name = data.name;
        this.scope = Scope.valueOf(data.scope.toUpperCase());
        return this;
    }

    public enum Scope {
        UNSAVED,
        SAVE,
        LOCAL,
        LINE
    }

}
