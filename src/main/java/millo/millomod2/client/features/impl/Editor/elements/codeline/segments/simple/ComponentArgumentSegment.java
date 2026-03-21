package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.model.arguments.ComponentArgumentModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;

public class ComponentArgumentSegment extends SimpleSegment<ComponentArgumentModel> {

    public ComponentArgumentSegment(ComponentArgumentModel model) {
        super(model);
    }

    @Override
    TextElement createContent(ComponentArgumentModel model) {
        return new SimpleArgumentBuilder("T\"" + model.getValue() + "\"")
                .style(Styles.COMPONENT)
                .onClickCmd("/txt " + model.getValue())
                .build();
    }

    @Override
    public Class<ComponentArgumentModel> getModelClass() {
        return ComponentArgumentModel.class;
    }

}
