package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.hypercube.template.Template;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.text.Text;

import java.util.HashMap;

public class CodeBrowser extends FlexElement<CodeBrowser> {

    private Tab currentTab = null;
    private HashMap<String, Tab> openTabs = new HashMap<>();

    private ListElement tabList;
    private CodeTextArea codeTextArea;

    protected CodeBrowser(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    public CodeBrowser(int width, int height) {
        super(0, 0, width, height, Text.empty());

        direction(ElementDirection.COLUMN);
        mainAlign(MainAxisAlignment.START);
        crossAlign(CrossAxisAlignment.STRETCH);
//        background(0x22000000);

        tabList = ListElement.create(width, 20)
                .gap(1)
                .padding(0)
                .border(new Border().top(0xFFFFFFFF).bottom(0xFFFFFFFF))
                .direction(ElementDirection.ROW)
                .crossAlign(CrossAxisAlignment.CENTER);

        codeTextArea = new CodeTextArea(this);

        addChildren(
                tabList,
                codeTextArea
        );
    }

    public void openTemplate(Template template) {
        if (!openTabs.containsKey(template.getName())) {
            Tab tab = new Tab(this, template.getName(), template);
            openTabs.put(template.getName(), tab);
            tabList.addChild(tab.button);
        }

        openTab(template.getName());
    }

    public void openTab(String name) {
        if (openTabs.containsKey(name)) {
            Tab tab = openTabs.get(name);
            currentTab = tab;
            codeTextArea.loadTemplate(tab.template);
        }
    }

    private static class Tab {
        String name;
        Template template;
        ButtonElement button;

        public Tab(CodeBrowser browser, String name, Template template) {
            this.name = name;
            this.template = template;
            button = ButtonElement.create(100, 20)
                    .message(Text.literal(name))
                    .onPress((button) -> {
                        browser.openTemplate(template);
                    });
        }
    }
}
