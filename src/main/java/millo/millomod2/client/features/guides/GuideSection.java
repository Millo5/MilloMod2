package millo.millomod2.client.features.guides;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.Feature;
import millo.millomod2.client.features.FeatureHandler;
import millo.millomod2.client.features.addons.Keybound;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GuideSection {

    /**
     * Guide data is registered during client initialization, before the text renderer exists.
     * Keep element factories here and create widgets only after GuideMenu has opened.
     */
    private final ArrayList<Consumer<GuideSectionElement>> content = new ArrayList<>();
    private final ArrayList<String> searchTerms = new ArrayList<>();

    protected GuideSection() {
        super();
    }

    public void populate(millo.millomod2.menu.elements.ListElement guideContent) {
        GuideSectionElement section = new GuideSectionElement(guideContent.getWidth() - 12);
        content.forEach(factory -> factory.accept(section));
        guideContent.addChild(section);
    }

    public String getSearchText() {
        return String.join(" ", searchTerms);
    }

    public GuideSection addHeader(String s) {
        searchTerms.add(s);
        content.add(target -> target.addChild(
                TextElement.create(Text.literal(s).setStyle(Styles.HEADER.getStyle()))
        ));
        return this;
    }

    public GuideSection addText(Consumer<GuideSectionText> consumer) {
        GuideSectionText text = new GuideSectionText();
        consumer.accept(text);
        var segments = text.getSegments();
        segments.forEach(segment -> searchTerms.add(segment.text()));
        content.add(target -> target.addChild(GuideRichTextElement.create(segments)));
        return this;
    }

    public GuideSection addParagraph(String text) {
        searchTerms.add(text);
        content.add(target -> target.addChild(WrappedTextElement.create(Text.literal(text))));
        return this;
    }

    public GuideSection addCommand(String command) {
        return addSnippet("/" + command, "Click to copy snippet.");
    }

    public GuideSection addCommand(String command, String... aliases) {
        StringBuilder snippet = new StringBuilder("/").append(command);
        for (String alias : aliases) {
            snippet.append("  |  ").append("/").append(alias);
        }
        return addSnippet(snippet.toString(), "Click to copy snippet.");
    }

    public GuideSection addSnippet(String snippet) {
        return addSnippet(snippet, "Click to copy snippet.");
    }

    public GuideSection addSnippet(String snippet, String tooltip) {
        content.add(target -> target.addChild(GuideSnippetElement.create(snippet, tooltip)));
        return this;
    }

    public GuideSection addKeybind(String featureId, String keybindId, String label) {
        searchTerms.add(label);
        content.add(target -> {
            String binding = "Unbound";
            Feature feature = FeatureHandler.get(featureId);
            if (feature instanceof Keybound keybound) {
                KeyBinding keybind = keybound.getKeybind(keybindId);
                if (keybind != null && !keybind.isUnbound()) {
                    binding = keybind.getBoundKeyLocalizedText().getString();
                }
            }
            target.addChild(GuideRichTextElement.create(List.of(
                    new GuideSectionText.Segment(label + ": ", false),
                    new GuideSectionText.Segment(binding, true)
            )));
        });
        return this;
    }

    public GuideSection addWarning(String s) {
        searchTerms.add(s);
        content.add(target -> target.addChild(
                WrappedTextElement.create(Text.literal(s).setStyle(Styles.SCARY.getStyle()))
        ));
        return this;
    }
}
