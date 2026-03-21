package millo.millomod2.client.features.impl.Editor.elements.codeline.segments;

import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineSegment;
import millo.millomod2.client.hypercube.model.codeblocks.BlockCodeBlockModel;
import millo.millomod2.client.hypercube.model.codefields.ActionCodeFields;
import millo.millomod2.client.hypercube.model.codefields.DynamicCodeFields;
import millo.millomod2.client.hypercube.template.MethodType;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

public class LineStarterSegment extends CodeLineSegment<BlockCodeBlockModel> {
    private final static Text FUNC_PREFIX = Text.literal("function ").setStyle(Styles.FUNCTION.getStyle());
    private final static Text PROC_PREFIX = Text.literal("process ").setStyle(Styles.PROCESS.getStyle());
    private final static Text EVENT_PREFIX = Text.literal("player_event ").setStyle(Styles.PLAYER_EVENT.getStyle());
    private final static Text EEVENT_PREFIX = Text.literal("entity_event ").setStyle(Styles.ENTITY_EVENT.getStyle());
    private final static Text GEVENT_PREFIX = Text.literal("game_event ").setStyle(Styles.GAME_EVENT.getStyle());

    private final MethodType type;

    public LineStarterSegment(BlockCodeBlockModel model, MethodType type) {
        super(model);
        this.type = type;
    }

    @Override
    public @Nullable Class<BlockCodeBlockModel> getModelClass() {
        return null;
    }

    @Override
    public void buildVisual(CodeLineElement lineElement) {
        Text prefix = switch (type) {
            case FUNC -> FUNC_PREFIX;
            case PROCESS -> PROC_PREFIX;
            case EVENT -> EVENT_PREFIX;
            case ENTITY_EVENT -> EEVENT_PREFIX;
            case GAME_EVENT -> GEVENT_PREFIX;
        };

        lineElement.addChild(text(prefix));

        if (model.getCodeFields() instanceof ActionCodeFields act) {
            lineElement.addChild(text(act.getAction()));
            if (act.isLSCancel()) lineElement.addChild(text(" LSC", Styles.ACTION));
        }

        if (model.getCodeFields() instanceof DynamicCodeFields dyn) {
            lineElement.addChild(text(dyn.getData()));
        }
    }
}
