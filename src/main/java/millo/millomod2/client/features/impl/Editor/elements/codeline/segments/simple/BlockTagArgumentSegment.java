package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.hypercube.model.arguments.BlockTagArgumentModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.text.Text;

public class BlockTagArgumentSegment extends SimpleSegment<BlockTagArgumentModel> {

    public BlockTagArgumentSegment(BlockTagArgumentModel model) {
        super(model);
    }

    @Override
    TextElement createContent(BlockTagArgumentModel model) {
        return new SimpleArgumentBuilder(model.getOption())
                .style(Styles.BLOCK_TAG)
                .tooltip(Text.literal(model.getTag()).setStyle(Styles.COMMENT.getStyle()))
                .build();
    }

    @Override
    public Class<BlockTagArgumentModel> getModelClass() {
        return BlockTagArgumentModel.class;
    }

}
