package millo.millomod2.client.features.impl.Editor.template.lines;

import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.template.Argument;
import millo.millomod2.client.features.impl.Editor.template.CodeLineIndentationMutation;
import millo.millomod2.client.hypercube.actiondump.readable.CodeBlock;
import millo.millomod2.client.hypercube.template.MethodType;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class StarterLine extends DynamicCodeLine implements CodeLineIndentationMutation {

    private final static Text FUNC_PREFIX = Text.literal("function ").setStyle(Styles.FUNCTION.getStyle());
    private final static Text PROC_PREFIX = Text.literal("process ").setStyle(Styles.PROCESS.getStyle());
    private final static Text EVENT_PREFIX = Text.literal("player_event ").setStyle(Styles.PLAYER_EVENT.getStyle());
    private final static Text EEVENT_PREFIX = Text.literal("entity_event ").setStyle(Styles.ENTITY_EVENT.getStyle());
    private final static Text GEVENT_PREFIX = Text.literal("game_event ").setStyle(Styles.GAME_EVENT.getStyle());

    private final MethodType type;

    public StarterLine(CodeBlock block, String name, ArrayList<Argument<?>> arguments, MethodType type) {
        super(block, name, arguments);
        this.type = type;
    }

    @Override
    public int getIndentationChange() {
        return 1;
    }

    @Override
    public void buildOn(CodeLineElement lineElement) {
        append(lineElement, switch (type) {
            case FUNC -> FUNC_PREFIX;
            case PROCESS -> PROC_PREFIX;
            case EVENT -> EVENT_PREFIX;
            case ENTITY_EVENT -> EEVENT_PREFIX;
            case GAME_EVENT -> GEVENT_PREFIX;
        });

        append(lineElement, Text.literal(name)); // TODO: onclick to search for usages
        append(lineElement, Text.literal("("));
        Argument.buildArgumentsOn(lineElement, arguments);
        append(lineElement, Text.literal(")"));
    }
}
