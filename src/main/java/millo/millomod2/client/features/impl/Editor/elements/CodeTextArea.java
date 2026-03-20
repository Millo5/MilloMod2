package millo.millomod2.client.features.impl.Editor.elements;

import millo.millomod2.client.MilloMod;
import millo.millomod2.client.features.impl.Editor.logic.CodeBodyBuilder;
import millo.millomod2.client.features.impl.Editor.logic.model.TemplateModel;
import millo.millomod2.client.features.impl.Editor.template.CodeBody;
import millo.millomod2.client.hypercube.template.Template;
import millo.millomod2.menu.elements.ListElement;

public class CodeTextArea extends ListElement {

    private CodeBrowser browser;

    protected CodeTextArea(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public CodeTextArea(CodeBrowser browser) {
        this(0, 20, browser.getWidth(), browser.getHeight() - 20);
        padding(10);
        maxExpansion(browser.getHeight() - 20);
    }


    public void clearContents() {
        this.clearChildren();
    }

    public void loadTemplate(TemplateModel template) {
//        TemplateParser parser = new TemplateParser(template); TODO
//        CodeBody result = parser.getResult();

        MilloMod.MC.send(() -> {
            clearContents();
//            loadCodeBody(result, template);
        });
    }


    // Code loading

    private void loadCodeBody(CodeBody body, Template template) {
        CodeBodyBuilder builder = new CodeBodyBuilder(body, this, template);
        builder.build();


//        for (CodeEntry entry : body.getLines()) {
//            if (entry instanceof BracketLine bracket) indentLevel += bracket.getIndentationChange() < 0 ? -1 : 0;
//
//            if (entry instanceof CodeBody nestedBody) loadCodeBody(nestedBody, indentLevel);
//            if (entry instanceof CodeLine line) {
////                this.addChild(line.getElement());
////                TextElement text = TextElement.create(Text.literal("  ".repeat(indentLevel)).append(line.getDisplayText()));
////                Text tooltip = line.getTooltip();
////                if (tooltip != null) text.setTooltip(Tooltip.of(tooltip));
////                this.addChild(text);
//            }
//
//            if (entry instanceof BracketLine bracket) indentLevel += bracket.getIndentationChange() > 0 ? 1 : 0;
//        }
    }


}
