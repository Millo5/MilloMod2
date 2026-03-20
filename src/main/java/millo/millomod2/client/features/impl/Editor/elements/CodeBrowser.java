package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.features.impl.Editor.logic.model.TemplateModel;
import millo.millomod2.client.hypercube.template.MethodType;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.text.Text;

import java.util.HashMap;

public class CodeBrowser extends FlexElement<CodeBrowser> {

    private Tab currentTab = null;
    private final HashMap<String, Tab> tabNameMap = new HashMap<>();
    private final Hierarchy hierarchy;

    private ListElement tabListElement;
    private CodeTextArea codeTextArea;


    public CodeBrowser(Hierarchy hierarchy, int width, int height) {
        super(0, 0, width, height, Text.empty());
        this.hierarchy = hierarchy;

        direction(ElementDirection.COLUMN);
        mainAlign(MainAxisAlignment.START);
        crossAlign(CrossAxisAlignment.STRETCH);
//        background(0x22000000);

        tabListElement = ListElement.create(width, 20)
                .gap(1)
                .padding(0)
                .border(new Border().top(0xFFFFFFFF).bottom(0xFFFFFFFF))
                .direction(ElementDirection.ROW)
                .crossAlign(CrossAxisAlignment.CENTER);

        codeTextArea = new CodeTextArea(this);

        addChildren(
                tabListElement,
                codeTextArea
        );
    }

    public void openTemplate(TemplateModel template) {
        Tab tab = tabNameMap.get(template.getFileName());
        if (tab != null) {
            tab.setTemplate(template);
        }
        openTemplate(template.getFileName());
    }

    public void openTemplate(String templateName) {
        String suffixlessName = MethodType.trimSuffix(templateName);

        if (!tabNameMap.containsKey(templateName)) {
            TemplateModel template = hierarchy.getTemplate(templateName);
            if (template == null) return;

            Tab tab = new Tab(this, suffixlessName, template);
            tabNameMap.put(templateName, tab);
            tabListElement.addChild(tab);
        }

        Tab tab = tabNameMap.get(templateName);
        openTab(templateName);
        codeTextArea.loadTemplate(tab.getTemplate());
    }

    public void openTab(String name) {
        if (!tabNameMap.containsKey(name)) return;

        if (currentTab != null) currentTab.setSelected(false);
        Tab tab = tabNameMap.get(name);
        currentTab = tab;
        tab.setSelected(true);
        codeTextArea.loadTemplate(tab.getTemplate());

        hierarchy.focus(name);
    }

    public void closeTab(Tab tab) {
        int currentIndex = Math.min(tabListElement.getChildren().indexOf(tab), tabListElement.getChildren().size() - 2);

        tabNameMap.remove(tab.getTemplate().getFileName());
        tabListElement.removeChild(tab);
        if (currentTab == tab) {
            codeTextArea.clearContents();
            currentTab = null;

            if (currentIndex >= 0) openTab(((Tab) tabListElement.getChildren().get(currentIndex)).getTemplate().getFileName());
        }
    }
}
