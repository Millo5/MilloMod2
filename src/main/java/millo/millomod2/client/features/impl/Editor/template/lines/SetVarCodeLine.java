package millo.millomod2.client.features.impl.Editor.template.lines;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.features.impl.Editor.template.CodeLine;
import millo.millomod2.client.hypercube.actiondump.readable.CodeBlock;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

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

    @Override
    public Text getDisplayText() {
        MutableText text = Text.literal("");
        return switch (action) {
            case "+=":
            case "-=":
            case "=":
            case "+":
            case "-":
            case "x":
                text.append(arguments.getFirst().getDisplayText());
                if (arguments.size() == 1 && (action.equals("+=") || action.equals("-="))) {
                    text.append(Text.literal(String.valueOf(action.charAt(0) + action.charAt(0))));
                    yield text;
                }
                text.append(SPACE).append(Text.literal(action)).append(SPACE);

                String joiner = "+";
                if (action.equals("-") || action.equals("x")) joiner = action;
                for (int i = 1; i < arguments.size(); i++) {
                    text.append(arguments.get(i).getDisplayText());
                    if (i != arguments.size() - 1) text.append(SPACE).append(Text.literal(joiner)).append(SPACE);
                }
                yield text;
            default:
                yield Text.literal(block.getIdentifier())
                        .append(DOT)
                        .append(Text.literal(action))
                        .append(Text.literal("("))
                        .append(Argument.getArgumentsText(arguments))
                        .append(Text.literal(")"));
        };


    }

}
