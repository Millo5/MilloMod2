package millo.millomod2.client.features.impl.Editor.elements.codeline.segments;

import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineSegment;
import millo.millomod2.client.hypercube.model.arguments.ArgumentModel;
import millo.millomod2.client.hypercube.model.arguments.BlockTagArgumentModel;
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
            case "+=", "-=", "=", "+", "-", "x", "/", "%":
                buildArg(lineElement, args.getFirst());
                if (action.length() == 2 && args.size() == 1) {
                    lineElement.addChild(text(String.valueOf(action.charAt(0)) + action.charAt(0)));
                    return;
                }

                if ("+-x/%".contains(action)) {
                    lineElement.addChild(text(" = "));
                } else {
                    lineElement.addChild(text(" " + action + " "));
                }

                String joiner = "+";
                if ("-x/%".contains(action)) joiner = action;
                for (int i = 1; i < args.size(); i++) {

                    ArgumentModel<? extends ArgumentModel<?>> arg = args.get(i);
                    if (arg instanceof BlockTagArgumentModel blockTag) {
                        lineElement.addChild(text("; "));
                    } else if (i != 1) lineElement.addChild(text(" " + joiner + " "));
                    buildArg(lineElement, args.get(i));
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
