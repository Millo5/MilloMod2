package millo.millomod2.client.features.impl.Editor.template.lines;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.features.impl.Editor.template.CodeLine;
import millo.millomod2.client.hypercube.actiondump.readable.CodeBlock;
import millo.millomod2.menu.elements.flex.FlexElement;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;


// Used for actions like set_var.=(x, "hello")
// Action blocks, with arguments

public class CodeActionLine implements CodeLine {

    private final CodeBlock block; // "set_var" or "player_action"
    private final String action; // "=" or "GiveItem"
    private String subAction;
    private String attribute; // NOT / LS-CANCEL
    private String target;
    private final List<Argument<?>> arguments;

    private final Identifier blockId;


    public CodeActionLine(CodeBlock block, String action, List<Argument<?>> arguments) {
        this.block = block;
        this.action = action;
        this.arguments = arguments;
        this.blockId = Identifier.of(block.getItem().material.toLowerCase());
    }

    @Override
    public Identifier getBlockId() {
        return blockId;
    }

    protected void prefix(FlexElement<?> lineElement) {
        append(lineElement, Text.literal(block.getIdentifier()));
        append(lineElement, DOT);
    }

    @Override
    public void buildOn(FlexElement<?> lineElement) {
        prefix(lineElement);
        append(lineElement, Text.literal(action));

        if (attribute != null || subAction != null) append(lineElement, DOT);

        if (attribute != null) {
            if (attribute.equals("NOT")) append(lineElement, Text.literal("!"));
            else append(lineElement, Text.literal(attribute));
        }

        if (subAction != null) {
            append(lineElement, Text.literal(subAction));
        }

        append(lineElement, Text.literal("("));
        Argument.buildArgumentsOn(lineElement, arguments);
        append(lineElement, Text.literal(")"));
        if (target != null) {
            append(lineElement, Text.literal(" -> "));
            append(lineElement, Text.literal(target));
        }
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setSubAction(String subAction) {
        this.subAction = subAction;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}
