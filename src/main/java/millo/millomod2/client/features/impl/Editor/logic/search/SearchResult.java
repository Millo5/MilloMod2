package millo.millomod2.client.features.impl.Editor.logic.search;

import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;

///  For global searching
public class SearchResult {

    private final TextElement element;
    private final int startIndex;
    private final int endIndex;

    public SearchResult(TextElement element, int startIndex, int endIndex) {
        this.element = element;
        this.startIndex = startIndex;
        this.endIndex = endIndex;

        element.setHighlight(Styles.HIGHLIGHT.getColor(), startIndex, endIndex);
    }

    public void clearHighlight() {
        element.setHighlight(0, 0, 0);
    }

}
