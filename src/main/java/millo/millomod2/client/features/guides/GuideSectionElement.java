package millo.millomod2.client.features.guides;

import millo.millomod2.menu.ContainerElement;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

/** A bordered guide card that lays out a section's related rows as one visual group. */
public class GuideSectionElement extends ContainerElement<GuideSectionElement> {

    private static final int PADDING = 6;
    private static final int GAP = 4;

    public GuideSectionElement(int width) {
        super(0, 0, width, PADDING * 2, Text.empty());
        background(0x40000000);
        border(new Border().full(0x70404040));
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        layoutChildren();
    }

    @Override
    public void layoutChildren() {
        int y = PADDING;
        int contentWidth = Math.max(1, getWidth() - PADDING * 2);

        for (ClickableWidget child : getChildren()) {
            child.setWidth(contentWidth);
            child.setPosition(PADDING, y);
            y += child.getHeight() + GAP;
        }

        if (!getChildren().isEmpty()) y -= GAP;
        setHeight(y + PADDING);
    }

    @Override
    protected void childrenUpdated() {
        layoutChildren();
    }
}
