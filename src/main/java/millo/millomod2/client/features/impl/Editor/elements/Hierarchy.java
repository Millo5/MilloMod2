package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.menu.elements.flex.ResizableFlexElement;
import net.minecraft.text.Text;

public class Hierarchy extends ResizableFlexElement<Hierarchy> {

    protected Hierarchy(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    public Hierarchy(int width, int height) {
        super(0, 0, width, height, Text.empty());

        border(new Border().right(0xFFFFFFFF));
        resizeDirection(ResizeDirection.EAST);
    }

    @Override
    protected void renderElement(RenderArgs args) {
        super.renderElement(args);
    }
}
