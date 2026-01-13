package millo.millomod2.client.features.impl.Editor.template.lines;

import millo.millomod2.client.features.impl.Editor.template.CodeLine;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.flex.FlexElement;
import net.minecraft.text.Text;

public class ErrorLine implements CodeLine {

    String message;

    public ErrorLine(String message) {
        this.message = message;
    }

    @Override
    public void buildOn(FlexElement<?> lineElement) {
        append(lineElement, Text.literal("Error: " + message).setStyle(Styles.SCARY.getStyle()));
    }
}
