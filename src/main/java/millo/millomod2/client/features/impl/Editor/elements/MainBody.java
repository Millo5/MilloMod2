package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.features.impl.Editor.EditorMenu;
import millo.millomod2.client.features.impl.Editor.logic.model.TemplateModel;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.text.Text;

import java.util.Optional;

public class MainBody extends FlexElement<MainBody> {

    private final Hierarchy hierarchy;
    private final CodeBrowser codeBrowser;
    private final SearchBar searchBar;

    private final EditorMenu menu;

    public MainBody(EditorMenu menu) {
        super(0, 0, menu.width, menu.height - 20, Text.empty());
        this.menu = menu;

        direction(ElementDirection.ROW);
        mainAlign(MainAxisAlignment.SPACE_BETWEEN);
        crossAlign(CrossAxisAlignment.STRETCH);

        background(0x11000000);

        hierarchy = new Hierarchy(menu, menu.width / 5, height)
                .maxWidth(menu.width / 2)
                .minWidth(20);
        codeBrowser = new CodeBrowser(hierarchy, width - hierarchy.getWidth(), height);
        addChildren(
                hierarchy,
                codeBrowser
        );

        searchBar = new SearchBar(250, 20, codeBrowser);
        searchBar.setAbsoluteX(width - searchBar.getWidth() - 5);
        searchBar.setAbsoluteY(25);
        addChild(searchBar);
    }

    private int getAvailableWidth() {
        return width - hierarchy.getWidth();
    }

    public CodeBrowser getCodeBrowser() {
        return codeBrowser;
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

    public Hierarchy getHierarchy() {
        return hierarchy;
    }

    public void tryOpenTemplate(String name) {
        menu.getMain().getCodeBrowser().openTemplate(name);

        if (true) return; // TODO

        Optional<TemplateModel> templateOpt = menu.getLoadedPlot().getCachedTemplate(name);
        templateOpt.ifPresent(codeBrowser::openTemplate);
    }

    public void openAndFocusSearch() {
//        hierarchy.openAndFocusSearch();
    }

    public SearchBar getSearchBar() {
        return searchBar;
    }
}
