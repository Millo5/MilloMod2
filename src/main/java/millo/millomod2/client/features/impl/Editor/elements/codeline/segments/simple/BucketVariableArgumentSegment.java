package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.model.arguments.BucketVariableArgumentModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.text.Text;

public class BucketVariableArgumentSegment extends SimpleSegment<BucketVariableArgumentModel> {

    public BucketVariableArgumentSegment(BucketVariableArgumentModel model) {
        super(model);
    }

    @Override
    TextElement createContent(BucketVariableArgumentModel model) {
        StringBuilder command = new StringBuilder("/bvar ");
        if (model.usesAlias()) command.append(model.getNamespaceAlias()).append(" ");
        command.append(model.getKey()).append(" ").append(model.getName());

        var tooltip = Text.literal("Key: " + model.getKey());
        if (model.usesAlias()) tooltip.append("\nAlias: " + model.getNamespaceAlias());

        return new SimpleArgumentBuilder(model.getName())
                .style(Styles.BUCKET)
                .tooltip(tooltip)
                .onClickCmd(command.toString())
                .build();
    }

    @Override
    public Class<BucketVariableArgumentModel> getModelClass() {
        return BucketVariableArgumentModel.class;
    }

}
