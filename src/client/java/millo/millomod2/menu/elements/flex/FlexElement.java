package millo.millomod2.menu.elements.flex;

import millo.millomod2.menu.ContainerElement;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.List;

public class FlexElement extends ContainerElement {

    private FlexDirection direction = FlexDirection.ROW;
    private MainAxisAlignment mainAlign = MainAxisAlignment.START;
    private CrossAxisAlignment crossAlign = CrossAxisAlignment.START;

    private int padding = 0;
    private int gap = 0;

    private FlexElement(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    public static FlexElement create(int width, int height) {
        return new FlexElement(0, 0, width, height, Text.empty());
    }

    // Builder methods

    public FlexElement direction(FlexDirection direction) {
        this.direction = direction;
        return this;
    }

    public FlexElement mainAlign(MainAxisAlignment alignment) {
        this.mainAlign = alignment;
        return this;
    }

    public FlexElement crossAlign(CrossAxisAlignment alignment) {
        this.crossAlign = alignment;
        return this;
    }

    public FlexElement padding(int padding) {
        this.padding = padding;
        return this;
    }

    public FlexElement gap(int gap) {
        this.gap = gap;
        return this;
    }

    //

    private void layoutChildren() {
        List<ClickableWidget> children = getChildren();
        if (children.isEmpty()) return;

        int contentX = getX() + padding;
        int contentY = getY() + padding;
        int contentWidth = getWidth() - 2 * padding;
        int contentHeight = getHeight() - 2 * padding;

        boolean row = direction == FlexDirection.ROW;

        int totalMainSize = 0;
        for (ClickableWidget child : children) {
            totalMainSize += row ? child.getWidth() : child.getHeight();
        }
        totalMainSize += gap * (children.size() - 1);

        int freeSpace = (row ? contentWidth : contentHeight) - totalMainSize;

        int cursor;
        int spacing = gap;

        switch (mainAlign) {
            case CENTER -> cursor = freeSpace / 2;
            case END -> cursor = freeSpace;
            case SPACE_BETWEEN -> {
                cursor = 0;
                if (children.size() > 1) {
                    spacing = gap + freeSpace / (children.size() - 1);
                }
            }
            default -> cursor = 0;
        }

        for (ClickableWidget child : children) {
            int childX = contentX;
            int childY = contentY;

            if (row) {
                childX += cursor;
                childY += crossPosition(child.getHeight(), contentHeight);
            } else {
                childY += cursor;
                childX += crossPosition(child.getWidth(), contentWidth);
            }

            if (crossAlign == CrossAxisAlignment.STRETCH) {
                if (row) child.setHeight(contentHeight);
                else child.setWidth(contentWidth);
            }

            child.setPosition(childX, childY);
            cursor += (row ? child.getWidth() : child.getHeight()) + spacing;
        }
    }

    private int crossPosition(int childSize, int contentSize) {
        return switch (crossAlign) {
            case CENTER -> (contentSize - childSize) / 2;
            case END -> contentSize - childSize;
            default -> 0;
        };
    }

    @Override
    protected void childrenUpdated() {
        layoutChildren();
    }
}
