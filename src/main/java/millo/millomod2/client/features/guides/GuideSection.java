package millo.millomod2.client.features.guides;

import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.ListElement;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class GuideSection {

    private ListElement content = ListElement.create(0, 0);

    protected GuideSection() {
        super();
    }

    public ClickableWidget toElement() {
        return content;
    }

    public GuideSection addHeader(String s) {
        content.addChild(TextElement.create(s));
        return this;
    }

    public GuideSection addText(Consumer<GuideSectionText> consumer) {
        GuideSectionText text = new GuideSectionText();
        consumer.accept(text);
        content.addChild(TextElement.create(text.getText()));
        return this;
    }

    public GuideSection addWarning(String s) {
        content.addChild(TextElement.create(Text.literal(s).setStyle(Styles.SCARY.getStyle())));
        return this;
    }
}
