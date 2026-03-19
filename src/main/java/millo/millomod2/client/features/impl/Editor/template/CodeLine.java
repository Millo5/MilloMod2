package millo.millomod2.client.features.impl.Editor.template;

import millo.millomod2.client.features.impl.Editor.elements.CodeLineElement;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public interface CodeLine extends CodeEntry {
    Text DOT = Text.literal(".");
    Text SPACE = Text.literal(" ");

    default Text getTooltip() { return null; }

    Identifier getBlockId();

    void buildOn(CodeLineElement lineElement);

    default void append(CodeLineElement lineElement, Text text) {
        lineElement.addChild(TextElement.create(text));
    }

    default void append(CodeLineElement lineElement, ClickableWidget textElement) {
        lineElement.addChild(textElement);
    }
}
