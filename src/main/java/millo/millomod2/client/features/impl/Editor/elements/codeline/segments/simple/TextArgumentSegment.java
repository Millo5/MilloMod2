package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.model.arguments.TextArgumentModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;

public class TextArgumentSegment extends SimpleSegment<TextArgumentModel> {

    public TextArgumentSegment(TextArgumentModel model) {
        super(model);
    }

    @Override
    TextElement createContent(TextArgumentModel model) {
        return new SimpleArgumentBuilder("\"" + model.getValue() + "\"")
                .style(Styles.TEXT)
                .onClickCmd("/str " + model.getValue())
                .build();
    }

    @Override
    public Class<TextArgumentModel> getModelClass() {
        return TextArgumentModel.class;
    }

}
