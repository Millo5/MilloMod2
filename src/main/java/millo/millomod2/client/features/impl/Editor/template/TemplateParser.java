package millo.millomod2.client.features.impl.Editor.template;

import millo.millomod2.client.features.impl.Editor.template.lines.*;
import millo.millomod2.client.hypercube.actiondump.readable.ActionDump;
import millo.millomod2.client.hypercube.actiondump.readable.CodeBlock;
import millo.millomod2.client.hypercube.template.*;

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

        } catch (Exception e) {
            errored = true;
            body.add(new ErrorLine("Failed to parse block: " + block.block + ", " + e.getMessage(), e));
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

        return switch (block.getIdentifier()) {
            case "call_func" -> new CallFunctionLine(block, templateBlock.data, arguments, MethodType.FUNC);
            case "start_process" -> new CallFunctionLine(block, templateBlock.data, arguments, MethodType.PROCESS);

            case "func" -> new StarterLine(block, templateBlock.data, arguments, MethodType.FUNC);
            case "process" -> new StarterLine(block, templateBlock.data, arguments, MethodType.PROCESS);
            case "event" -> new StarterLine(block, templateBlock.action, arguments, MethodType.EVENT);
            case "entity_event" -> new StarterLine(block, templateBlock.action, arguments, MethodType.ENTITY_EVENT);
            case "game_event" -> new StarterLine(block, templateBlock.action, arguments, MethodType.GAME_EVENT);

            case "set_var" -> new SetVarCodeLine(block, templateBlock.action, arguments);
            case "select_obj" -> new UniqueCodeLine(block, templateBlock.action, arguments, CodeBlockType.SELECT_OBJECT);
            case "control" -> new UniqueCodeLine(block, templateBlock.action, arguments, CodeBlockType.CONTROL);
            default -> {
                CodeActionLine line = new CodeActionLine(block, templateBlock.action == null ? "-" : templateBlock.action, arguments);
                if (templateBlock.attribute != null) line.setAttribute(templateBlock.attribute);
                if (templateBlock.subAction != null) line.setSubAction(templateBlock.subAction);
                if (templateBlock.target != null) line.setTarget(templateBlock.target);
                yield line;
            }
        };
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
