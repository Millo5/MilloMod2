package millo.millomod2.client.features.impl.Editor.logic.hierarchy;

import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.impl.Editor.Editor;
import millo.millomod2.client.features.impl.Editor.elements.CodeBrowser;
import millo.millomod2.client.features.impl.Editor.elements.HierarchyMethodElement;
import millo.millomod2.client.hypercube.template.MethodType;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.ClickableElement;
import net.minecraft.client.font.Alignment;
import net.minecraft.text.Text;

public class HierarchyMethod implements HierarchyEntry {

    private final String templateName;
    private final String displayName;

    public HierarchyMethod(String templateName) {
        this.templateName = templateName;
        this.displayName = MethodType.trimSuffix(templateName);
    }


    @Override
    public ClickableElement<?> getElement(CodeBrowser browser) {

        String folderRegex = FeatureHandler.get(Editor.class).getFolderRegex();
        String[] parts = displayName.split(folderRegex);
        String displayName = " " + parts[parts.length - 1];

        int borderColor = switch (MethodType.fromSuffix(templateName)) {
            case EVENT -> Styles.PLAYER_EVENT.getColor();
            case FUNC -> Styles.FUNCTION.getColor();
            case PROCESS -> Styles.PROCESS.getColor();
            case ENTITY_EVENT -> Styles.ENTITY_EVENT.getColor();
            case GAME_EVENT -> Styles.GAME_EVENT.getColor();
            case null -> Styles.SCARY.getColor();
        };

        return new HierarchyMethodElement(browser, templateName, 100, 10)
                .position(20, 0)
                .message(Text.literal(displayName))
                .border(new ClickableElement.Border().left(0xff000000 | borderColor))
                .textAlignment(Alignment.LEFT);
    }

    @Override
    public String getName() {
        return displayName;
    }

    @Override
    public boolean contains(String methodName) {
        return templateName.equals(methodName);
    }
}
