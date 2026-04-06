package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineSegment;
import millo.millomod2.client.hypercube.model.arguments.ParameterArgumentModel;
import millo.millomod2.client.util.style.Styles;

public class ParameterArgumentSegment extends CodeLineSegment<ParameterArgumentModel> {

    public ParameterArgumentSegment(ParameterArgumentModel model) {
        super(model);
    }

    @Override
    public void buildVisual(CodeLineElement lineElement) {
        Styles style;
        try {
            style = Styles.valueOf(model.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            style = Styles.PARAMETER;
        }

        if (model.isOptional()) lineElement.addChild(text("[", style));
        lineElement.addChild(text(model.getType(), style));
        if (model.isPlural()) lineElement.addChild(text("...", style));
        lineElement.addChild(text(" "));

        lineElement.addChild(new SimpleArgumentBuilder(model.getName())
                .style(style)
                .onClickCmd("/var " + model.getName() + " -i")
                .build());

        if (model.isOptional()) {
            if (model.getDefaultValue() != null) {
                lineElement.addChild(text(" = ", style));
                CodeLineSegment.create(model.getDefaultValue()).buildVisual(lineElement);
            }
            lineElement.addChild(text("]", style));
        }

    }

    @Override
    public Class<ParameterArgumentModel> getModelClass() {
        return ParameterArgumentModel.class;
    }


}
