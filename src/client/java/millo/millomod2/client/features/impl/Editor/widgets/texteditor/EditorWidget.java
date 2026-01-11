package millo.millomod2.client.features.impl.Editor.widgets.texteditor;

import millo.millomod2.client.features.impl.Editor.template.lines.BracketLine;
import millo.millomod2.client.features.impl.Editor.template.CodeBody;
import millo.millomod2.client.features.impl.Editor.template.CodeEntry;
import millo.millomod2.client.features.impl.Editor.template.CodeLine;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class EditorWidget {

    private int indentation = 0;

    public EditorWidget() {
        super();
////        useParentDimensions(true, true);
//        withDimensions(10, 10, true);
//        withPadding(10, 10, 5);
//        withAlignment(Alignment.TOPLEFT);
    }

    public void setLines(CodeBody result) {
//        clearWidgets();

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

//        addWidget(TextWidget.create().withText(
//                indentText.append(line.getDisplayText())));
    }
}
