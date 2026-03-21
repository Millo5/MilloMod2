package millo.millomod2.client.features.impl.Editor.elements.codeline.segments;

import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineSegment;
import millo.millomod2.client.hypercube.model.arguments.ArgumentModel;
import millo.millomod2.client.hypercube.model.codeblocks.BlockCodeBlockModel;
import millo.millomod2.client.hypercube.model.codefields.ActionCodeFields;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;

public class SetVarSegment extends CodeLineSegment<BlockCodeBlockModel> {
    public SetVarSegment(BlockCodeBlockModel model) {
        super(model);
    }

    @Override
    public @Nullable Class<BlockCodeBlockModel> getModelClass() {
        return null;
    }

    @Override
    public void buildVisual(CodeLineElement lineElement) {
        if (!(model.getCodeFields() instanceof ActionCodeFields act)) {
            new ErrorSegment("Expected ActionCodeFields for set_var block").buildVisual(lineElement);
            return;
        };

        ArrayList<ArgumentModel<?>> args = model.getArgs();
        String action = act.getAction();
        switch (action) {
            case "+=", "-=", "=", "+", "-", "x":
                buildArg(lineElement, args.getFirst());
                if (args.size() == 1 && ("+= -=".contains(action))) {
                    lineElement.addChild(text(String.valueOf(action.charAt(0)) + action.charAt(0)));
                    return;
                }

                lineElement.addChild(text(" " + action + " "));

                String joiner = "+";
                if (action.equals("-") || action.equals("x")) joiner = action;
                for (int i = 1; i < args.size(); i++) {
                    buildArg(lineElement, args.get(i));
                    if (i != args.size()-1) lineElement.addChild(text(" " + joiner + " "));
                }
                break;
            default:
                buildArg(lineElement, args.getFirst());
                lineElement.addChild(text(" = " + action));
                lineElement.addChild(text("("));
                for (int i = 1; i < args.size(); i++) {
                    buildArg(lineElement, args.get(i));
                    if (i != args.size()-1) lineElement.addChild(text(", "));
                }
                lineElement.addChild(text(")"));
                break;
        }

    }

    private void buildArg(CodeLineElement lineElement, ArgumentModel<?> arg) {
        CodeLineSegment.create(arg).buildVisual(lineElement);
    }
}
