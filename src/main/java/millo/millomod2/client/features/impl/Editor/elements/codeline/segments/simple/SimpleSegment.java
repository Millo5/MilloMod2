package millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple;

import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineSegment;
import millo.millomod2.menu.elements.TextElement;

public abstract class SimpleSegment<T> extends CodeLineSegment<T> {

    protected final TextElement content; // Potentially not final and updated in response to model changes in the future

    public SimpleSegment(T model) {
        super(model);
        this.content = createContent(model);
    }

    abstract TextElement createContent(T model);

    @Override
    public void buildVisual(CodeLineElement lineElement) {
        lineElement.addChild(content);
    }
}
