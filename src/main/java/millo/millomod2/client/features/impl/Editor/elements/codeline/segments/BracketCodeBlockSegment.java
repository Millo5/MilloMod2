package millo.millomod2.client.features.impl.Editor.elements.codeline.segments;

import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineSegment;
import millo.millomod2.client.features.impl.Editor.elements.codeline.components.BlockPrefixComponent;
import millo.millomod2.client.features.impl.Editor.elements.codeline.components.IndentationComponent;
import millo.millomod2.client.hypercube.model.codeblocks.BracketCodeBlockModel;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.util.Identifier;

public class BracketCodeBlockSegment extends CodeLineSegment<BracketCodeBlockModel> {

    private static final Identifier BLOCK_ID = Identifier.of("minecraft", "piston");

    private final TextElement text;

    public BracketCodeBlockSegment(BracketCodeBlockModel model) {
        super(model);
        addComponent(new IndentationComponent(
                model.getDirection() == BracketCodeBlockModel.BracketDirection.CLOSE ? -1 : 0,
                model.getDirection() == BracketCodeBlockModel.BracketDirection.OPEN ? 1 : 0
        ));
        addComponent(new BlockPrefixComponent(BLOCK_ID));

        this.text = text(model.getDirection() == BracketCodeBlockModel.BracketDirection.OPEN ? "{" : "}");
    }

    @Override
    public Class<BracketCodeBlockModel> getModelClass() {
        return BracketCodeBlockModel.class;
    }

    @Override
    public void buildVisual(CodeLineElement lineElement) {
        lineElement.addChild(text);
    }
}
