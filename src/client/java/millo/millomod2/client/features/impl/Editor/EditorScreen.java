package millo.millomod2.client.features.impl.Editor;

import millo.millomod2.client.features.impl.Editor.widgets.hierarchy.HierarchyMethodWidget;
import millo.millomod2.client.features.impl.Editor.widgets.hierarchy.HierarchyWidget;
import millo.millomod2.client.features.impl.Editor.widgets.texteditor.LineContainer;
import millo.millomod2.client.hypercube.data.LineStarterType;
import millo.millomod2.client.util.style.GUIStyle;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.sapfii.sapscreens.screens.widgets.WidgetContainerScreen;

public class EditorScreen extends WidgetContainerScreen {

    private static final float HIERARCHY_WIDTH = 0.2f;

    HierarchyWidget hierarchy;
    LineContainer lineContainer;

    public EditorScreen() {
        super(null);

        int hierarchyWidth = (int) (this.width * HIERARCHY_WIDTH);

        hierarchy = new HierarchyWidget();
        hierarchy.withDimensions(hierarchyWidth, height);
        hierarchy.addEntry(new HierarchyMethodWidget(Text.literal("test method"), LineStarterType.FUNCTION));
        addWidget(hierarchy);

        lineContainer = new LineContainer(this.width - hierarchyWidth, height);
        addWidget(lineContainer);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.fill(0, 0, this.width, this.height, GUIStyle.BACKGROUND);

        int hierarchyWidth = (int) (this.width * HIERARCHY_WIDTH);
        context.fill(hierarchyWidth, 0, hierarchyWidth + 1, this.height, GUIStyle.TEXT);

        lineContainer.updateWidth(this.width - hierarchyWidth, hierarchyWidth);
        super.render(context, mouseX, mouseY, deltaTicks);
    }
}
