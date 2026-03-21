package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.model.arguments.ParameterArgumentModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;

public class ParameterArgumentSegment extends SimpleSegment<ParameterArgumentModel> {

    public ParameterArgumentSegment(ParameterArgumentModel model) {
        super(model);
    }

    @Override
    TextElement createContent(ParameterArgumentModel model) {

        StringBuilder display = new StringBuilder();
        if (model.isOptional()) display.append("[");
        display.append(model.getType());
        if (model.isPlural()) display.append("...");
        display.append(" ").append(model.getName());
        if (model.isOptional()) {
            if (model.getDefaultValue() != null) display.append(" = ").append(model.getDefaultValue());
            display.append("]");
        }

        Styles style;
        try {
            style = Styles.valueOf(model.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            style = Styles.PARAMETER;
        }

        return new SimpleArgumentBuilder(display.toString())
                .style(style)
                .onClickCmd("/var " + model.getName() + " -i")
                .build();
    }

    @Override
    public Class<ParameterArgumentModel> getModelClass() {
        return ParameterArgumentModel.class;
    }

}
