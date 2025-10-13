package millo.millomod2.client.features.impl.Editor.widgets.hierarchy;

import net.sapfii.sapscreens.screens.widgets.Widget;
import net.sapfii.sapscreens.screens.widgets.WidgetListBox;

public class HierarchyWidget extends WidgetListBox {

    @Override
    public Widget<WidgetListBox> addWidget(Widget<?> widget) {
        if (!(widget instanceof HierarchyEntry)) {
            throw new IllegalArgumentException("Only HierarchyEntry widgets can be added to HierarchyWidget");
        }
        return super.addWidget(widget);
    }

    public void addEntry(HierarchyEntry entry) {
        addWidget((Widget<?>) entry);
    }

}
