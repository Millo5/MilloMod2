package millo.millomod2.client.features.impl.Editor.elements.codeline.components;

import millo.millomod2.client.features.impl.Editor.elements.codeline.SegmentComponent;

public class IndentationComponent extends SegmentComponent {

    private final int preIndent;
    private final int postIndent;

    public IndentationComponent(int preIndent, int postIndent) {
        this.preIndent = preIndent;
        this.postIndent = postIndent;
    }

    public int getPreIndent() {
        return preIndent;
    }

    public int getPostIndent() {
        return postIndent;
    }
}
