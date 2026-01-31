package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.features.impl.Editor.EditorMenu;
import millo.millomod2.client.features.impl.Editor.logic.hierarchy.HierarchyEntry;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import millo.millomod2.menu.elements.flex.ResizableFlexElement;
import net.minecraft.text.Text;

public class Hierarchy extends ResizableFlexElement<Hierarchy> {

    private final EditorMenu menu;
    private final ListElement list;

    public Hierarchy(EditorMenu menu, int width, int height) {
        super(0, 0, width, height, Text.empty());
        this.menu = menu;

        border(new Border().right(0xFFFFFFFF));
        resizeDirection(ResizeDirection.EAST);
        direction(ElementDirection.COLUMN);
        mainAlign(MainAxisAlignment.START);
        crossAlign(CrossAxisAlignment.STRETCH);
        padding(5);

        list = ListElement.create(20, height)
                .direction(ElementDirection.COLUMN)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .maxExpansion(height);

        addChild(list);
    }

    public void reload() {
        list.clearChildren();
        if (menu == null) return;
        if (menu.getLoadedPlot() == null) return;

        for (HierarchyEntry entry : menu.getLoadedPlot().getHierarchyEntries()) {
            list.addChild(entry.getElement(menu.getMain().getCodeBrowser()));
        }
    }

    @Override
    protected void renderElement(RenderArgs args) {
        super.renderElement(args);
    }
}
