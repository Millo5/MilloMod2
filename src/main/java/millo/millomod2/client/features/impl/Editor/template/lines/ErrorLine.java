package millo.millomod2.client.features.impl.Editor.template.lines;

import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.template.CodeLine;
import millo.millomod2.client.util.MilloLog;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ErrorLine implements CodeLine {

    String message;

    private final Exception e;

    public ErrorLine(String message) {
        this.message = message;
        e = null;
    }

    public ErrorLine(String message, Exception e) {
        this.message = message;
        this.e = e;
    }

    @Override
    public Text getTooltip() {
        if (e == null) return null;

        var text = Text.literal(e.getMessage()).append("\n");
        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement traceElement : trace)
            text.append("\tat " + traceElement + "\n");


        Throwable cause = e.getCause();
        if (cause != null) {
            text.append("\t" + cause + "\n");

            for (Throwable se : cause.getSuppressed()) {
                text.append("\tSuppressed: " + se);
            }
        }

        return text;
    }

    private static final Identifier BLOCK_ID = Identifier.of("minecraft", "barrier");

    @Override
    public Identifier getBlockId() {
        return BLOCK_ID;
    }

    @Override
    public void buildOn(CodeLineElement lineElement) {
        TextElement element = TextElement.create(Text.literal("Error: " + message).setStyle(Styles.SCARY.getStyle()));

        Text tooltip = getTooltip();
        if (tooltip != null) {
            element.setTooltip(Tooltip.of(tooltip));
            element.onClickListener(() -> {
                MilloLog.logInGame(tooltip);
                return true;
            });
        }
        append(lineElement, element);
    }
}
