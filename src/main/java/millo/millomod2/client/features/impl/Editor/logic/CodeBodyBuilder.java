package millo.millomod2.client.features.impl.Editor.logic;

import millo.millomod2.client.features.impl.Editor.elements.CodeTextArea;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineSegment;
import millo.millomod2.client.features.impl.Editor.elements.codeline.components.BlockPrefixComponent;
import millo.millomod2.client.features.impl.Editor.elements.codeline.components.IndentationComponent;
import millo.millomod2.client.features.impl.Editor.elements.codeline.segments.ErrorSegment;
import millo.millomod2.client.features.impl.Editor.template.CodeBody;
import millo.millomod2.client.features.impl.Editor.template.CodeLine;
import millo.millomod2.client.features.impl.Editor.template.CodeLineIndentationMutation;
import millo.millomod2.client.features.impl.Editor.template.lines.BracketLine;
import millo.millomod2.client.features.impl.Editor.template.lines.ErrorLine;
import millo.millomod2.client.features.impl.TeleportHandler;
import millo.millomod2.client.hypercube.model.TemplateModel;
import millo.millomod2.client.hypercube.model.codeblocks.CodeBlockModel;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.BlockFaceElement;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.buttons.ButtonElement;
import millo.millomod2.menu.elements.flex.CrossAxisAlignment;
import millo.millomod2.menu.elements.flex.ElementDirection;
import millo.millomod2.menu.elements.flex.FlexElement;
import millo.millomod2.menu.elements.flex.MainAxisAlignment;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CodeBodyBuilder {
    private static final Identifier STRUCTURE_VOID_ID = Identifier.of("minecraft", "structure_void");

    private final CodeTextArea codeTextArea;

    private final TemplateModel template;
    private final String methodName;

    private int indentLevel = 0;
    private int lineNumber = 1;
    private int physicalOffset = 0;

    public CodeBodyBuilder(CodeTextArea codeTextArea, TemplateModel template) {
        this.methodName = template.getName();
        this.template = template;
        this.codeTextArea = codeTextArea;
    }

    public void build() {
        for (CodeBlockModel<?> block : template.getBlocks()) {
            buildLine(block);
        }
    }

    private void buildLine(CodeBlockModel<?> block) {
        CodeLineSegment<?> core = CodeLineSegment.create(block);

        // Calculate indentation
        IndentationComponent indent = core.getComponent(IndentationComponent.class);
        if (indent != null) indentLevel += indent.getPreIndent();
        String indentString = "  ".repeat(indentLevel);
        if (indent != null) indentLevel += indent.getPostIndent();
        if (indent == null || indent.getPreIndent() >= 0) physicalOffset += 2;

        // Initialize line element
        CodeLineElement element = new CodeLineElement(0, 0, codeTextArea.getWidth(), 10)
                .padding(2)
                .gap(0)
                .direction(ElementDirection.ROW)
                .mainAlign(MainAxisAlignment.START)
                .crossAlign(CrossAxisAlignment.CENTER);

        // Create block face element
        BlockPrefixComponent blockPrefix = core.getComponent(BlockPrefixComponent.class);
        Identifier blockId = STRUCTURE_VOID_ID;
        if (blockPrefix != null) blockId = blockPrefix.getId();
        BlockFaceElement blockFaceElement = new BlockFaceElement(blockId, 0, 0, 10, 10);
        if (indent != null && indent.getPostIndent() == 1) blockFaceElement.rotate(3.14159f);
        element.addChild(blockFaceElement);

        // Create line number button
        int physicalOffset = this.physicalOffset;
        element.addChild(ButtonElement.create(30, 10)
                .message(Text.literal(lineNumber+++"").setStyle(Styles.LINE_NUM.getStyle()))
                .hoverBackground(0x33000000)
                .background(0)
                .onPress((button) -> {
                    TeleportHandler.teleportToMethod(template.getMethodType().prefixString(methodName), true, (pos) -> {
                        TeleportHandler.teleportTo(pos.add(-1, -1.5, physicalOffset), false);
                    });
                })
        );

        // Add indentation and build line content
        element.addChild(TextElement.create(indentString));
        try {
            core.buildVisual(element);
        } catch (Exception e) {
            new ErrorSegment("Error in core", e).buildVisual(element);
            return;
        }
        codeTextArea.addChild(element);
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

        BlockFaceElement block = new BlockFaceElement(line.getBlockId(), 0, 0, 10, 10);
        if (line instanceof BracketLine bracket) {
            if (bracket.getIndentationChange() == 1) block.rotate(3.14159f);
        }
        element.addChild(block);

        int physicalOffset = this.physicalOffset;
//        element.addChild(ButtonElement.create(30, 10)
//                .message(Text.literal(lineNumber+++"").setStyle(Styles.LINE_NUM.getStyle()))
//                .hoverBackground(0x33000000)
//                .background(0)
//                .onPress((button) -> {
//                    TeleportHandler.teleportToMethod(template.getTemplateMethodType().prefixString(methodName), true, (pos) -> {
//                        TeleportHandler.teleportTo(pos.add(-1, -1.5, physicalOffset), false);
//                    });
//                })
//        );

        element.addChild(TextElement.create(Text.literal("  ".repeat(indentLevel)).setStyle(Styles.LINE_NUM.getStyle()))
                .align(TextElement.TextAlignment.CENTER));

        CodeLineElement codeLineElement = new CodeLineElement(0, 0, codeTextArea.getWidth(), 10);
        element.addChild(codeLineElement);

        try {
            line.buildOn(codeLineElement);
            codeTextArea.addChild(element);
        } catch (Exception e) {
            lineNumber--;
            buildLine(new ErrorLine("Error while building", e));
        }
    }
}
