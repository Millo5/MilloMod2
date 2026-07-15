package millo.millomod2.client.features.guides;

import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class GuideSectionText {

    private final MutableText text = Text.empty().copy();

    public Text getText() {
        return text;
    }

    public GuideSectionText addSnippet(String s) {
        text.append(Text.literal(s).setStyle(Styles.HIGHLIGHT.getStyle()));
        return this;
    }

    public GuideSectionText addText(String s) {
        text.append(Text.literal(s));
        return this;
    }

    public GuideSectionText addText(Text t) {
        text.append(t);
        return this;
    }
}
