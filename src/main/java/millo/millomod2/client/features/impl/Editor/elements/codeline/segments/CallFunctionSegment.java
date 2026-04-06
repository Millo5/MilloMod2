package millo.millomod2.client.features.impl.Editor.elements.codeline.segments;

import millo.millomod2.client.features.impl.Editor.EditorMenu;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineSegment;
import millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple.SimpleArgumentBuilder;
import millo.millomod2.client.hypercube.model.codefields.DynamicCodeFields;
import millo.millomod2.client.hypercube.template.MethodType;
import millo.millomod2.client.util.style.Styles;
import millo.millomod2.menu.elements.TextElement;
import net.minecraft.text.Text;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CallFunctionSegment extends CodeLineSegment<DynamicCodeFields> {
    private static final Text FUNC_PREFIX = Text.literal("call ").setStyle(Styles.FUNCTION.getStyle());
    private static final Text PROC_PREFIX = Text.literal("start ").setStyle(Styles.PROCESS.getStyle());

    private static final String expressionRegex = "%.+?\\(.+?\\)";
    private static final String containsExpressionRegex = "^.*" + expressionRegex + ".*$";

    private final MethodType methodType;

    public CallFunctionSegment(DynamicCodeFields model, MethodType methodType) {
        super(model);
        this.methodType = methodType;
    }

    @Override
    public Class<DynamicCodeFields> getModelClass() {
        return null;
    }

    @Override
    public void buildVisual(CodeLineElement lineElement) {
        lineElement.addChild(switch (methodType) {
            case FUNC -> text(FUNC_PREFIX);
            case PROCESS -> text(PROC_PREFIX);
            default -> text(methodType.name(), Styles.SCARY);
        });

        Styles style = switch (methodType) {
            case FUNC -> Styles.DICT;
            case PROCESS -> Styles.ADDED;
            default -> Styles.SCARY;
        };

        TextElement text = new SimpleArgumentBuilder(model.getData())
                .style(style)
                .onClick(getOnclick())
                .build();

        lineElement.addChild(text);
    }

    private Supplier<Boolean> getOnclick() {
        boolean expression = model.getData().matches(containsExpressionRegex);

        if (!expression) return () -> {
            EditorMenu.getCachedBody().tryOpenTemplate(methodType.suffixString(model.getData()));
            return true;
        };

        return () -> {
//            String regex = model.getData().replaceAll(expressionRegex, ".+?");
            Pattern p = Pattern.compile(expressionRegex);
            Matcher m = p.matcher(model.getData());

            StringBuilder regex = new StringBuilder();
            int lastInd = 0;

            while (m.find()) {
                regex.append(Pattern.quote(model.getData().substring(lastInd, m.start())));
                regex.append(".*?");
                lastInd = m.end();
            }

            regex.append(Pattern.quote(methodType.suffixString("")));

            EditorMenu.getCachedBody().openTemplateContext(methodType, regex.toString());
            return true;
        };
    }
}
