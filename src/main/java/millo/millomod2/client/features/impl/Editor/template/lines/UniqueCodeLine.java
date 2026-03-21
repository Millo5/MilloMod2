package millo.millomod2.client.features.impl.Editor.template.lines;

import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.actiondump.readable.CodeBlock;
import millo.millomod2.client.hypercube.template.CodeBlockType;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;

import java.util.List;

public class UniqueCodeLine extends CodeActionLine {
    private static final Text SO_PREFIX = Text.literal("select ").setStyle(Styles.SELECT.getStyle());
    private static final Text CONTROL_PREFIX = Text.literal("control ").setStyle(Styles.CONTROL.getStyle());

    private final CodeBlockType type;
    public UniqueCodeLine(CodeBlock block, String action, List<Argument<?>> arguments, CodeBlockType type) {
        super(block, action, arguments);
        this.type = type;
    }

    @Override
    protected void prefix(CodeLineElement lineElement) {
        append(lineElement, switch (type) {
            case SELECT_OBJECT -> SO_PREFIX;
            case CONTROL -> CONTROL_PREFIX;
            default -> throw new IllegalStateException("Unexpected block type: " + type);
        });
    }
}
