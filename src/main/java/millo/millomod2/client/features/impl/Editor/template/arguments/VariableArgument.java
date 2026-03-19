package millo.millomod2.client.features.impl.Editor.template.arguments;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.data.VariableScope;
import millo.millomod2.client.hypercube.template.ArgumentItemData;
import millo.millomod2.client.util.PlayerUtil;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class VariableArgument extends Argument<VariableArgument> {

    private String name;
    private VariableScope scope;

    @Override
    public Text getDisplayText() {
        return Text.literal(name).setStyle(Styles.VAR.getStyle())
                .append(Text.literal("°").setStyle(scope.getStyle()));
    }

    @Override
    public Text getTooltip() {
        return Text.literal(scope.name()).setStyle(scope.getStyle());
    }

    @Override
    public Supplier<Boolean> getOnClick() {
        return () -> {
            String cmd = "var " + name + switch (scope) {
                case SAVED -> " -s";
                case LOCAL -> " -l";
                case LINE -> " -i";
                default -> "";
            };
            PlayerUtil.sendCommand(cmd);
            return true;
        };
    }

    @Override
    public VariableArgument from(ArgumentItemData data) {
        this.name = data.name;
        this.scope = VariableScope.valueOf(data.scope.toUpperCase());
        return this;
    }

}
