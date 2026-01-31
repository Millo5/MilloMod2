package millo.millomod2.client.features.impl.Editor.logic.hierarchy;

import millo.millomod2.client.features.impl.Editor.elements.CodeBrowser;
import millo.millomod2.menu.elements.ClickableElement;

public interface HierarchyEntry {

    ClickableElement<?> getElement(CodeBrowser browser);

    String getName();

    boolean contains(String methodName);
}
