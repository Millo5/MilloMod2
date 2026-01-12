package millo.millomod2.menu.elements.flex;

import millo.millomod2.menu.ContainerElement;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlexElement<T extends FlexElement<T>> extends ContainerElement<T> {

    private ElementDirection direction = ElementDirection.ROW;
    private MainAxisAlignment mainAlign = MainAxisAlignment.START;
    private CrossAxisAlignment crossAlign = CrossAxisAlignment.START;

    private int padding = 0;
    private int gap = 0;

    private final Map<ClickableWidget, Integer> growMap = new HashMap<>();

    protected FlexElement(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    public static FlexElement<?> create(int width, int height) {
        return new FlexElement<>(0, 0, width, height, Text.empty());
    }

    protected T self() {
        return (T) this;
    }

    // Builder methods

    public T direction(ElementDirection direction) {
        this.direction = direction;
        return self();
    }

    public T mainAlign(MainAxisAlignment alignment) {
        this.mainAlign = alignment;
        return self();
    }

    public T crossAlign(CrossAxisAlignment alignment) {
        this.crossAlign = alignment;
        return self();
    }

    public T padding(int padding) {
        this.padding = padding;
        return self();
    }

    public T gap(int gap) {
        this.gap = gap;
        return self();
    }

    public T grow(ClickableWidget child, int factor) {
        growMap.put(child, factor);
        return self();
    }

    //

    @Override
    public void layoutChildren() {
        List<ClickableWidget> children = getChildren();
        if (children.isEmpty()) return;

        int contentX = padding;
        int contentY = padding;
        int contentWidth = getWidth() - 2 * padding;
        int contentHeight = getHeight() - 2 * padding;

        boolean row = direction == ElementDirection.ROW;

        int totalMainSize = 0;
        int totalGrow = 0;
        for (ClickableWidget child : children) {
            totalMainSize += row ? child.getWidth() : child.getHeight();
            totalGrow += growMap.getOrDefault(child, 0);
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
                if (children.size() > 1) spacing = gap + freeSpace / (children.size() - 1);
            }
            default -> cursor = 0;
        }

        for (ClickableWidget child : children) {
            int grow = growMap.getOrDefault(child, 0);

            if (grow > 0 && freeSpace > 0 && totalGrow > 0) {
                int extra = freeSpace * grow / totalGrow;
                if (row) child.setWidth(child.getWidth() + extra);
                else child.setHeight(child.getHeight() + extra);
            }

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
