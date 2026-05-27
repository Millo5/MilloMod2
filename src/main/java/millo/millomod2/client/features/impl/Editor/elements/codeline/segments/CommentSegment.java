package millo.millomod2.client.features.impl.Editor.elements.codeline.segments;

import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineSegment;
import millo.millomod2.client.hypercube.model.arguments.ArgumentModel;
import millo.millomod2.client.hypercube.model.arguments.TextArgumentModel;
import millo.millomod2.client.hypercube.model.codeblocks.BlockCodeBlockModel;
import millo.millomod2.client.hypercube.model.codefields.ActionCodeFields;
import millo.millomod2.client.util.style.Styles;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;

public class CommentSegment extends CodeLineSegment<BlockCodeBlockModel> {
    public CommentSegment(BlockCodeBlockModel model) {
        super(model);
    }

    @Override
    public @Nullable Class<BlockCodeBlockModel> getModelClass() {
        return null;
    }

    @Override
    public void buildVisual(CodeLineElement lineElement) {
        if (!(model.getCodeFields() instanceof ActionCodeFields act)) {
            new ErrorSegment("Expected ActionCodeFields for comment").buildVisual(lineElement);
            return;
        };

        ArrayList<ArgumentModel<?>> args = model.getArgs();

        lineElement.addChild(text("// " + act.getAction(), Styles.COMMENT));
        for (ArgumentModel<?> arg : args) {
            if (arg instanceof TextArgumentModel textArg) {
                lineElement.addChild(text(textArg.getValue(), Styles.COMMENT));
            } else {
                buildArg(lineElement, arg);
            }
        }
    }

    private void buildArg(CodeLineElement lineElement, ArgumentModel<?> arg) {
        CodeLineSegment.create(arg).buildVisual(lineElement);
    }
}
