package millo.millomod2.client.features.impl.Editor.logic.hierarchy;

import millo.millomod2.client.features.impl.Editor.elements.CodeBrowser;
import millo.millomod2.menu.elements.ClickableElement;
import millo.millomod2.menu.elements.FolderElement;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class HierarchyFolder implements HierarchyEntry {

    private final ArrayList<HierarchyEntry> entries = new ArrayList<>();
    private final String name;

    public HierarchyFolder(String name) {
        this.name = name;
    }

    public void addEntry(HierarchyEntry entry) {
        entries.add(entry);

        entries.sort((a, b) -> {
            if (a instanceof HierarchyFolder && !(b instanceof HierarchyFolder)) return -1;
            if (!(a instanceof HierarchyFolder) && b instanceof HierarchyFolder) return 1;
            return a.getName().compareToIgnoreCase(b.getName());
        });
    }

    @Override
    public ClickableElement<?> getElement(CodeBrowser browser) {
        FolderElement folderElement = FolderElement.create(500, 5000, Text.literal(name))
                .border(new ClickableElement.Border().left(0x50000000))
                .background(0);

        for (HierarchyEntry entry : entries) {
            folderElement.addChild(entry.getElement(browser));
        }

        folderElement.getContent()
                .padding(0)
                .gap(0)
                .position(8, 12);

        return folderElement;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean contains(String methodName) {
        return entries.stream().anyMatch(entry -> entry.contains(methodName));
    }

    public ArrayList<HierarchyEntry> getHierarchyEntries() {
        return entries;
    }

    public void clear() {
        entries.clear();
    }
}
