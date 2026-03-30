package millo.millomod2.client.features.impl.Editor.elements.codeline.segments;

import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineElement;
import millo.millomod2.client.features.impl.Editor.elements.codeline.CodeLineSegment;
import millo.millomod2.client.features.impl.Editor.elements.codeline.components.BlockPrefixComponent;
import millo.millomod2.client.features.impl.Editor.elements.codeline.components.IndentationComponent;
import millo.millomod2.client.features.impl.Editor.elements.codeline.segments.simple.SimpleArgumentBuilder;
import millo.millomod2.client.hypercube.actiondump.readable.ActionDump;
import millo.millomod2.client.hypercube.model.arguments.ArgumentModel;
import millo.millomod2.client.hypercube.model.arguments.SoundArgumentModel;
import millo.millomod2.client.hypercube.model.codeblocks.BlockCodeBlockModel;
import millo.millomod2.client.hypercube.model.codefields.ActionCodeFields;
import millo.millomod2.client.hypercube.model.codefields.DynamicCodeFields;
import millo.millomod2.client.hypercube.model.codefields.SubActionCodeFields;
import millo.millomod2.client.hypercube.template.CodeBlockType;
import millo.millomod2.client.hypercube.template.MethodType;
import millo.millomod2.client.util.style.Styles;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BlockCodeBlockSegment extends CodeLineSegment<BlockCodeBlockModel> {
    private static final Text SO_PREFIX = Text.literal("select ").setStyle(Styles.SELECT.getStyle());
    private static final Text CONTROL_PREFIX = Text.literal("control ").setStyle(Styles.CONTROL.getStyle());

    private final Text prefix;
    private final CodeLineSegment<?> baseSegment;
    private final CodeLineSegment<?> lineSegment;

    public BlockCodeBlockSegment(BlockCodeBlockModel model) {
        super(model);

        lineSegment = switch (model.getBlock()) {
            case "event" -> new LineStarterSegment(model, MethodType.EVENT);
            case "entity_event" -> new LineStarterSegment(model, MethodType.ENTITY_EVENT);
            case "game_event" -> new LineStarterSegment(model, MethodType.GAME_EVENT);

            case "if_player" -> new ConditionSegment(model, CodeBlockType.IF_PLAYER);
            case "if_entity" -> new ConditionSegment(model, CodeBlockType.IF_ENTITY);
            case "if_game" -> new ConditionSegment(model, CodeBlockType.IF_GAME);
            case "if_var" -> new ConditionSegment(model, CodeBlockType.IF_VARIABLE);

            case "set_var" -> new SetVarSegment(model);
            default -> null;
        };
        baseSegment = switch (model.getBlock()) {
            case "func" -> new LineStarterSegment(model, MethodType.FUNC);
            case "process" -> new LineStarterSegment(model, MethodType.PROCESS);
            case "call_func" -> new CallFunctionSegment((DynamicCodeFields) model.getCodeFields(), MethodType.FUNC);
            case "start_process" -> new CallFunctionSegment((DynamicCodeFields) model.getCodeFields(), MethodType.PROCESS);
            default -> null;
        };
        prefix = switch (model.getBlock()) {
            case "select_obj" -> SO_PREFIX;
            case "control" -> CONTROL_PREFIX;
            default -> null;
        };

        if (lineSegment instanceof LineStarterSegment || baseSegment instanceof LineStarterSegment) addComponent(new IndentationComponent(0, 1));

        String material = ActionDump.getActionDump().orElseThrow()
                .getCodeBlock(model.getBlock()).orElseThrow()
                .getItem().material.toLowerCase();
        addComponent(new BlockPrefixComponent(Identifier.of("minecraft", material)));
    }

    @Override
    public Class<BlockCodeBlockModel> getModelClass() {
        return BlockCodeBlockModel.class;
    }

    @Override
    public void buildVisual(CodeLineElement lineElement) {
        if (lineSegment != null) {
            lineSegment.buildVisual(lineElement);
            return;
        }

        buildVisualBase(lineElement);
        buildVisualArguments(lineElement);

        if (model.getCodeFields() instanceof ActionCodeFields act) {
            if (act.getTarget() != null) lineElement.addChild(text(" -> "+act.getTarget()));
        }
    }


    private void buildVisualBase(CodeLineElement lineElement) {
        if (baseSegment != null) {
            baseSegment.buildVisual(lineElement);
            return;
        }

        if (prefix != null) lineElement.addChild(text(prefix));
        else lineElement.addChild(text(model.getBlock()));

        if (model.getCodeFields() instanceof ActionCodeFields act) {
            if (prefix == null) lineElement.addChild(text("."));
            if (act.getAttribute() != null) lineElement.addChild(text(act.getAttribute() + "."));
            if (act.getAction().contains("Sound")) {
                lineElement.addChild(new SimpleArgumentBuilder(act.getAction())
                        .onClick(() -> {
                            // Preview all sounds
                            if (model.getArgs() == null) return false;
                            for (ArgumentModel<?> arg : model.getArgs()) {
                                if (arg instanceof SoundArgumentModel sound) sound.play();
                            }

                            return false;
                        }).build());
            } else lineElement.addChild(text(act.getAction()));
            if (act instanceof SubActionCodeFields sub) {
                lineElement.addChild(text("." + sub.getSubAction()));
            }
        }
    }

    private void buildVisualArguments(CodeLineElement lineElement) {
        if (model.getArgs() != null) {
            lineElement.addChild(text("("));
            int i = 0;
            for (ArgumentModel<?> arg : model.getArgs()) {
                try {
                    CodeLineSegment.create(arg).buildVisual(lineElement);
                } catch (Exception e) {
                    lineElement.addChild(text("ERROR"));
                }

                if (++i < model.getArgs().size()) lineElement.addChild(text(", "));
            }

            lineElement.addChild(text(")"));
        }
    }
}
