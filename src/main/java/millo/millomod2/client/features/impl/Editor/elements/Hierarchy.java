package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.impl.Editor.Editor;
import millo.millomod2.client.features.impl.Editor.EditorMenu;
import millo.millomod2.client.hypercube.model.TemplateModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.FolderElement;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextFieldElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import millo.millomod2.menu.elements.flex.ResizableFlexElement;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class Hierarchy extends ResizableFlexElement<Hierarchy> {

    private final EditorMenu menu;
    private final ListElement list;
    private String searchQuery = "";
    private int searchTimeout = 0;

    private FolderElement root;
    private final TextFieldElement search;

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
                .gap(2)
                .maxExpansion(height - 12);

        search = new TextFieldElement(200, 12, Text.empty());
        search.setPlaceholder(Text.literal("Search...").setStyle(Styles.COMMENT.getStyle()));
        search.setChangedListener(newValue -> {
            searchQuery = newValue;
            searchTimeout = 3;
        });

        addChild(list);
    }

    public void reload() {
        list.clearChildren();
        if (menu == null) return;
        if (menu.getLoadedPlot() == null) return;

        list.addChild(search);
        createRootFolder();
    }

    private void createRootFolder() {
        if (menu == null) return;
        if (menu.getLoadedPlot() == null) return;

        root = (FolderElement) menu.getLoadedPlot().getRootFolder().getElement(menu.getMain().getCodeBrowser());
        root.setOpened(true);
        list.addChild(root);
    }

    private void search() {
        if (menu == null) return;
        if (menu.getLoadedPlot() == null) return;

        search(root);
    }

    private void search(FolderElement folder) {
        String query = searchQuery.toLowerCase();

        if (query.isEmpty()) { // Cheaper loop
            for (ClickableWidget child : folder.getContent().getChildren()) {
                if (child instanceof FolderElement subFolder) {
                    subFolder.visible = true;
                    subFolder.setOpened(false);
                    search(subFolder);
                } else if (child instanceof HierarchyMethodElement method) {
                    method.visible = true;
                }
            }
            return;
        }

        boolean anyChildVisible = false;
        for (ClickableWidget child : folder.getContent().getChildren()) {
            if (child instanceof FolderElement subFolder) {
                search(subFolder);
                if (subFolder.visible) anyChildVisible = true;
            } else if (child instanceof HierarchyMethodElement method) {
                boolean matches = method.getTemplateName().toLowerCase().contains(query);
                method.visible = matches;
                if (matches) {
                    focus(method.getTemplateName());
                    anyChildVisible = true;
                }
            }
        }
        if (folder != root) folder.visible = anyChildVisible;
    }

    public void focus(String methodName) {
        String folderRegex = FeatureHandler.get(Editor.class).getFolderRegex();
        String[] pathParts = methodName.split(folderRegex);

        if (pathParts.length == 0) return;

        root.setOpened(true);
        focusFolder(root.getContent(), methodName, pathParts, 0);
    }

    private void focusFolder(final ListElement listElement, final String methodName, final String[] pathParts, int index) {
        if (index >= pathParts.length) return;

        for (var child : listElement.getChildren()) {
            if (child instanceof FolderElement folder) {
                String folderName = folder.getTitle().getString();
                if (folderName.equals(pathParts[index])) {
                    folder.setOpened(true);
                    listElement.layoutChildren();

                    if (index + 1 < pathParts.length) {
                        focusFolder(folder.getContent(), methodName, pathParts, index + 1);
                    }
                    return;
                }
            } else if (child instanceof HierarchyMethodElement method) {
                if (method.getTemplateName().equals(methodName)) {
                    method.highlight();
                    return;
                }
            }
        }
    }

    @Override
    protected void renderElement(RenderArgs args) {
        super.renderElement(args);

        if (searchTimeout > 0) {
            searchTimeout--;
            if (searchTimeout == 0) {
                search();
            }
        }
    }

    public TemplateModel getTemplate(String templateName) {
        if (menu == null) return null;
        if (menu.getLoadedPlot() == null) return null;

        return menu.getLoadedPlot().getTemplate(templateName);
    }

    public void focusSearch() {
        if (MilloMod.MC.currentScreen == null) return;
        MilloMod.MC.currentScreen.setFocused(search);
    }
}
