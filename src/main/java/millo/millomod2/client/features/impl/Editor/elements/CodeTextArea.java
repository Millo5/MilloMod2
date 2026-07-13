package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.logic.CodeBodyBuilder;
import millo.millomod2.client.features.impl.Editor.logic.search.SearchResult;
import millo.millomod2.client.features.impl.Editor.logic.search.Searchable;
import millo.millomod2.client.hypercube.model.TemplateModel;
import millo.millomod2.menu.elements.ListElement;
import net.minecraft.client.gui.widget.ClickableWidget;

import java.util.ArrayList;
import java.util.Collection;

public class CodeTextArea extends ListElement implements Searchable {

    private CodeBrowser browser;

    public CodeTextArea(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public CodeTextArea(CodeBrowser browser) {
        this(0, 20, browser.getWidth(), browser.getHeight() - 20);
        padding(10);
        maxExpansion(browser.getHeight() - 20);
    }


    public void clearContents() {
        this.clearChildren();
    }

    public void loadTemplate(TemplateModel template) {
        MilloMod.MC.send(() -> {
            clearContents();
            loadCodeBody(template);
        });
    }


    // Code loading

    private void loadCodeBody(TemplateModel template) {
        CodeBodyBuilder builder = new CodeBodyBuilder(this, template);
        builder.build();
    }

    public void focusLine(int line) {
        MilloMod.MC.send(() -> {
            int index = line - 1;
            if (index < 0 || index >= getChildren().size()) return;
            if (!(getChildren().get(index) instanceof CodeLineElement codeLine)) return;

            scrollToChild(codeLine);
            codeLine.highlight();
        });
    }


    @Override
    public Collection<? extends SearchResult> search(String searchQuery) {
        ArrayList<SearchResult> results = new ArrayList<>();
        for (var child : getChildren()) {
            if (child instanceof Searchable searchable) {
                results.addAll(searchable.search(searchQuery));
            }
        }
        return results;
    }

    public String getRawContents() {
        StringBuilder builder = new StringBuilder();
        for (var child : getChildren()) {
            if (child instanceof CodeLineElement line) {
                int segment = 0;
                for (ClickableWidget lineChild : line.getChildren()) {
                    if (segment++ < 2) continue; // Skip block and line number
                    String lineContent = lineChild.getMessage().getString();
                    builder.append(lineContent);
                }
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}
