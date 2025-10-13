package millo.millomod2.client.features.impl.Editor.template;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;


// Used for actions like set_var.=(x, "hello")
// Action blocks, with arguments

public class CodeActionLine implements CodeLine {

    private final static Text DOT = Text.literal(".");

    private final String block; // "set_var" or "player_action"
    private final String action; // "=" or "GiveItem"
    private final List<Argument<?>> arguments;

    public CodeActionLine(String block, String action, List<Argument<?>> arguments) {
        this.block = block;
        this.action = action;
        this.arguments = arguments;
    }

    @Override
    public Text getDisplayText() {
        MutableText text = Text.literal(block)
                .append(DOT)
                .append(Text.literal(action))
                .append(Text.literal("("));

        for (int i = 0; i < arguments.size(); i++) {
            text = text.append(arguments.get(i).getDisplayText());
            if (i < arguments.size() - 1) {
                text = text.append(Text.literal(", "));
            }
        }

        text = text.append(Text.literal(")"));
        return text;
    }

}
