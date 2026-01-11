package millo.millomod2.client.features.impl.Editor.template.lines;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.features.impl.Editor.template.CodeLine;
import millo.millomod2.client.hypercube.actiondump.readable.CodeBlock;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class DynamicCodeLine implements CodeLine {

    private final CodeBlock block;
    private final String name;
    private final ArrayList<Argument<?>> arguments;

    public DynamicCodeLine(CodeBlock block, String name, ArrayList<Argument<?>> arguments) {
        this.block = block;
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public Text getDisplayText() {
        MutableText text = Text.literal(block.getIdentifier())
                .append(DOT)
                .append(Text.literal(name))
                .append(Text.literal("("));

        text.append(Argument.getArgumentsText(arguments));

        text = text.append(Text.literal(")"));
        return text;
    }
}
