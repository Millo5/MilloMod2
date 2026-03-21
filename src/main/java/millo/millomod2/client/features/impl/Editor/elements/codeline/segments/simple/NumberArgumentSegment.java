package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.model.arguments.NumberArgumentModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;

public class NumberArgumentSegment extends SimpleSegment<NumberArgumentModel> {

    public NumberArgumentSegment(NumberArgumentModel model) {
        super(model);
    }

    @Override
    TextElement createContent(NumberArgumentModel model) {
        return new SimpleArgumentBuilder(model.getValue())
                .style(Styles.NUMBER)
                .onClickCmd("/num " + model.getValue())
                .build();
    }

    @Override
    public Class<NumberArgumentModel> getModelClass() {
        return NumberArgumentModel.class;
    }

}
