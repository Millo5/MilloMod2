package millo.millomod2.client.features.impl.Editor.template.lines;

import millo.millomod2.client.features.impl.Editor.template.CodeLine;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.flex.FlexElement;

public class BracketLine implements CodeLine {

    private final boolean open;
    private final String type;

    public BracketLine(boolean open, String type) {
        this.open = open;
        this.type = type;
    }

    public int getIndentationChange() {
        return open ? 1 : -1;
    }

    @Override
    public void buildOn(FlexElement<?> lineElement) {
        lineElement.addChild(TextElement.create(open ? "{" : "}"));
    }
}
