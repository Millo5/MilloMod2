package millo.millomod2.client.features.impl.Editor.logic;

import millo.millomod2.client.features.impl.Editor.elements.CodeTextArea;
import millo.millomod2.client.features.impl.Editor.template.CodeBody;
import millo.millomod2.client.features.impl.Editor.template.CodeLine;
import millo.millomod2.client.features.impl.Editor.template.lines.BracketLine;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.BlockFaceElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.text.Text;

public class CodeBodyBuilder {

    private final CodeBody root;
    private final CodeTextArea codeTextArea;

    private int indentLevel = 0;
    private int lineNumber = 1;

    public CodeBodyBuilder(CodeBody body, CodeTextArea codeTextArea) {
        this.root = body;
        this.codeTextArea = codeTextArea;
    }

    public void build() {
        buildBody(root);
    }

    private void buildBody(CodeBody body) {
        for (var entry : body.getLines()) {
            if (entry instanceof BracketLine bracket) indentLevel += bracket.getIndentationChange() < 0 ? -1 : 0;

            if (entry instanceof CodeBody nestedBody) buildBody(nestedBody);
            if (entry instanceof CodeLine line) {
                buildLine(line);
            }

            if (entry instanceof BracketLine bracket) indentLevel += bracket.getIndentationChange() > 0 ? 1 : 0;
        }
    }

    private void buildLine(CodeLine line) {
        FlexElement<?> element = FlexElement.create(codeTextArea.getWidth(), 10)
                .padding(2)
                .gap(0)
                .direction(ElementDirection.ROW)
                .mainAlign(MainAxisAlignment.START)
                .crossAlign(CrossAxisAlignment.CENTER);

        element.addChild(new BlockFaceElement(line.getBlockId(), 0, 0, 10, 10));

        element.addChild(ButtonElement.create(30, 10)
                .message(Text.literal(lineNumber+++"").setStyle(Styles.LINE_NUM.getStyle()))
                .hoverBackground(0x33000000)
                .background(0)
        );

        element.addChild(TextElement.create(Text.literal("  ".repeat(indentLevel)).setStyle(Styles.LINE_NUM.getStyle()))
                .align(TextElement.TextAlignment.CENTER));

        line.buildOn(element);
        codeTextArea.addChild(element);
    }
}
