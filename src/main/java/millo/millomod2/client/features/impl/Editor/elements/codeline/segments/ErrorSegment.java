package millo.millomod2.client.features.impl.Editor.elements.codeline.segments;

import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineSegment;
import millo.millomod2.client.util.style.Styles;

public class ErrorSegment extends CodeLineSegment<String> {

    public ErrorSegment(String model) {
        super(model);
    }

    public ErrorSegment(String model, Throwable error) {
        super(model + " (" + error.getMessage() + ")");
    }

    @Override
    public Class<String> getModelClass() {
        return null;
    }

    @Override
    public void buildVisual(CodeLineElement lineElement) {
        lineElement.addChild(text("Error: " + model, Styles.SCARY));
    }
}
