package millo.millomod2.client.features.impl.Editor.template;

import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.flex.FlexElement;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public interface CodeLine extends CodeEntry{
    Text DOT = Text.literal(".");
    Text SPACE = Text.literal(" ");

    default Text getTooltip() { return null; }

    Identifier getBlockId();

    void buildOn(FlexElement<?> lineElement);

    default void append(FlexElement<?> lineElement, Text text) {
        lineElement.addChild(TextElement.create(text));
    }

    default void append(FlexElement<?> lineElement, ClickableWidget textElement) {
        lineElement.addChild(textElement);
    }
}
