package millo.millomod2.client.features.impl.Editor.widgets.hierarchy;

import millo.millomod2.client.hypercube.data.LineStarterType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.sapfii.sapscreens.screens.widgets.ButtonWidget;

public class HierarchyMethodWidget extends ButtonWidget implements HierarchyEntry {

    private final LineStarterType type;

    public HierarchyMethodWidget(Text text, LineStarterType type) {
        super(text);
        this.type = type;
    }

    @Override
    public HierarchyMethodWidget getThis() {
        return this;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.fill(0, 0, 2, this.height, type.getColor());
    }
}
