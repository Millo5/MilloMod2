package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.model.arguments.VectorArgumentModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;

public class VectorArgumentSegment extends SimpleSegment<VectorArgumentModel> {

    public VectorArgumentSegment(VectorArgumentModel model) {
        super(model);
    }

    @Override
    TextElement createContent(VectorArgumentModel model) {
        return new SimpleArgumentBuilder("<" + model.getX() + ", " + model.getY() + ", " + model.getZ() + ">")
                .style(Styles.VECTOR)
                .onClickCmd("/vec get " + model.getX() + " " + model.getY() + " " + model.getZ())
                .build();
    }

    @Override
    public Class<VectorArgumentModel> getModelClass() {
        return VectorArgumentModel.class;
    }

}
