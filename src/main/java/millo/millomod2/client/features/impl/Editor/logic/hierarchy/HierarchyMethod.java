package millo.millomod2.client.features.impl.Editor.logic.hierarchy;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.impl.Editor.Editor;
import millo.millomod2.client.features.impl.Editor.elements.CodeBrowser;
import millo.millomod2.client.hypercube.template.Template;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.ClickableElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import net.minecraft.client.font.Alignment;
import net.minecraft.text.Text;

public class HierarchyMethod implements HierarchyEntry {

    private final Template template;

    public HierarchyMethod(Template template) {
        this.template = template;
    }

    @Override
    public ClickableElement<?> getElement(CodeBrowser browser) {

        String folderRegex = FeatureHandler.get(Editor.class).getFolderRegex();
        String[] parts = template.getName().split(folderRegex);
        String displayName = " " + parts[parts.length - 1];

        int borderColor = switch (template.getTemplateMethodType()) {
            case EVENT -> Styles.PLAYER_EVENT.getColor();
            case FUNC -> Styles.FUNCTION.getColor();
            case PROCESS -> Styles.PROCESS.getColor();
            case ENTITY_EVENT -> Styles.ENTITY_EVENT.getColor();
            case GAME_EVENT -> Styles.GAME_EVENT.getColor();
        };

        return ButtonElement.create(100, 10)
                .position(20, 0)
                .message(Text.literal(displayName))
                .border(new ClickableElement.Border().left(0xff000000 | borderColor))
                .textAlignment(Alignment.LEFT)
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
