package millo.millomod2.client.features.guides;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class GuideSectionText {

    private final ArrayList<Segment> segments = new ArrayList<>();

    public List<Segment> getSegments() {
        return List.copyOf(segments);
    }

    public GuideSectionText addSnippet(String text) {
        segments.add(new Segment(text, true));
        return this;
    }

    public GuideSectionText addText(String text) {
        segments.add(new Segment(text, false));
        return this;
    }

    public GuideSectionText addText(Text text) {
        return addText(text.getString());
    }

    public record Segment(String text, boolean snippet) {}
}
