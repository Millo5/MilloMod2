package millo.millomod2.menu.elements;

import millo.millomod2.menu.ContainerElement;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.joml.Matrix3x2fStack;

public class FolderElement extends ContainerElement<FolderElement> {

    private boolean opened = false;
    private final Component title;
    private final ListElement contentList;

    private int maxHeightWhenOpened = 24;

    public FolderElement(int x, int y, int width, int height, Component title) {
        super(x, y, width, height, Component.empty());
        this.title = title;
        contentList = ListElement.create(width, 12)
                .gap(4)
                .padding(4)
                .position(0, 12)
                .maxExpansion(height - 12);
        super.addChild(contentList);
        maxHeightWhenOpened = height;
    }

    public static FolderElement create(int width, int height, Component title) {
        return new FolderElement(0, 0, width, height, title)
                .background(0x40000000);
    }

    public FolderElement position(int x, int y) {
        setPosition(x, y);
        return this;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public ListElement getContent() {
        return contentList;
    }

    //

    private float visualHeight = 0;

    @Override
    protected void renderElement(RenderArgs args) {
        args.context().fill(0, 0, getWidth(), getHeight(), background);

        args.context().drawString(getTextRenderer(), title, 16, 1, 0xFFFFFFFF, true);

        float targetHeight = opened ? Math.min(maxHeightWhenOpened, contentList.getHeight() + 12) : 12;

        Matrix3x2fStack mats = args.context().pose();
        mats.pushMatrix();
        mats.rotateAbout((contentList.getHeight() / visualHeight) * 1.570796f, 9f, 5);
        args.context().drawString(getTextRenderer(), "☽", 5, 1, 0xFFFFFFFF, false);
        mats.popMatrix();

//        visualHeight += (targetHeight - visualHeight) * 0.2f;
        visualHeight = Mth.clampedLerp(args.deltaTicks(), visualHeight, targetHeight);
        setHeight(Math.round(visualHeight));

        renderChildren(args);
    }


    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        if (!active || !visible) return false;
        if (!isMouseOver(click.x(), click.y())) return false;

        if (click.y() <= getY() + 12) {
            setOpened(!isOpened());
            return true;
        }
        return super.mouseClicked(click, doubled);
    }


    @Override
    public void addChild(AbstractWidget child) {
        contentList.addChild(child);
    }

    
    public Component getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "FolderElement{" + title + ", [" + toStringChildren() + "]}";
    }
}
