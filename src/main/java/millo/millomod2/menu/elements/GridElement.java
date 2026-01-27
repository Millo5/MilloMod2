package millo.millomod2.menu.elements;

import millo.millomod2.menu.ContainerElement;
import millo.millomod2.menu.elements.flex.ElementDirection;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.List;

public class GridElement extends ContainerElement<GridElement> {

    private ElementDirection direction = ElementDirection.ROW;
    private int padding, gap;

    public GridElement(int width, int height) {
        super(0, 0, width, height, Text.empty());
    }

    public GridElement padding(int padding) {
        this.padding = padding;
        return this;
    }

    public GridElement gap(int gap) {
        this.gap = gap;
        return this;
    }

    public GridElement direction(ElementDirection direction) {
        this.direction = direction;
        return this;
    }


    @Override
    public void layoutChildren() {
        List<ClickableWidget> children = getChildren();

        if (children.isEmpty()) return;

        boolean vertical = direction == ElementDirection.COLUMN;

        int cursor = padding;
        int cursorCross = padding;
        int crossLayerSize = 0;

        for (ClickableWidget child : children) {
            if (children instanceof ContainerElement<?> ce) {
                ce.layoutChildren();
            }
            if (vertical){
                if (cursor + child.getHeight() > getHeight() - padding) {
                    cursor = padding;
                    cursorCross += crossLayerSize + gap;
                    crossLayerSize = 0;
                }

                child.setPosition(cursorCross, cursor);
                cursor += child.getHeight() + gap;
                crossLayerSize = Math.max(crossLayerSize, child.getWidth());
            } else {
                if (cursor + child.getWidth() > getWidth() - padding) {
                    cursor = padding;
                    cursorCross += crossLayerSize + gap;
                    crossLayerSize = 0;
                }

                child.setPosition(cursor, cursorCross);
                cursor += child.getWidth() + gap;
                crossLayerSize = Math.max(crossLayerSize, child.getHeight());
            }
        }
    }

    @Override
    protected void childrenUpdated() {
        layoutChildren();
    }
}
