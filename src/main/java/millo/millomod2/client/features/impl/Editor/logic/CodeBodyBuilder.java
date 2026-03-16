package millo.millomod2.client.features.impl.Editor.logic;

import millo.millomod2.client.features.impl.Editor.elements.CodeTextArea;
import millo.millomod2.client.features.impl.Editor.template.CodeBody;
import millo.millomod2.client.features.impl.Editor.template.CodeLine;
import millo.millomod2.client.features.impl.Editor.template.CodeLineIndentationMutation;
import millo.millomod2.client.features.impl.Editor.template.lines.ErrorLine;
import millo.millomod2.client.features.impl.TeleportHandler;
import millo.millomod2.client.hypercube.template.Template;
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

    private final Template template;
    private final String methodName;

    private int indentLevel = 0;
    private int lineNumber = 1;
    private int physicalOffset = 0;

    public CodeBodyBuilder(CodeBody body, CodeTextArea codeTextArea, Template template) {
        this.methodName = template.getName();
        this.template = template;
        this.root = body;
        this.codeTextArea = codeTextArea;
    }

    public void build() {
        buildBody(root);
    }

    private void buildBody(CodeBody body) {
        for (var entry : body.getLines()) {

            if (entry instanceof CodeLineIndentationMutation mut) indentLevel += mut.getIndentationChange() < 0 ? -1 : 0;

            if (entry instanceof CodeBody nestedBody) buildBody(nestedBody);
            if (entry instanceof CodeLine line) {
                buildLine(line);
            }

            if (entry instanceof CodeLineIndentationMutation mut) indentLevel += mut.getIndentationChange() > 0 ? 1 : 0;
            else physicalOffset += 2;
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

        int physicalOffset = this.physicalOffset;
        element.addChild(ButtonElement.create(30, 10)
                .message(Text.literal(lineNumber+++"").setStyle(Styles.LINE_NUM.getStyle()))
                .hoverBackground(0x33000000)
                .background(0)
                .onPress((button) -> {
                    TeleportHandler.teleportToMethod(template.getTemplateMethodType().prefixString(methodName), true, (pos) -> {
                        TeleportHandler.teleportTo(pos.add(-1, -1.5, physicalOffset), false);
                    });
                })
        );

        element.addChild(TextElement.create(Text.literal("  ".repeat(indentLevel)).setStyle(Styles.LINE_NUM.getStyle()))
                .align(TextElement.TextAlignment.CENTER));

        try {
            line.buildOn(element);
            codeTextArea.addChild(element);
        } catch (Exception e) {
            lineNumber--;
            buildLine(new ErrorLine("Error while building", e));
        }
    }
}
