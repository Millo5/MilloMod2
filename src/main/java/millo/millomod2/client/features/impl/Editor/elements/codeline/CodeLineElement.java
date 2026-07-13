package millo.millomod2.client.features.impl.Editor.elements.codeline;

import millo.millomod2.client.features.impl.Editor.logic.search.SearchResult;
import millo.millomod2.client.features.impl.Editor.logic.search.Searchable;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.flex.FlexElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;

public class CodeLineElement extends FlexElement<CodeLineElement> implements Searchable {

    private float highlight = 0f;

    public CodeLineElement(int x, int y, int width, int height) {
        super(x, y, width, height, Text.empty());
    }


    @Override
    public Collection<? extends SearchResult> search(String searchQuery) {
        ArrayList<SearchResult> results = new ArrayList<>();

        for (ClickableWidget child : getChildren()) {
            if (child instanceof TextElement textElement) {
                int index = textElement.getMessage().getString().indexOf(searchQuery);
                if (index != -1) {
                    results.add(new SearchResult(textElement, index, index + searchQuery.length()));
                }
            }
        }
        return results;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        if (highlight > 0f) {
            int alpha = (int) (highlight * 0x60) << 24;
            context.fill(getX(), getY(), getRight(), getBottom(), alpha | (Styles.HIGHLIGHT.getColor() & 0x00FFFFFF));
            highlight = Math.max(0f, highlight - deltaTicks * 0.1f);
        }
        super.renderWidget(context, mouseX, mouseY, deltaTicks);
    }

    public void highlight() {
        highlight = 1f;
    }

}
