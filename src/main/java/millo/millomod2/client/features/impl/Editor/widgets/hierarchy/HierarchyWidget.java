package millo.millomod2.client.features.impl.Editor.widgets.hierarchy;


import millo.millomod2.client.rendering.gui.FlexChild;

public class HierarchyWidget implements FlexChild {
    @Override
    public float getFlexGrow() {
        return 0.1f;
    }


//    @Override
//    public Widget<WidgetListBox> addWidget(Widget<?> widget) {
//        if (!(widget instanceof HierarchyEntry)) {
//            throw new IllegalArgumentException("Only HierarchyEntry widgets can be added to HierarchyWidget");
//        }
//        return super.addWidget(widget);
//    }
//
//    public void addEntry(HierarchyEntry entry) {
//        addWidget((Widget<?>) entry);
//    }

}
