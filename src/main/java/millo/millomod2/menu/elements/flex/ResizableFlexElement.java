package millo.millomod2.menu.elements.flex;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.text.Text;

public class ResizableFlexElement<T extends ResizableFlexElement<T>> extends FlexElement<T> {

    private final int DISTANCE = 2;
    private boolean resizing = false;
    private boolean mouseOverResizeArea = false;

    private double mouseXStart;
    private double mouseYStart;
    private double resizeStartWidth;
    private double resizeStartHeight;

    private ResizeDirection resizeDirection;
    private int minWidth = 50;
    private int minHeight = 50;
    private int maxWidth = 500;
    private int maxHeight = 500;


    protected ResizableFlexElement(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);

        minWidth = width / 2;
        minHeight = height / 2;
        maxWidth = width * 3;
        maxHeight = height * 3;
    }

    public static ResizableFlexElement<?> create(int width, int height) {
        return new ResizableFlexElement<>(0, 0, width, height, Text.empty());
    }

    @Override
    protected void renderElement(RenderArgs args) {
        if (resizeDirection != null) {
            int x = args.mouseX();
            int y = args.mouseY();

            x -= resizeDirection == ResizeDirection.WEST ? getX() : resizeDirection == ResizeDirection.EAST ? getRight() : 0;
            y -= resizeDirection == ResizeDirection.NORTH ? getY() : resizeDirection == ResizeDirection.SOUTH ? getBottom() : 0;

            mouseOverResizeArea = false;
            switch (resizeDirection) {
                case EAST, WEST -> {
                    if (Math.abs(x) <= DISTANCE || resizing) {
                        args.context().setCursor(StandardCursors.RESIZE_EW);
                        mouseOverResizeArea = true;
                    }
                }
                case NORTH, SOUTH -> {
                    if (Math.abs(y) <= DISTANCE || resizing) {
                        args.context().setCursor(StandardCursors.RESIZE_NS);
                        mouseOverResizeArea = true;
                    }
                }
            }
        }

        super.renderElement(args);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (mouseOverResizeArea && resizeDirection != null) {
            resizing = true;
            mouseXStart = click.x();
            mouseYStart = click.y();
            resizeStartWidth = getWidth();
            resizeStartHeight = getHeight();
            return true;
        }

        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(Click click) {
        if (resizing) {
            resizing = false;
            return true;
        }
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        if (resizing) {
            int newWidth = getWidth();
            int newHeight = getHeight();

            switch (resizeDirection) {
                case EAST -> newWidth = (int) (resizeStartWidth + (click.x() - mouseXStart));
                case WEST -> newWidth = (int) (resizeStartWidth - (click.x() - mouseXStart));
                case SOUTH -> newHeight = (int) (resizeStartHeight + (click.y() - mouseYStart));
                case NORTH -> newHeight = (int) (resizeStartHeight - (click.y() - mouseYStart));
            }

            newWidth = Math.max(minWidth, Math.min(maxWidth, newWidth));
            newHeight = Math.max(minHeight, Math.min(maxHeight, newHeight));

            setDimensions(newWidth, newHeight);
            layoutChildren();
            return true;
        }
        return super.mouseDragged(click, offsetX, offsetY);
    }

    //

    public T resizeDirection(ResizeDirection direction) {
        this.resizeDirection = direction;
        return self();
    }
    public T minWidth(int minWidth) {
        this.minWidth = minWidth;
        return self();
    }
    public T minHeight(int minHeight) {
        this.minHeight = minHeight;
        return self();
    }
    public T maxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return self();
    }
    public T maxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return self();
    }

    public enum ResizeDirection {
        EAST,
        WEST,
        NORTH,
        SOUTH
    }
}
