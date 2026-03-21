package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.model.arguments.HintArgumentModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;

public class HintArgumentSegment extends SimpleSegment<HintArgumentModel> {

    public HintArgumentSegment(HintArgumentModel model) {
        super(model);
    }

    @Override
    TextElement createContent(HintArgumentModel model) {
        return new SimpleArgumentBuilder("Hint")
                .style(Styles.COMMENT)
                .build();
    }

    @Override
    public Class<HintArgumentModel> getModelClass() {
        return HintArgumentModel.class;
    }

}
