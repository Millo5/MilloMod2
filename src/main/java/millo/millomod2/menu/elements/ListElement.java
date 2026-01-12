package millo.millomod2.menu.elements;

import millo.millomod2.menu.ContainerElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.List;

public class ListElement extends ContainerElement<ListElement> {

    private ElementDirection direction = ElementDirection.COLUMN;
    private CrossAxisAlignment crossAlign = CrossAxisAlignment.START;

    private int padding = 0;
    private int gap = 0;

    private int minExpansion = 0;
    private boolean needsLayout = true;

    private ListElement(int x, int y, int width, int height) {
        super(x, y, width, height, Text.empty());
    }

    public static ListElement create(int width, int height) {
        return new ListElement(0, 0, width, height);
    }

    public ListElement position(int x, int y) {
        setPosition(x, y);
        return this;
    }

    public ListElement direction(ElementDirection direction) {
        this.direction = direction;
        minExpansion = direction == ElementDirection.ROW ? width : height;
        return this;
    }

    public ListElement crossAlign(CrossAxisAlignment alignment) {
        this.crossAlign = alignment;
        return this;
    }

    public ListElement padding(int padding) {
        this.padding = padding;
        return this;
    }

    public ListElement gap(int gap) {
        this.gap = gap;
        return this;
    }

    //

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        if (direction == ElementDirection.ROW) {
            minExpansion = width;
        }
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        if (direction == ElementDirection.COLUMN) {
            minExpansion = height;
        }
    }


    //

    @Override
    public void layoutChildren() {
        List<ClickableWidget> children = getChildren();

        if (children.isEmpty()) {
            setExpansion(0);
            return;
        }

        boolean vertical = direction == ElementDirection.COLUMN;

        int cursor = padding;
        int contentCross = 0;

        for (ClickableWidget child : children) {
            if (children instanceof ContainerElement ce) {
                ce.layoutChildren();
            }
            if (vertical) {
                child.setPosition(padding + crossOffset(child.getWidth()), cursor);
                cursor += child.getHeight() + gap;
                contentCross = Math.max(contentCross, child.getWidth());
            } else {
                child.setPosition(cursor, padding + crossOffset(child.getHeight()));
                cursor += child.getWidth() + gap;
                contentCross = Math.max(contentCross, child.getHeight());
            }
        }

        cursor -= gap;
        setExpansion(cursor);
    }

    private int crossOffset(int size) {
        int available = (direction == ElementDirection.ROW ? getHeight() : getWidth()) - 2 * padding;
        return switch (crossAlign) {
            case CENTER -> (available - size) / 2;
            case END -> available - size;
            default -> 0;
        };
    }

    private void setExpansion(int expansion) {
        expansion = Math.max(expansion, minExpansion);
        if (direction == ElementDirection.ROW) setWidth(expansion + padding * 2);
        else setHeight(expansion + padding * 2);
    }

    @Override
    protected void renderElement(RenderArgs args) {
        layoutChildren();
        renderChildren(args);
    }

    @Override
    protected void childrenUpdated() {
        layoutChildren();
    }

}
