package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.impl.Editor.logic.hierarchy.HierarchyFolder;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.Menu;
import millo.millomod2.menu.elements.FolderElement;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class HierarchyFolderElement extends FolderElement {

    private final HierarchyFolder folder;
    private final CodeBrowser browser;

    public HierarchyFolderElement(CodeBrowser browser, HierarchyFolder folder) {
        super(0, 0, 500, 5000, Text.literal(folder.getName()));

        this.folder = folder;
        this.browser = browser;
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        if (click.y() <= getY() + 12 && click.button() == 1) {
            if (MilloMod.MC.currentScreen instanceof Menu menu) {
                ListElement contextMenu = ListElement.create(100, 20)
                        .background(0xCC222222)
                        .direction(ElementDirection.COLUMN)
                        .crossAlign(CrossAxisAlignment.STRETCH)
                        .gap(0);
                contextMenu.addChild(ButtonElement.create(100, 20)
                        .message(Text.literal("New Template").setStyle(Styles.CONTROL.getStyle()))
                        .onPress(button -> {
                            menu.closeContextMenu();
                        })
                );
                contextMenu.addChild(ButtonElement.create(100, 20)
                        .message(Text.literal("Delete").setStyle(Styles.SCARY.getStyle()))
                        .onPress(button -> {
                            browser.getHierarchy().removeFolder(folder);
                            menu.closeContextMenu();
                        })
                );

                menu.openContextMenu(contextMenu,
                        (int) MilloMod.MC.mouse.getScaledX(MilloMod.MC.getWindow()),
                        (int) MilloMod.MC.mouse.getScaledY(MilloMod.MC.getWindow())
                );
            }
            return true;
        }
        return super.mouseClicked(click, doubled);
    }

    public void removeTemplate(String templateName) {
        for (ClickableWidget child : getContent().getChildren()) {
            if (child instanceof HierarchyMethodElement methodElement) {
                if (methodElement.getTemplateName().equals(templateName)) {
                    getContent().removeChild(methodElement);
                    break;
                }
            } else if (child instanceof HierarchyFolderElement folderElement) {
                folderElement.removeTemplate(templateName);
                if (folderElement.getContent().getChildren().isEmpty()) {
                    getContent().removeChild(folderElement);
                }
            }
        }
    }

}
