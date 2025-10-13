package millo.millomod2.client.features.impl.Editor.template;

import millo.millomod2.client.hypercube.actiondump.readable.ActionDump;
import millo.millomod2.client.hypercube.actiondump.readable.CodeBlock;
import millo.millomod2.client.hypercube.template.ArgumentItemSlot;
import millo.millomod2.client.hypercube.template.Arguments;
import millo.millomod2.client.hypercube.template.Template;
import millo.millomod2.client.hypercube.template.TemplateBlock;

import java.util.ArrayList;

public class TemplateParser {

    private boolean errored = false;
    private final CodeBody result;

    private final ActionDump actionDump;

    private final TemplateReader reader;

    public TemplateParser(Template template) {
        actionDump = ActionDump.getActionDump().orElseThrow();
        result = new CodeBody();
        reader = new TemplateReader(template);

        while (reader.hasNext()) {
            parseTemplateBlock(result, reader.next());
        }
    }

    private void parseTemplateBlock(CodeBody body, TemplateBlock block) {
        try {
            switch (block.id) {
                case "block" -> body.add(parseBlock(block));
                case "bracket" -> parseBody(body, block);
                default -> {
                    errored = true;
                    body.add(new ErrorLine("Unknown block type: " + block.id));
                }
            }

        } catch (ParsingException e) {
            errored = true;
            body.add(new ErrorLine("Failed to parse block: " + block.block));
        }
    }

    private void parseBody(CodeBody outerBody, TemplateBlock block) {
        if (block.direct.equals("close")) {
            errored = true;
            outerBody.add(new ErrorLine("Unmatched closing bracket for type: " + block.type));
            return;
        }

        CodeBody body = new CodeBody();
        body.add(new BracketLine(block.direct.equals("open"), block.type));

        while (reader.hasNext()) {
            TemplateBlock next = reader.next();
            if (next.id.equals("bracket") && next.direct.equals("close") && next.type.equals(block.type)) {
                break;
            } else {
                parseTemplateBlock(body, next);
            }
        }

        body.add(new BracketLine(false, block.type));
        outerBody.add(body);
    }

    private CodeLine parseBlock(TemplateBlock templateBlock) throws ParsingException {
        CodeBlock block = actionDump.getCodeBlock(templateBlock.block).orElseThrow(() -> new ParsingException("No such codeblock: " + templateBlock.block));

        ArrayList<Argument<?>> arguments = parseArguments(templateBlock.args);

        return new CodeActionLine(block.getIdentifier(), templateBlock.action == null ? "-" : templateBlock.action, arguments);
    }

    private ArrayList<Argument<?>> parseArguments(Arguments args) {
        if (args == null) return new ArrayList<>();

        ArrayList<Argument<?>> arguments = new ArrayList<>();
        for (ArgumentItemSlot itemSlot : args.items) {
            arguments.add(Argument.from(itemSlot));
        }
        return arguments;
    }

    public CodeBody getResult() {
        return result;
    }

    public boolean hasErrored() {
        return errored;
    }

    private static class ParsingException extends Exception {
        public ParsingException(String message) {
            super(message);
        }
    }


}
