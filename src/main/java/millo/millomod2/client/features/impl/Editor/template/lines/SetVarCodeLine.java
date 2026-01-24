package millo.millomod2.client.features.impl.Editor.template.lines;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.features.impl.Editor.template.CodeLine;
import millo.millomod2.client.hypercube.actiondump.readable.CodeBlock;
import millo.millomod2.menu.elements.flex.FlexElement;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class SetVarCodeLine implements CodeLine {

    private final CodeBlock block;
    private final String action;
    private final List<Argument<?>> arguments;

    public SetVarCodeLine(CodeBlock block, String action, List<Argument<?>> arguments) {
        this.block = block;
        this.action = action;
        this.arguments = arguments;
    }

    private static final Identifier BLOCK_ID = Identifier.of("iron_block");

    @Override
    public Identifier getBlockId() {
        return BLOCK_ID;
    }

    @Override
    public void buildOn(FlexElement<?> lineElement) {
        switch (action) {
            case "+=":
            case "-=":
            case "=":
            case "+":
            case "-":
            case "x":
                append(lineElement, arguments.getFirst().toTextElement());
                if (arguments.size() == 1 && (action.equals("+=") || action.equals("-="))) {
                    append(lineElement, Text.literal(String.valueOf(action.charAt(0)) + action.charAt(0)));
                    return;
                }
                append(lineElement, SPACE);
                append(lineElement, Text.literal(action));
                append(lineElement, SPACE);

                String joiner = "+";
                if (action.equals("-") || action.equals("x")) joiner = action;
                for (int i = 1; i < arguments.size(); i++) {
                    append(lineElement, arguments.get(i).toTextElement());
                    if (i != arguments.size() - 1) {
                        append(lineElement, SPACE);
                        append(lineElement, Text.literal(joiner));
                        append(lineElement, SPACE);
                    }
                }
                break;
            default:
                append(lineElement, Text.literal(block.getIdentifier()));
                append(lineElement, DOT);
                append(lineElement, Text.literal(action));
                append(lineElement, Text.literal("("));
                Argument.buildArgumentsOn(lineElement, arguments);
                append(lineElement, Text.literal(")"));
        }
    }
}
