package millo.millomod2.client.features.impl.Editor.template.lines;

import millo.millomod2.client.features.impl.Editor.elements.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.template.CodeLine;
import millo.millomod2.client.features.impl.Editor.template.CodeLineIndentationMutation;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.util.Identifier;

public class BracketLine implements CodeLine, CodeLineIndentationMutation {

    private final boolean open;
    private final String type;

    public BracketLine(boolean open, String type) {
        this.open = open;
        this.type = type;
    }

    public int getIndentationChange() {
        return open ? 1 : -1;
    }

    private static final Identifier BLOCK_ID = Identifier.of("minecraft", "piston");

    @Override
    public Identifier getBlockId() {
        return BLOCK_ID;
    }

    @Override
    public void buildOn(CodeLineElement lineElement) {
        lineElement.addChild(TextElement.create(open ? "{" : "}"));
    }
}
