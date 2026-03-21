package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.model.arguments.VariableArgumentModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class VariableArgumentSegment extends SimpleSegment<VariableArgumentModel> {

    // TODO: right click to highlight all occurrences of this variable in the code

    public VariableArgumentSegment(VariableArgumentModel model) {
        super(model);
    }

    @Override
    TextElement createContent(VariableArgumentModel model) {
        MutableText name = Text.literal(model.getName()).setStyle(Styles.VAR.getStyle())
                .append(Text.literal("°").setStyle(model.getScope().getStyle()));

        String cmd = "var " + model.getName() + switch (model.getScope()) {
            case SAVED -> " -s";
            case LOCAL -> " -l";
            case LINE -> " -i";
            default -> "";
        };

        return new SimpleArgumentBuilder(name)
                .tooltip(Text.literal(model.getScope().name()).setStyle(model.getScope().getStyle()))
                .onClickCmd(cmd)
                .build();
    }

    @Override
    public Class<VariableArgumentModel> getModelClass() {
        return VariableArgumentModel.class;
    }

}
