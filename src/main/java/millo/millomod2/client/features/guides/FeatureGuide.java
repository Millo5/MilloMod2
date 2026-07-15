package millo.millomod2.client.features.guides;

import millo.millomod2.menu.elements.ListElement;

import java.util.ArrayList;
import java.util.function.Consumer;

public class FeatureGuide {

    public static ArrayList<FeatureGuide> guides = new ArrayList<>();
    
    public static void registerGuide(FeatureGuide guide) {
        guides.add(guide);
    }

    private String name = "Unnamed Guide";
    private ArrayList<GuideSection> sections = new ArrayList<>();
    private String category = "Other";

    public String getName() {
        return name;
    }

    public void populateGuide(ListElement guideContent) {
        for (GuideSection section : sections) {
            guideContent.addChild(section.toElement());
        }
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
