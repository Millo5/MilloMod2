package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.impl.Editor.logic.search.SearchResult;
import millo.millomod2.client.features.impl.Editor.logic.search.Searchable;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.AbsoluteElement;
import millo.millomod2.menu.elements.TextFieldElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashSet;

public class SearchBar extends FlexElement<SearchBar> implements AbsoluteElement {

    private final CodeBrowser browser;
    private final TextFieldElement searchField;

    private int absoluteX = 0;
    private int absoluteY = 0;

    private String searchQuery = "";
    private HashSet<Searchable> subscribers = new HashSet<>();
    private ArrayList<SearchResult> currentResults = new ArrayList<>();

    private boolean active = false;

    public SearchBar(int width, int height, CodeBrowser browser) {
        super(0, 0, width, height, Text.empty());
        this.browser = browser;

        mainAlign(MainAxisAlignment.CENTER);
        crossAlign(CrossAxisAlignment.CENTER);

        searchField = new TextFieldElement(width, 16, Text.literal(""));
        searchField.setPlaceholder(Text.literal("Search...").setStyle(Styles.COMMENT.getStyle()));
        searchField.setMaxLength(1000);
        searchField.setChangedListener((value) -> {
            searchQuery = value;
            search();
        });

        addChild(searchField);
    }

    private void search() {
        for (SearchResult currentResult : currentResults) {
            currentResult.clearHighlight();
        }

        if (searchQuery.isEmpty()) {
            currentResults.clear();
            return;
        }

        currentResults.clear();
        for (Searchable searchable : subscribers) {
            currentResults.addAll(searchable.search(searchQuery));
        }
    }

    public void setActive(boolean active) {
        this.active = active;
        if (!active) {
            searchField.setText("");
            searchQuery = "";
            search();
        }

        searchField.setVisible(active);
    }

    public boolean isActive() {
        return active;
    }

    public void focus() {
        if (MilloMod.MC.currentScreen == null) return;
        MilloMod.MC.currentScreen.setFocused(searchField);
    }

    public ArrayList<SearchResult> getCurrentResults() {
        return currentResults;
    }

    public void addSubscriber(Searchable searchable) {
        subscribers.add(searchable);
    }

    public void removeSubscriber(Searchable searchable) {
        subscribers.remove(searchable);
    }

    public void setAbsoluteX(int absoluteX) {
        this.absoluteX = absoluteX;
    }

    public void setAbsoluteY(int absoluteY) {
        this.absoluteY = absoluteY;
    }

    @Override
    public int getAbsoluteX() {
        return absoluteX;
    }

    @Override
    public int getAbsoluteY() {
        return absoluteY;
    }
}
