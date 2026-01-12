package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.features.impl.Editor.EditorMenu;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.text.Text;

public class MainBody extends FlexElement<MainBody> {

    private Hierarchy hierarchy;
    private CodeBrowser codeBrowser;

    private EditorMenu menu;

    protected MainBody(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    public MainBody(EditorMenu menu) {
        super(0, 0, menu.width, menu.height - 20, Text.empty());
        this.menu = menu;

        direction(ElementDirection.ROW);
        mainAlign(MainAxisAlignment.SPACE_BETWEEN);
        crossAlign(CrossAxisAlignment.STRETCH);

        background(0x11000000);

        hierarchy = new Hierarchy(menu.width / 5, height)
                .maxWidth(menu.width / 2)
                .minWidth(20);
        codeBrowser = new CodeBrowser(width - hierarchy.getWidth(), height);
        addChildren(
                hierarchy,
                codeBrowser
        );
    }

    private int getAvailableWidth() {
        return width - hierarchy.getWidth();
    }


    @Override
    protected void renderElement(RenderArgs args) {
        if (codeBrowser.getWidth() != getAvailableWidth()) {
            codeBrowser.setWidth(getAvailableWidth());
            layoutChildren();
            codeBrowser.layoutChildren();
        }
        super.renderElement(args);
    }
}
