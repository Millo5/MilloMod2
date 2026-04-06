package millo.millomod2.menu.elements;

import millo.millomod2.menu.ContainerElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class ListElement extends ContainerElement<ListElement> {

    private ElementDirection direction = ElementDirection.COLUMN;
    private CrossAxisAlignment crossAlign = CrossAxisAlignment.START;

    private int padding = 0;
    private int gap = 0;

    private int minExpansion = 0;
    private int maxExpansion = Integer.MAX_VALUE;

    private int contentExpansion = 0;
    private double renderedScrollOffset = 0d;
    private double scrollOffset = 0d;
    private double scrollSpeed = 40.0d;

    protected ListElement(int x, int y, int width, int height) {
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

    public ListElement scrollSpeed(double speed) {
        this.scrollSpeed = speed;
        return this;
    }

    public ListElement maxExpansion(int maxExpansion) {
        this.maxExpansion = maxExpansion;
        return this;
    }

    //

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
//        if (direction == ElementDirection.ROW) {
//            minExpansion = width;
//        }
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        // I hope commenting these out doesn't break anything :)
//        if (direction == ElementDirection.COLUMN) {
//            minExpansion = height;
//        }
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

        int cursor = padding - (int) renderedScrollOffset;

        for (ClickableWidget child : children) {
            if (!child.visible) continue;
            if (children instanceof ContainerElement<?> ce) {
                ce.layoutChildren();
            }
            if (vertical) {
                child.setPosition(padding + crossOffset(child.getWidth()), cursor);
                cursor += child.getHeight() + gap;
            } else {
                child.setPosition(cursor, padding + crossOffset(child.getHeight()));
                cursor += child.getWidth() + gap;
            }
        }

        cursor -= gap;
        contentExpansion = cursor + (int) renderedScrollOffset;
        setExpansion(contentExpansion);
        clampScroll();
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
        expansion = Math.min(expansion, maxExpansion);

        if (direction == ElementDirection.ROW) setWidth(expansion + padding * 2);
        else setHeight(expansion + padding * 2);
    }

    private void clampScroll() {
        int viewportSize = (direction == ElementDirection.ROW ? getWidth() : getHeight()) - padding * 2;
        int maxScroll = Math.max(0, contentExpansion - viewportSize);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
    }

    @Override
    protected void renderElement(RenderArgs args) {
        renderedScrollOffset = MathHelper.lerp(args.deltaTicks(), renderedScrollOffset, scrollOffset);
        layoutChildren();
        renderChildren(args);
    }

    @Override
    protected void childrenUpdated() {
        layoutChildren();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (isMouseOver(mouseX, mouseY)) {
            double currentOffset = scrollOffset;
            switch (direction) {
                case COLUMN -> scrollOffset -= verticalAmount * scrollSpeed;
                case ROW -> scrollOffset -= horizontalAmount * scrollSpeed;
            }
            clampScroll();

            if (currentOffset != scrollOffset) return true;
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);

    }
}
