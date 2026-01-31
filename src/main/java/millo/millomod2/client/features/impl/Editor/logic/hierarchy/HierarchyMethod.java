package millo.millomod2.client.features.impl.Editor.logic.hierarchy;

import millo.millomod2.client.features.impl.Editor.elements.CodeBrowser;
import millo.millomod2.client.hypercube.template.Template;
import millo.millomod2.menu.elements.ClickableElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import net.minecraft.text.Text;

public class HierarchyMethod implements HierarchyEntry {

    private final Template template;

    public HierarchyMethod(Template template) {
        this.template = template;
    }

    @Override
    public ClickableElement<?> getElement(CodeBrowser browser) {
        return ButtonElement.create(100, 10)
                .message(Text.literal(template.getName()))
                .onPress((button) -> {
                    browser.openTemplate(template);
                });
    }

    @Override
    public String getName() {
        return template.getName();
    }

    @Override
    public boolean contains(String methodName) {
        return template.getMethodName().equals(methodName);
    }
}
