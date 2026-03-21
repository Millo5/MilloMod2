package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.impl.Editor.Editor;
import millo.millomod2.client.features.impl.Editor.EditorMenu;
import millo.millomod2.client.hypercube.model.TemplateModel;
import millo.millomod2.menu.elements.FolderElement;
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

        list = ListElement.create(20, height - 10)
                .direction(ElementDirection.COLUMN)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .maxExpansion(height - 12);

        addChild(list);
    }

    public void reload() {
        list.clearChildren();
        if (menu == null) return;
        if (menu.getLoadedPlot() == null) return;

        list.addChild(menu.getLoadedPlot().getRootFolder().getElement(menu.getMain().getCodeBrowser()));
    }

    public void focus(String methodName) {
        String folderRegex = FeatureHandler.get(Editor.class).getFolderRegex();
        String[] pathParts = methodName.split(folderRegex);

        if (pathParts.length == 0) return;

        FolderElement root = (FolderElement) list.getChildren().getFirst();
        root.setOpened(true);

        focusFolder(root.getContent(), pathParts, 0);
    }

    private void focusFolder(ListElement listElement, String[] pathParts, int index) {
        if (index >= pathParts.length) return;

        for (var child : listElement.getChildren()) {
            if (child instanceof FolderElement folder) {
                String folderName = folder.getTitle().getString();
                if (folderName.equals(pathParts[index])) {
                    folder.setOpened(true);
                    listElement.layoutChildren();

                    if (index + 1 < pathParts.length) {
                        focusFolder(folder.getContent(), pathParts, index + 1);
                    }
                    return;
                }
            }
        }
    }

    @Override
    protected void renderElement(RenderArgs args) {
        super.renderElement(args);
    }

    public TemplateModel getTemplate(String templateName) {
        if (menu == null) return null;
        if (menu.getLoadedPlot() == null) return null;

        return menu.getLoadedPlot().getTemplate(templateName);
    }
}
