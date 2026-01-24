package millo.millomod2.client.features.impl.Editor.template.lines;

import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.features.impl.Editor.template.CodeLine;
import millo.millomod2.client.hypercube.actiondump.readable.CodeBlock;
import millo.millomod2.menu.elements.flex.FlexElement;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

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
    public Identifier getBlockId() {
        return Identifier.of(block.getItem().material.toLowerCase());
    }

    @Override
    public void buildOn(FlexElement<?> lineElement) {
        append(lineElement, Text.literal(block.getIdentifier()));
        append(lineElement, DOT);
        append(lineElement, Text.literal(name));
        append(lineElement, Text.literal("("));
        Argument.buildArgumentsOn(lineElement, arguments);
        append(lineElement, Text.literal(")"));
    }
}
