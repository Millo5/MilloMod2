package millo.millomod2.client.features.impl.Editor.template.lines;

import millo.millomod2.client.features.impl.Editor.EditorMenu;
import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.hypercube.actiondump.readable.CodeBlock;
import millo.millomod2.client.hypercube.template.MethodType;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import millo.millomod2.menu.elements.flex.FlexElement;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class CallFunctionLine extends DynamicCodeLine {
    private static final Text FUNC_PREFIX = Text.literal("call ").setStyle(Styles.FUNCTION.getStyle());
    private static final Text PROC_PREFIX = Text.literal("start ").setStyle(Styles.PROCESS.getStyle());

    private final MethodType type;

    public CallFunctionLine(CodeBlock block, String name, ArrayList<Argument<?>> arguments, MethodType type) {
        super(block, name, arguments);
        this.type = type;
    }

    @Override
    public void buildOn(FlexElement<?> lineElement) {
        append(lineElement, switch (type) {
            case FUNC -> FUNC_PREFIX;
            case PROCESS -> PROC_PREFIX;
            default -> throw new IllegalStateException("Unexpected method type: " + type);
        });

        TextElement element = TextElement.create(name);
        element.onClickListener(() -> {
            EditorMenu.getCachedBody().tryOpenTemplate(type.suffixString(name));
            return true;
        });
        lineElement.addChild(element);

        append(lineElement, Text.literal("("));
        Argument.buildArgumentsOn(lineElement, arguments);
        append(lineElement, Text.literal(")"));
    }
}
