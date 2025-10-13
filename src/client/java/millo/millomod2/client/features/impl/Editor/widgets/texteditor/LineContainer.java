package millo.millomod2.client.features.impl.Editor.widgets.texteditor;

import millo.millomod2.client.features.impl.Editor.template.BracketLine;
import millo.millomod2.client.features.impl.Editor.template.CodeBody;
import millo.millomod2.client.features.impl.Editor.template.CodeEntry;
import millo.millomod2.client.features.impl.Editor.template.CodeLine;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.sapfii.sapscreens.screens.widgets.TextDisplayWidget;
import net.sapfii.sapscreens.screens.widgets.WidgetListBox;

public class LineContainer extends WidgetListBox {

    private int indentation = 0;

    public LineContainer(int width, int height) {
        super();
        useParentDimensions(true, true);
        withDimensions(width, height);
        withPadding(10, 10, 5);
        withAlignment(Alignment.TOPLEFT);
    }

    public void updateWidth(int width, int xOffset) {
        this.width = width;
        this.xOffset = xOffset;
    }

    public void setLines(CodeBody result) {
        this.clearChildren();

        indentation = -1;
        addBody(result);
    }

    private void addBody(CodeBody body) {
        indentation++;
        for (CodeEntry entry : body.getLines()) {
            if (entry instanceof CodeLine line) {
                addLine(line);
            } else if (entry instanceof CodeBody body1) {
                addBody(body1);
            }
        }
        indentation--;
    }

    private void addLine(CodeLine line) {
        boolean isBracket = line instanceof BracketLine;

        MutableText indentText = Text.literal(" ".repeat((indentation - (isBracket ? 1 : 0)) * 4));

        addWidget(new TextDisplayWidget(
                indentText.append(line.getDisplayText()),
                0, Alignment.LEFT));
    }
}
