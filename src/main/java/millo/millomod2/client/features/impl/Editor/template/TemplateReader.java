package millo.millomod2.client.features.impl.Editor.template;

import millo.millomod2.client.hypercube.template.Template;
import millo.millomod2.client.hypercube.template.TemplateBlock;

import java.util.ArrayList;

public class TemplateReader {

    private final Template template;
    private final ArrayList<TemplateBlock> blocks;

    private int index = 0;
    private final int size;

    public TemplateReader(Template template) {
        this.template = template;
        this.blocks = new ArrayList<>(template.blocks);
        this.size = blocks.size();
    }

    public boolean hasNext() {
        return index < size;
    }

    public TemplateBlock next() {
        return blocks.get(index++);
    }

    public Template getTemplate() {
        return template;
    }

}
