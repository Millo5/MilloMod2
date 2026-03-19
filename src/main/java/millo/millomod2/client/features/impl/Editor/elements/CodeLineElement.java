package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.features.impl.Editor.logic.search.SearchResult;
import millo.millomod2.client.features.impl.Editor.logic.search.Searchable;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.flex.FlexElement;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;

public class CodeLineElement extends FlexElement<CodeLineElement> implements Searchable {

    public CodeLineElement(int x, int y, int width, int height) {
        super(x, y, width, height, Text.empty());
    }


    @Override
    public Collection<? extends SearchResult> search(String searchQuery) {
        ArrayList<SearchResult> results = new ArrayList<>();

        for (ClickableWidget child : getChildren()) {
            if (child instanceof TextElement textElement) {
//                textElement.getMessage().getString()
            }
        }
        return results;
    }
}
