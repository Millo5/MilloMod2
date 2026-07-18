package millo.millomod2.client.features.guides;

import millo.millomod2.menu.elements.ListElement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.function.Consumer;

public class FeatureGuide {

    public static final ArrayList<FeatureGuide> guides = new ArrayList<>();
    
    public static void registerGuide(FeatureGuide guide) {
        guides.add(guide);
    }

    public static void clearGuides() {
        guides.clear();
    }

    public static ArrayList<FeatureGuide> getGuides() {
        ArrayList<FeatureGuide> sortedGuides = new ArrayList<>(guides);
        sortedGuides.sort(Comparator
                .comparing(FeatureGuide::getCategory)
                .thenComparing(FeatureGuide::getName));
        return sortedGuides;
    }

    private String name = "Unnamed Guide";
    private ArrayList<GuideSection> sections = new ArrayList<>();
    private String category = "Other";

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void populateGuide(ListElement guideContent) {
        for (GuideSection section : sections) {
            section.populate(guideContent);
        }
    }

    public boolean matchesSearch(String query) {
        if (query.isBlank()) return true;

        StringBuilder searchableText = new StringBuilder(name).append(' ').append(category);
        for (GuideSection section : sections) {
            searchableText.append(' ').append(section.getSearchText());
        }

        String normalizedText = searchableText.toString().toLowerCase(Locale.ROOT);
        for (String term : query.toLowerCase(Locale.ROOT).trim().split("\\s+")) {
            if (!normalizedText.contains(term)) return false;
        }
        return true;
    }

    public FeatureGuide setName(String name) {
        this.name = name;
        return this;
    }

    public FeatureGuide setCategory(String category) {
        this.category = category;
        return this;
    }

    public FeatureGuide addSection(Consumer<GuideSection> consumer) {
        GuideSection section = new GuideSection();
        consumer.accept(section);
        sections.add(section);
        return this;
    }
}
