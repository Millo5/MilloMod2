package millo.millomod2.client.features.impl.Editor.elements.codeline.segments;

import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineSegment;
import millo.millomod2.client.hypercube.model.arguments.ArgumentModel;
import millo.millomod2.client.hypercube.model.codeblocks.BlockCodeBlockModel;
import millo.millomod2.client.hypercube.model.codefields.ActionCodeFields;
import millo.millomod2.client.hypercube.template.CodeBlockType;
import millo.millomod2.client.util.style.Styles;

import java.util.ArrayList;

public class ConditionSegment extends CodeLineSegment<BlockCodeBlockModel> {

    private final CodeBlockType codeBlockType;

    public ConditionSegment(BlockCodeBlockModel model, CodeBlockType codeBlockType) {
        super(model);
        this.codeBlockType = codeBlockType;
    }

    @Override
    public Class<BlockCodeBlockModel> getModelClass() {
        return null;
    }

    @Override
    public void buildVisual(CodeLineElement lineElement) {
        if (!(model.getCodeFields() instanceof ActionCodeFields act)) {
            new ErrorSegment("Expected ActionCodeFields for condition block").buildVisual(lineElement);
            return;
        }

        lineElement.addChild(text(switch (codeBlockType) {
            case IF_PLAYER -> "if_player ";
            case IF_ENTITY -> "if_entity ";
            case IF_GAME -> "if_game ";
            case IF_VARIABLE -> "if ";
            default -> throw new IllegalStateException("Unexpected value: " + codeBlockType);
        }));

        ArrayList<ArgumentModel<?>> args = model.getArgs();
        if (codeBlockType == CodeBlockType.IF_VARIABLE) {
            lineElement.addChild(text("("));
            if (act.isNot()) lineElement.addChild(text("not ", Styles.UNSAVED));

            if (!args.isEmpty()) create(args.getFirst()).buildVisual(lineElement);
            lineElement.addChild(text(" " + act.getAction() + " ", Styles.ACTION));

            for (int i = 1; i < args.size(); i++) {
                if (i != 1) lineElement.addChild(text(", "));
                create(args.get(i)).buildVisual(lineElement);
            }

            lineElement.addChild(text(")"));
            return;
        }

        if (act.isNot()) lineElement.addChild(text("not ", Styles.UNSAVED));

        lineElement.addChild(text("("));
        for (int i = 0; i < args.size(); i++) {
            if (i != 0) lineElement.addChild(text(", "));
            create(args.get(i)).buildVisual(lineElement);
        }
        lineElement.addChild(text(")"));

    }

}
