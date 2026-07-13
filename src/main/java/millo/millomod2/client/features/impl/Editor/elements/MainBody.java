package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.features.impl.Editor.EditorMenu;
import millo.millomod2.client.features.impl.Editor.logic.MethodIndex;
import millo.millomod2.client.hypercube.template.MethodType;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.ClickableElement;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.client.font.Alignment;
import net.minecraft.text.Text;

import java.util.stream.Stream;

public class MainBody extends FlexElement<MainBody> {

    private final Hierarchy hierarchy;
    private final CodeBrowser codeBrowser;
    private final SearchBar searchBar;

    private EditorMenu menu;

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
        searchBar.addSubscriber(codeBrowser);
        searchBar.setActive(false);
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
        codeBrowser.openTemplate(name);
    }

    public void openTemplateContext(MethodType methodType, String regex) {
        Stream<String> filtered = menu.getLoadedPlot().getTemplateNames().stream()
                .filter(methodType::matches)
                .filter(name -> name.matches(regex));

        ListElement list = ListElement.create(200, 10)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .background(0x80000000)
                .gap(0)
                .maxExpansion(150)
                .padding(4);

        String suffix = methodType.suffixString("");

        filtered.forEach(name -> list.addChild(ButtonElement.create(200, 10)
                .message(Text.literal(name.replaceFirst(suffix + "$", "")))
                .textAlignment(Alignment.LEFT)
                .onPress(button -> {
                    codeBrowser.openTemplate(name);
                    menu.closeContextMenu();
                })));

        if (list.getChildren().isEmpty()) {
            list.addChild(TextElement.create(Text.literal("No compatible templates found!").setStyle(Styles.SCARY.getStyle())));
        }

        menu.openContextMenuAtCursor(list, 0, 8);
    }

    public void openUsages(String templateName) {
        ListElement usageMenu = ListElement.create(240, 10)
                .background(0xEE222222)
                .border(new ClickableElement.Border().full(0x6633AAAA))
                .direction(ElementDirection.COLUMN)
                .crossAlign(CrossAxisAlignment.STRETCH)
                .maxExpansion(170)
                .padding(5)
                .gap(2);

        MethodIndex methodIndex = menu.getLoadedPlot().getMethodIndex();
        var usages = methodIndex.getUsages(templateName);

        TextElement title = TextElement.create(Text.literal("Usages of ").setStyle(Styles.HEADER.getStyle())
                .append(Text.literal(MethodType.trimSuffix(templateName)).setStyle(Styles.NAME.getStyle())));
        title.setHeight(12);
        usageMenu.addChild(title);
        ButtonElement separator = ButtonElement.create(240, 1).background(0xAA666666).muted();
        separator.active = false;
        usageMenu.addChild(separator);

        if (usages.isEmpty()) {
            String message = methodIndex.isIndexing() ? "Indexing usages..." : "No usages found";
            ButtonElement empty = ButtonElement.create(240, 18)
                    .message(Text.literal(message).setStyle(Styles.COMMENT.getStyle()))
                    .textAlignment(Alignment.LEFT)
                    .removeHoverBackground();
            empty.active = false;
            usageMenu.addChild(empty);
        } else {
            for (MethodIndex.MethodUsage usage : usages) {
                Text message = Text.literal(MethodType.trimSuffix(usage.sourceTemplateName())).setStyle(Styles.NAME.getStyle())
                        .append(Text.literal("  line ").setStyle(Styles.COMMENT.getStyle()))
                        .append(Text.literal(String.valueOf(usage.line())).setStyle(Styles.LINE.getStyle()));
                usageMenu.addChild(ButtonElement.create(240, 18)
                        .message(message)
                        .background(0x18000000)
                        .hoverBackground(0x4033AAAA)
                        .textAlignment(Alignment.LEFT)
                        .onPress(button -> {
                            codeBrowser.openTemplateAtLine(usage.sourceTemplateName(), usage.line());
                            menu.closeContextMenu();
                        }));
            }

            if (methodIndex.isIndexing()) {
                TextElement indexing = TextElement.create(Text.literal("Still indexing...").setStyle(Styles.COMMENT.getStyle()));
                indexing.setHeight(10);
                usageMenu.addChild(indexing);
            }
        }
        menu.openContextMenuAtCursor(usageMenu);
    }

    public void focusHierarchySearch() {
        hierarchy.focusSearch();
    }

    public void focusCodeBrowserSearch() {
        searchBar.setActive(true);
        searchBar.focus();
    }

    public SearchBar getSearchBar() {
        return searchBar;
    }

    public void updateMenu(EditorMenu menu) {
        this.menu = menu;
    }

}
