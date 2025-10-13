package millo.millomod2.client.features.impl.Editor.template;

import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;

public class ErrorLine implements CodeLine {

    String message;

    public ErrorLine(String message) {
        this.message = message;
    }

    @Override
    public Text getDisplayText() {
        return Text.literal("Error: " + message).setStyle(Styles.SCARY.getStyle());
    }

}
