package millo.millomod2.client.features.impl.Editor.template.lines;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.features.impl.Editor.template.CodeLine;
import millo.millomod2.client.hypercube.actiondump.readable.CodeBlock;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

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

    public CodeActionLine(CodeBlock block, String action, List<Argument<?>> arguments) {
        this.block = block;
        this.action = action;
        this.arguments = arguments;
    }

    @Override
    public Text getDisplayText() {
        MutableText text = Text.literal(block.getIdentifier())
                .append(DOT)
                .append(Text.literal(action));

        if (attribute != null || subAction != null) text.append(DOT);

        if (attribute != null) {
            if (attribute.equals("NOT")) text.append(Text.literal("!"));
            else text.append(Text.literal(attribute));
        }

        if (subAction != null) {
            text = text.append(Text.literal(subAction));
        }

        text.append(Text.literal("("));
        text.append(Argument.getArgumentsText(arguments));

        text = text.append(Text.literal(")"));

        if (target != null) {
            text = text.append(Text.literal(" -> ")).append(Text.literal(target));
        }

        return text;
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
